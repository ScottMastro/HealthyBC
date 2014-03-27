package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rating")
public interface RatingHandler extends RemoteService {
	
	public ArrayList<Integer> getRating(String refID);
	
	public ArrayList<Boolean> setRating(String refID, int score);
	
	public ArrayList<String> addReview(String refID, String review);


}
