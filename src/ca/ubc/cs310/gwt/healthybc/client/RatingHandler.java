package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rating")
public interface RatingHandler extends RemoteService {
	
	public ArrayList<Integer> getRating(String refID, String currentUser);
	
	public ArrayList<Boolean> setRating(String refID, int score, String currentUser);
	
	public ArrayList<String> addReview(String refID, String review, String currentUser);
	
	public ArrayList<String> getAllReviews(String refID);

}
