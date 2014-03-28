package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TabHandlerAsync {
	
	public void clinicTabInfo(TableInfo ti, AsyncCallback<ArrayList<ClinicTabInfo>> callback);

	public void clinicTabInfo(MapInfo mi, AsyncCallback<ArrayList<ClinicTabInfo>> callback);
	
	public void sendEmail(String text, String emailAddress, String title, AsyncCallback<ArrayList<Boolean>> callback);

}
