package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.RatingHandler;


@SuppressWarnings("serial")
public class RatingHandlerImpl extends RemoteServiceServlet implements RatingHandler {


	public RatingHandlerImpl(){
	}

	public ArrayList<Integer> getRating(String refID) {
		
		RemoteDataManager rdm = new RemoteDataManager();		
		return rdm.getClinicRating(refID);

	}
	
	public ArrayList<Boolean> setRating(String refID, int rating) {
		
		RemoteDataManager rdm = new RemoteDataManager();		
		boolean response = rdm.submitClinicRating(refID, rating);
		ArrayList<Boolean> a = new ArrayList<Boolean>();
		a.add(response);
		
		return a;

	}
	
	public ArrayList<Boolean> addReview(String review){
		
		//TODO: something
		return null;
		
	}


}