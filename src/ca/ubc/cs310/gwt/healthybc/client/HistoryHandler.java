package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("history")
public interface HistoryHandler extends RemoteService {
	
	public String saveClinicVisit(String refID, String currentUser, String clinicName, String clinicAddress);
	public ArrayList<String> getHistory(String refID);

}
