package ca.ubc.cs310.gwt.healthybc.client;

import org.scribe.oauth.OAuthService;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class SocialLogin {

	static SocialLogin singleton = null;
	
	static Image facebookImage;
	static Image googleImage;
	static Image twitterImage;
	
	// Auth providers
	public static final int UNKNOWN		= -1;
	public static final int DEFAULT		= 0;	// no oauth, regular user/pass login
	public static final int FACEBOOK 	= 1;
	public static final int GOOGLE 		= 2;
	public static final int TWITTER 	= 3;
	
	// Protected Resource URLs
	private static final String FACEBOOK_PROTECTED_RESOURCE_URL  = "https://graph.facebook.com/me";
    private static final String GOOGLE_PROTECTED_RESOURSE_URL    = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final String TWITTER_PROTECTED_RESOURCE_URL   = "https://api.twitter.com/1/account/verify_credentials.json";
    
    private static final String CALLBACK_URL = "https://1-dot-a161527.appspot.com/";
    
    public static final String SESSION_EXPIRED_MESSAGE = "Session has expired";
    
    // Cookies
    private final static String SESSION_ID_COOKIE         = "healthybc_session_id";
    private final static String AUTH_PROVIDER_COOKIE      = "healthybc_provider";
    private final static String AUTH_PROVIDER_NAME_COOKIE = "healthybc_provider_name";
    private final static String USERNAME_COOKIE           = "healthybc_user";
    private final static String REDIRECT_URL_COOKIE       = "healthybc_redirect_url";
    
    // Loading dialog position
//    public final static int DIALOG_X_POSITION = 600;
//    public final static int DIALOG_Y_POSITION = 110;
    
	public static SocialLogin getInstance() {
		if (singleton == null)
			singleton = new SocialLogin();
		return singleton;
	}
	
	// Returns the panel containing buttons for social media logins
	public static HorizontalPanel createLoginPanel() {
		HorizontalPanel container = new HorizontalPanel();
		String imageStyle = "socialIcon";
		
		facebookImage = new Image("images/facebook.png");
		facebookImage.setStyleName(imageStyle);
		facebookImage.setTitle("Login with Facebook");
		container.add(facebookImage);
		
		googleImage = new Image("images/google.png");
		googleImage.setStyleName(imageStyle);
		googleImage.setTitle("Login with Google");
		container.add(googleImage);
		
		facebookImage.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) {
				final int authProvider = FACEBOOK;
				getAuthorizationUrl(authProvider);
			}	
		});
		
		googleImage.addClickHandler(new ClickHandler() 
		{
			@Override
			public void onClick(ClickEvent event) {
				final int authProvider = GOOGLE;
				getAuthorizationUrl(authProvider);
			}	
		});
		
		return container;
	}
	
	public static Anchor createLogoutPanel() {
		
		Anchor logoutAnchor = new Anchor("Logout");
		logoutAnchor.setWordWrap(false);
		
		logoutAnchor.addClickHandler(new ClickHandler() {
			@Override
            public void onClick(ClickEvent event)
            {
                HealthyBC.get().log("Calling logout()");
                SocialLogin.logout();
            }
		});
		
		return logoutAnchor;
	}
	
	public static void logout()
    {
        final String sessionId = getSessionIdFromCookie();
        
        new LoginCallbackAsync<Void>()
        {

            @Override
            public void onFailure(Throwable caught)
            {
                reload(); // reload anyway otherwise we're toast! we will never be able to log out
            }

            @Override
            public void onSuccess(Void result)
            {
                HealthyBC.get().log("Logged out.. clearing cookies");
                HealthyBC.get().log("Redirecting to site url");
                reload();
            }

            @Override
            protected void callService(AsyncCallback<Void> cb)
            {
                OAuthLoginService.Util.getInstance().logout(sessionId,cb);
            }
        }.go("Logging out..");
    }   

	public static boolean alreadyLoggedIn() {
        if (getSessionIdFromCookie() != null)
            return true;
        return false;
	}
	
	private static String getSessionIdFromCookie() {
		return Cookies.getCookie(SESSION_ID_COOKIE);
	}

	private static void getAuthorizationUrl(final int authProvider)
    {
        String authProviderName = getAuthProviderName(authProvider);
        final String callbackUrl = getCallbackUrl();
        HealthyBC.get().log("Getting authorization url");
        HealthyBC.get().log("authProvider: " + authProvider);
        
        final Credential credential = new Credential();
        credential.setRedirectUrl(callbackUrl);
        credential.setAuthProvider(authProvider);
        
        new LoginCallbackAsync<String>()
        {
            @Override
            public void onSuccess(String result)
            {
                String authorizationUrl = result;
                HealthyBC.get().log("Authorization url: " + authorizationUrl);
                
                // clear all cookes first
                clearCookies(); 
                
                // save the auth provider to cookie
                saveAuthProvider(authProvider);
                
                // save the redirect url to a cookie as well
                // we need to redirect there after logout
                saveRedirectUrl(callbackUrl);
                
//                Window.alert("Redirecting to: " + authorizationUrl);
                redirect(authorizationUrl);
            }
            
            @Override
            protected void callService(AsyncCallback<String> cb)
            {
                OAuthLoginService.Util.getInstance().getAuthorizationUrl(credential,cb);
            }

            @Override
            public void onFailure(Throwable caught)
            {
                handleException(caught);
            }
        }.go("Getting Authorization URL from " + authProviderName + "...");
    }
	
	public static String getCallbackUrl() {
		return CALLBACK_URL;
	}

	public static String getAuthProviderName(int authProvider) {
		 if (authProvider == FACEBOOK)
			 return "Facebook";
		 else if (authProvider == GOOGLE)
			 return "Google";
		 else if (authProvider == TWITTER)
			 return "Twitter";
		 
		 return "Default";
	}

	private static void redirect(String url) {
		Window.Location.assign(url);
	}

	private static void saveRedirectUrl(String url) {
		Cookies.setCookie(REDIRECT_URL_COOKIE, url);		
	}

	private static void saveAuthProvider(int authProvider) {
		Cookies.setCookie(AUTH_PROVIDER_COOKIE,Integer.toString(authProvider)); 
		String authProviderName = getAuthProviderName(authProvider);
		saveAuthProviderName(authProviderName);
	}

	private static void saveAuthProviderName(String authProviderName) {
		Cookies.setCookie(AUTH_PROVIDER_NAME_COOKIE,authProviderName);
	}

	private static void clearCookies() {
		Cookies.removeCookie(SESSION_ID_COOKIE);
        Cookies.removeCookie(AUTH_PROVIDER_COOKIE);
        Cookies.removeCookie(AUTH_PROVIDER_NAME_COOKIE);
        Cookies.removeCookie(USERNAME_COOKIE);
        Cookies.removeCookie(REDIRECT_URL_COOKIE);
	}

	public static void handleException(Throwable caught) {
		if (caught.getMessage().equals(SocialLogin.SESSION_EXPIRED_MESSAGE))
		{
			showSessionExpired();
		}
		else
		{
			showGenericException(caught);
		}
	}

	private static void showGenericException(Throwable caught) {
        String message = "Exception: " + caught;
        message += "\n";
        message += "Please Logout/reload the application";
//        String st = LogUtil.stackTraceToString(caught);
//        message += "Stack Trace:\n" + st;
        Window.alert(message);
	}

	private static void showSessionExpired() {
		Window.alert("Your session seems to have expired!\n" +  "You will be logged out");
        reload();
	}

	public static void reload() {
        String appUrl = getRedirectUrlFromCookie();
        int savedAuthProvider = getAuthProviderFromCookieAsInt();
        
        clearCookies();
        
        if (savedAuthProvider == DEFAULT || savedAuthProvider == UNKNOWN)
        {
            HealthyBC.updateLoginStatus();
        }
 
        if (appUrl != null)
        {
            redirect(appUrl);
        }
	}

    private static String getRedirectUrlFromCookie() {
    	return Cookies.getCookie(REDIRECT_URL_COOKIE);
	}

	public static int getAuthProviderFromCookieAsInt()
    {
       String authProviderStr = getAuthProviderFromCookie();
       int authProvider = UNKNOWN;
       if (authProviderStr != null)
       {
           try
           {
               authProvider = Integer.parseInt(authProviderStr);
               
           }
           catch(NumberFormatException e)
           {
               return UNKNOWN;
           }
       }
       
       return authProvider;
    }

    public static String getProtectedResourceUrl(int authProvider)
    {
        switch(authProvider)
        {
            case FACEBOOK:
            {
                return FACEBOOK_PROTECTED_RESOURCE_URL;
            }
            
            case GOOGLE:
            {
                return GOOGLE_PROTECTED_RESOURSE_URL;
            }

            case TWITTER:
            {
                return TWITTER_PROTECTED_RESOURCE_URL;
            }
            
            default:
            {
                return null;
            }
        }
    }

	public static String getAuthProviderNameFromCookie() {
		return Cookies.getCookie(AUTH_PROVIDER_NAME_COOKIE);
	}

	public static String getUsernameFromCookie() {
		return Cookies.getCookie(USERNAME_COOKIE);
	}

    public static boolean redirected()
    {
        String authProvider = getAuthProviderFromCookie();
        if (authProvider == null)
        {
            return false;
        }
        
        if (Location.getParameter("code") != null) //facebook, google
            return true;
        
        if (Location.getParameter("oauth_token") != null) // twitter
            return true;
        
        String error = Location.getParameter("error");
        if (error != null)
        {
            String errorMessage = Location.getParameter("error_description");
            Window.alert("Error: " + error + ":" + errorMessage);
            reload();
            return false;
        }
        
        return false;
    }

	private static String getAuthProviderFromCookie() {
		return Cookies.getCookie(AUTH_PROVIDER_COOKIE);
	}

	public static Credential getCredential() throws LoginAuthException
    {
        String authProvider = getAuthProviderFromCookie();
        if (authProvider == null)
            return null;
        int ap = DEFAULT;
        
        try
        {
            ap = Integer.parseInt(authProvider);
        }
        catch(Exception e)
        {
            throw new LoginAuthException("Could not convert authProvider " + authProvider + " to Integer");
        }
        
        switch(ap)
        {
            case DEFAULT:
            {
                Credential credential = new Credential();
                credential.setAuthProvider(ap);
                return credential;
            }
            case FACEBOOK:
            {
            	Credential credential = new Credential();
                credential.setAuthProvider(ap);
                credential.setState(Location.getParameter("state"));
                credential.setVerifier(Location.getParameter("code"));
                return credential;
            }
            case GOOGLE:
            {
                Credential credential = new Credential();
                credential.setAuthProvider(ap);
                credential.setVerifier(Location.getParameter("code"));
                return credential;
            }
            case TWITTER:
            {
                Credential credential = new Credential();
                credential.setAuthProvider(ap);
                credential.setVerifier(Location.getParameter("oauth_verifier"));
                return credential;
            }
            
            default:
            {
                throw new LoginAuthException("ClientUtils.getCredential: Auth Provider " + authProvider + " Not implemented yet");
            }
        }
    }

	public static void saveSessionId(String sessionId) {
		Cookies.setCookie(SESSION_ID_COOKIE,sessionId);
	}

	public static void saveUsername(String name) {
		Cookies.setCookie(USERNAME_COOKIE,name);
	}
}
