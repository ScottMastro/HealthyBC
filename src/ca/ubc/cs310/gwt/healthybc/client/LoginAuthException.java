package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

/**
 * @author muquit@muquit.com Aug 19, 2012 10:04:36 AM
 */
public class LoginAuthException extends Exception implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String message;
    public LoginAuthException()
    {
    }

    public LoginAuthException(String message)
    {
        super(message);
        this.message=message;
    }

    public LoginAuthException(Throwable cause)
    {
        super(cause);
        if (cause != null)
        {
            if (this.message == null)
            {
                this.message=cause.getMessage();
            }
        }
    }

    public LoginAuthException(String message,Throwable cause)
    {
        super(message,cause);
        this.message=message;
    }

    public String getMessage()
    {
        return(message);
    }
    
}
