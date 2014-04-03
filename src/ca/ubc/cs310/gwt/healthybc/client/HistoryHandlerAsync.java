package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HistoryHandlerAsync {
	
	public void saveClinicVisit(String refID, String currentUser, String clinicName, String clinicAddress, AsyncCallback<String> callback);
	public void getHistory(String refID, AsyncCallback<ArrayList<String>> callback);
}
