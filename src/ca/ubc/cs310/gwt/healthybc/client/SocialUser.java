package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

/**
 * The object we send back and forth between client and server.
 * Note: More information is stored in server side in ServersideSession
 * object.
 * @author muquit@muquit.com
 */
public class SocialUser implements Serializable
{
    private static final long serialVersionUID=1011L;
    private String sessionId;
    
    private String email;
    private String json;
    
    private String firstName;
    private String lastName;
    
    /* must be named exactly as JSON google returns -- starts */
    private String id;
    private String name;        // full name
    private String given_name;  // first name
    private String family_name; // last name
    private String gender;      // same on Yahoo   
    private String link;
    private String locale;
    /* must be named exactly as JSON google returns -- ends */
    
    /*
     Google returns JSON like:
     
      {
          "id": "116397076041912827850", 
          "name": "Muhammad Muquit", 
          "given_name": "Muhammad", 
          "family_name": "Muquit", 
          "link": "https://plus.google.com/116397076041912827850", 
          "gender": "male", 
          "locale": "en-US"
       }
     */
    
    public String getSessionId()
    {
        return sessionId;
    }
    public void setSessionId(String sessionId)
    {
        this.sessionId=sessionId;
    }
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }
    public String getJson()
    {
        return json;
    }
    public void setJson(String json)
    {
        this.json=json;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id=id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getGiven_name()
    {
        return given_name;
    }
    public void setGiven_name(String given_name)
    {
        this.given_name=given_name;
    }
    public String getFamily_name()
    {
        return family_name;
    }
    public void setFamily_name(String family_name)
    {
        this.family_name=family_name;
    }
    public String getGender()
    {
        return gender;
    }
    public void setGender(String gender)
    {
        this.gender=gender;
    }
    public String getLink()
    {
        return link;
    }
    public void setLink(String link)
    {
        this.link=link;
    }
    public String getLocale()
    {
        return locale;
    }
    public void setLocale(String locale)
    {
        this.locale=locale;
    }
    public String getFirstName() {
    	return firstName;
    }
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}

