package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import ca.ubc.cs310.gwt.healthybc.client.HistoryHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HistoryHandlerImpl extends RemoteServiceServlet implements HistoryHandler {

	public HistoryHandlerImpl(){
	}

	@Override
	public ArrayList<String> getHistory(String username) {
		RemoteDataManager rdm = new RemoteDataManager();		
		ArrayList<String> response = rdm.getUserHistory(username);
		return response;
	}

	@Override
	public String saveClinicVisit(String refID, String currentUser, String clinicName, String clinicAddress) {
		RemoteDataManager rdm = new RemoteDataManager();		
		String response = rdm.saveClinicVisit(refID, currentUser, clinicName, clinicAddress);
		
		return response;
	}

}
