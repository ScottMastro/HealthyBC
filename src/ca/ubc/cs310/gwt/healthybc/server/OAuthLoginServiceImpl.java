/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ca.ubc.cs310.gwt.healthybc.server;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.builder.api.GoogleApi20;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import ca.ubc.cs310.gwt.healthybc.client.Credential;
import ca.ubc.cs310.gwt.healthybc.client.LoginAuthException;
import ca.ubc.cs310.gwt.healthybc.client.OAuthLoginService;
import ca.ubc.cs310.gwt.healthybc.client.SocialLogin;
import ca.ubc.cs310.gwt.healthybc.client.SocialUser;

import com.allen_sauer.gwt.log.client.LogUtil;

// GWT has similar class. 
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class OAuthLoginServiceImpl extends RemoteServiceServlet implements OAuthLoginService
{
    private final String SESSION_ID             		= "HBC_sessionid";
    private final String SESSION_FACEBOOK_REQUEST_TOKEN = "HBC_facebook_request_token";
    private final String SESSION_GOOGLE_REQUEST_TOKEN 	= "HBC_google_request_token";
    private final String SESSION_NONCE          		= "HBC_nonce";
    private final String SESSION_PROTECTED_URL  		= "HBC_protected_url";
    private final String SESSION_ACCESS_TOKEN   		= "HBC_access_token";
    private final String SESSION_AUTH_PROVIDER 			= "HBC_auth_provider";

    private final static Log logger=LogFactory .getLog(OAuthLoginServiceImpl.class);

    private OAuthService getOAuthService(int authProvider) throws LoginAuthException
    {
        OAuthService service = null;
        switch(authProvider)
        {
            case SocialLogin.FACEBOOK:
            {
                service = new ServiceBuilder()
                    .provider(FacebookApi.class)
                    .apiKey(SocialSettings.FACEBOOK_APP_ID)
                    .apiSecret(SocialSettings.FACEBOOK_APP_SECRET)
                    .callback(SocialLogin.getCallbackUrl())
                    .build();
                break;
            }
            
            case SocialLogin.GOOGLE:
            {
                service = new ServiceBuilder()
                    .provider(GoogleApi20.class)
                    .apiKey(SocialSettings.GOOGLE_APP_ID)
                    .apiSecret(SocialSettings.GOOGLE_APP_SECRET)
                    .scope(SocialSettings.GOOGLE_SCOPE)
                    .callback(SocialLogin.getCallbackUrl())
                    .build();
                
                break;
            }
            
            default:
            {
                return null;
            }
            
        }
        return service;
    }

    @Override
    public String getAuthorizationUrl(Credential credential) throws LoginAuthException
    {
        logger.info("callback url: " + credential.getRedirectUrl());
        String authorizationUrl = null;
        Token requestToken = null;
        
        int authProvider = credential.getAuthProvider();
        
        OAuthService service = getOAuthService(authProvider);
        if (service == null)
        {
            throw new LoginAuthException("Could not build OAuthService");
        }
 
        logger.info("Getting Authorization url...");
        try
        {
            authorizationUrl = service.getAuthorizationUrl(requestToken);

            // Facebook has optional state var to protect against CSFR.
            // We'll use it
            if (authProvider == SocialLogin.FACEBOOK)
            {
                String state = makeRandomString();
                authorizationUrl+="&state=" + state;
                saveStateToSession(state);
            }
        }
        catch(Exception e)
        {
            String st = LogUtil.stackTraceToString(e);
            throw new LoginAuthException("Could not get Authorization url: " + st);
        }
        
        logger.info("Authorization url: " + authorizationUrl);
        
        return authorizationUrl;
    }
    
    private void saveStateToSession(String state) throws LoginAuthException {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        session.setAttribute(SESSION_NONCE,state);	
	}
    
	public static String stackTraceToString(Throwable caught)
    {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement e : caught.getStackTrace())
        {
            sb.append(e.toString()).append("\n");
        }
        return sb.toString();
    }    
    
    private void saveRequestTokenToSession(Token requestToken, int authProvider) throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        switch (authProvider) {
        	case SocialLogin.FACEBOOK: 
        	{
        		session.setAttribute(SESSION_FACEBOOK_REQUEST_TOKEN,requestToken);
        	}
        	case SocialLogin.GOOGLE:
        	{
        		session.setAttribute(SESSION_GOOGLE_REQUEST_TOKEN,requestToken);
        	}
        }
        
    }

    private void saveSessionIdToSession(String sessionId) throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        session.setAttribute(SESSION_ID,sessionId);
    }
    
    private void saveProtectedResourceUrlToSession(String url) throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        session.setAttribute(SESSION_PROTECTED_URL,url);
    }
    
    private void saveAuthProviderToSession(int authProvider) throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        session.setAttribute(SESSION_AUTH_PROVIDER,authProvider);
    }
    
    private void saveAccessTokenToSession(Token accessToken) throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        session.setAttribute(SESSION_ACCESS_TOKEN,accessToken);
    }
    
    private String getProtectedResourceUrlFromSession() throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        return (String) session.getAttribute(SESSION_PROTECTED_URL);
    }
    
    private String getStateFromSession() throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        return (String) session.getAttribute(SESSION_NONCE);
    }
    
    private String getSessionIdFromSession() throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        String sessionId = (String) session.getAttribute(SESSION_ID);
        return sessionId;
    }
    
    private Token getRequestTokenFromSession(int authProvider) throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        switch (authProvider) 
        {
        	case SocialLogin.FACEBOOK:
        	{
        		return (Token) session.getAttribute(SESSION_FACEBOOK_REQUEST_TOKEN);
        	}
        	case SocialLogin.GOOGLE:
        	{
        		return (Token) session.getAttribute(SESSION_GOOGLE_REQUEST_TOKEN);
        	}
        }
		return null;
    }

    private String makeRandomString()
    {
        UUID uuid=UUID.randomUUID();
        return uuid.toString();
    }
    
    private SocialUser getSocialUser(Token accessToken,int authProvider) throws LoginAuthException
    {
        logger.info("Token: " + accessToken + " Provider: " + authProvider);
        OAuthService service = getOAuthService(authProvider);
        
        String url = getProtectedResourceUrlFromSession();
        OAuthRequest request = new OAuthRequest(Verb.GET,url);
        // sign the request
        service.signRequest(accessToken,request);
        Response response = request.send();
        String json = response.getBody();
        SocialUser socialUser = getSocialUserFromJson(json,authProvider);
        return socialUser;
    }

    @Override
    public SocialUser verifySocialUser(Credential credential) throws LoginAuthException
    {
        int authProvider = credential.getAuthProvider();
        logger.info("authProvider: " + authProvider);
        String authProviderName = SocialLogin.getAuthProviderName(authProvider);
        logger.info("Verifying social usr from " + authProviderName);
        
        Token requestToken              = null;
        String protectedResourceUrl = SocialLogin.getProtectedResourceUrl(authProvider);
        
        if (authProvider == SocialLogin.FACEBOOK)
        {
            logger.info("Verifying state: " + credential.getState());
            verifyState(credential.getState()); 
        }  
        
        /* if there is any request token in session, get it */
        requestToken = getRequestTokenFromSession(authProvider);
        
        OAuthService service = null;
        Verifier verifier    = null;
        Token accessToken    = null;
        
        /* Get Access Token */
        if (authProvider != SocialLogin.DEFAULT)
        {
            service = getOAuthService(authProvider);
            verifier = new Verifier(credential.getVerifier());
            logger.info("Requesting access token with requestToken: " + requestToken);
            logger.info("verifier=" + verifier);
            try
            {
                accessToken = service.getAccessToken(requestToken,verifier);
                if (accessToken == null)
                {
                    logger.error("Could not get Access Token for " + authProviderName);
                    throw new LoginAuthException("Could not get Access Token");
                }
            }
            catch (Exception e)
            {
                logger.info("Exception received gettign Access Token: " + e);
                throw new LoginAuthException("Exception received getting Access Token: " + e);
            }
            logger.info("Got the access token: " + accessToken);
            logger.info(" Token: " + accessToken.getToken());
            logger.info(" Secret: " + accessToken.getSecret());
            logger.info(" Raw: " + accessToken.getRawResponse());
        }
        
        // make session id
        String sessionId = makeRandomString();
        
        // must save session id to session
        saveSessionIdToSession(sessionId);
        
        // must save authProvider to session
        saveAuthProviderToSession(authProvider);
        
        SocialUser socialUser = null;
        if (authProvider != SocialLogin.DEFAULT)
        {
            // must save access token to session
            saveAccessTokenToSession(accessToken);
            
            // must save the protected resource url to session
            saveProtectedResourceUrlToSession(protectedResourceUrl);
            
            // now request protected resource
            logger.info("Getting protected resource");
            logger.info("Protected resource url: " + protectedResourceUrl);
            try
            {
                OAuthRequest request = new OAuthRequest(Verb.GET,protectedResourceUrl);
                service.signRequest(accessToken,request);
                
                Response response = request.send();
                logger.info("Status code: " + response.getCode());
                logger.info("Body: " + response.getBody());
                
                String json = response.getBody();
                socialUser = getSocialUserFromJson(json,authProvider);
            }
            catch(Exception e)
            {
                logger.error("Could not retrieve protected resource: " + e);
                throw new LoginAuthException("Could not retrieve protected resource: " + e);
            }
        }
        socialUser.setSessionId(sessionId);
        
        return socialUser;
    }
    
    private Token getAccessTokenFromSession() throws LoginAuthException
    {
        HttpSession session = getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        return (Token) session.getAttribute(SESSION_ACCESS_TOKEN);
    }
    
    private SocialUser getSocialUserFromJson(String json, int authProvider) throws LoginAuthException
    {
        String authProviderName = SocialLogin.getAuthProviderName(authProvider); 
        logger.info("Auth provider: " + authProviderName);
        
        JSONParser jsonParser = new JSONParser();
        Object obj = null;
        SocialUser socialUser = new SocialUser();
        json = SocialSettings.prettyPrintJsonString(json);
        switch(authProvider)
        {
            case SocialLogin.FACEBOOK:
            {
                try
                {
                    obj = jsonParser.parse(json);
                    JSONObject jsonObj = (JSONObject) obj;

                    socialUser.setJson(json);
                    socialUser.setName((String) jsonObj.get("name"));
                    
                    return socialUser;
                }
                catch (ParseException e)
                {
                  throw new LoginAuthException("Could not parse JSON data from " + authProviderName + ":" + e.getMessage());
                }
            }

            case SocialLogin.GOOGLE:
            {
                try
                {
                    obj = jsonParser.parse(json);
                    JSONObject jsonObj = (JSONObject) obj;
                
                    socialUser.setJson(json);
                
                    socialUser.setName((String) jsonObj.get("name"));
                    socialUser.setFirstName((String) jsonObj.get("given_name"));
                    socialUser.setLastName((String) jsonObj.get("family_name"));
                    socialUser.setGender((String)jsonObj.get("gender"));
                    
                    return socialUser;
                }
                catch (ParseException e)
                {
                  throw new LoginAuthException("Could not parse JSON data from " + authProviderName + ":" + e.getMessage());
                }
            }
            
            default:
            {
                throw new LoginAuthException("Unknown Auth Provider: " + authProviderName);
            }
        }
    }
 
    private void verifyState(String state) throws LoginAuthException
    {
        String stateInSession = getStateFromSession();
        if (stateInSession == null)
        {
            throw new LoginAuthException("Could not find state in session");
        }
        if (!stateInSession.equals(state))
        {
            throw new LoginAuthException("State mismatch in session, expected: " + stateInSession + " Passed: " + state);
        }
    }

    private HttpSession getHttpSession()
    {
        return getThreadLocalRequest().getSession();
    }

    private HttpSession validateSession(String sessionId) throws LoginAuthException
    {
        if (sessionId == null)
            throw new LoginAuthException("Session Id can not be empty");
        HttpSession session=getHttpSession();
        if (session == null)
        {
            throw new LoginAuthException(SocialLogin.SESSION_EXPIRED_MESSAGE);
        }
        String savedSessionId = getSessionIdFromSession();
        if (sessionId.equals(savedSessionId))
        {
            return session;
        }
        throw new LoginAuthException("Session Id mismatch: expected " + "'" + sessionId + "'" + " Found: " + "'" + savedSessionId + "'");
    }

    @Override
    public void logout(String sessionId, int authProvider) throws LoginAuthException
    {
        validateSession(sessionId);
        
        // getHttpSession().invalidate();
        logger.info("Invalidated HTTP session");
        
    }
    
    @Override
    public String getAccessToken(String sessionId) throws LoginAuthException
    {
        validateSession(sessionId);
        Token accessToken = getAccessTokenFromSession();
        if (accessToken == null)
        {
            throw new LoginAuthException("Could not find Access Token in HTTP Session");
        }
        return accessToken.getRawResponse();
    }

}
