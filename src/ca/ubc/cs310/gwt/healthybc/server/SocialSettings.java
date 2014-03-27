package ca.ubc.cs310.gwt.healthybc.server;

import java.util.UUID;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;

public class SocialSettings
{
    /**  Facebook starts Dec-16-2012 -- */
    public static final String FACEBOOK_APP_NAME    = "OAuthLoginDemo";
    public static final String FACEBOOK_APP_ID      = "742561872429487";
    public static final String FACEBOOK_APP_SECRET  = "b7317134051ad46347f68a6bb7baea81";
    /** Facebook ends -- */
    
    /** Google starts Dec-16-2012 -- */
    public static final String GOOGLE_APP_NAME      = "OAuthLoginDemo";
    public final static String GOOGLE_APP_ID        = "426934140323.apps.googleusercontent.com";
    public final static String GOOGLE_APP_SECRET    = "6RcRY0q-jpl2216Rp_VY2olx";
    public final static String GOOGLE_SCOPE         = "https://www.googleapis.com/auth/userinfo.profile";
    /** Google ends -- */
    
    /** Twitter starts Dec-16-2012 -- */
    public static final String TWITTER_APP_NAME     = "OAuthLoginDemo";
    public final static String TWITTER_APP_ID       = "8WvNyhgdDgaFDyQY3KCssA";
    public final static String TWITTER_APP_SECRET   = "sEWSOVfFLUUpoeYURj56y11gD9BgfAYPDsnVMzmuvm4";
    /** Twitter ends -- */

    public static String makeRandomString()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
    /**
     * Pretty print json string.
     * @param uglyJson
     * @return prettyfied json if possible, ugly one otherwise
     * I found Argo is the only one which can format a json string the way I want.
     * Argo: http://argo.sourceforge.net/documentation.html
     */
    public static String prettyPrintJsonString(String uglyJson)
    {
        try
        {
            JsonRootNode jsonRootNode = new JdomParser().parse(uglyJson);
            JsonFormatter jsonFormatter = new PrettyJsonFormatter();
            String prettyJson = jsonFormatter.format(jsonRootNode);
            return prettyJson;
        }
        catch(Exception e)
        {
            return uglyJson;
        }
    }
}
