package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import ca.ubc.cs310.gwt.healthybc.client.RatingHandler;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class RatingHandlerImpl extends RemoteServiceServlet implements RatingHandler {


	public RatingHandlerImpl(){
	}

	public ArrayList<Integer> getRating(String refID, String currentUser) {
		
		RemoteDataManager rdm = new RemoteDataManager();		
		return rdm.getClinicRating(refID, currentUser);

	}
	
	public ArrayList<Boolean> setRating(String refID, int rating, String currentUser) {
		
		RemoteDataManager rdm = new RemoteDataManager();		
		boolean response = rdm.submitClinicRating(refID, rating, currentUser);
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(response);
		
		return a;

	}
	
	public ArrayList<String> addReview(String refID, String review){
		
		RemoteDataManager rdm = new RemoteDataManager();		
		String response = rdm.submitClinicReview(refID, review);
		ArrayList<String> a = new ArrayList<String>();
		a.add(response);
		
		return a;		
	}

}