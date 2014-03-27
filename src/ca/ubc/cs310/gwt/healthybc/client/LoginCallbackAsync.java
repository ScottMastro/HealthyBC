package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Shows a busy animaed gif image while doing rpc.
 * 
 * Adapted from: http://stackoverflow.com/questions/1309436/automatic-loading-indicator-when-calling-an-async-function
 * 
 * @author muquit@muquit.com Nov 27, 2012 8:18:49 PM
 */

public abstract class LoginCallbackAsync<T> implements AsyncCallback<T>
{
    protected abstract void callService(AsyncCallback<T> cb);
    
    final BusyPanel busyPanel = new BusyPanel();
    
    public void go(String message)
    {
        showLoadingMessage(message);
        execute();
    }
    
    private void showLoadingMessage(String message)
    {
        busyPanel.startProcessing(message);
    }
    
    private void hideLoadingMessage()
    {
        busyPanel.stopProcessing();
    }
    
    private void execute()
    {
        callService(new AsyncCallback<T>()
        {

            @Override
            public void onFailure(Throwable caught)
            {
                SocialLogin.handleException(caught);
                hideLoadingMessage();
                LoginCallbackAsync.this.onFailure(caught);
            }

            @Override
            public void onSuccess(T result)
            {
                hideLoadingMessage();
                LoginCallbackAsync.this.onSuccess(result);
            }
        });
    }
    
    public LoginCallbackAsync()
    {
        // TODO Auto-generated constructor stub
    }
}
