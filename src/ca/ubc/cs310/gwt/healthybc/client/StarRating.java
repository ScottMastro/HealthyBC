package ca.ubc.cs310.gwt.healthybc.client;
import java.util.ArrayList;

import org.cobogw.gwt.user.client.ui.Rating;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class StarRating {

	private Rating rating;
	private String refID;
	private boolean isRated = false;
	private int score;
	private int amount;

	public StarRating(String clinicRefID){
		refID = clinicRefID;
		score = 0;
		amount = 0;

		getStoredRatings();
		rating = new Rating(score,10);

		setClickListener();
	}


	private void setClickListener(){
		rating.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {

				if(!isRated){
					RatingHandlerAsync ratingHandler = GWT.create(RatingHandler.class);

					AddRatingCallback callback = new AddRatingCallback();
					ratingHandler.setRating(refID, rating.getValue(), callback);

					isRated = true;
				}

			}
		});
	}

	/**
	 * Response from server after adding new rating
	 */
	private class AddRatingCallback implements AsyncCallback<ArrayList<Boolean>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			isRated = false;
		}

		@Override
		public void onSuccess(ArrayList<Boolean> result) {
			if(result.get(0)){
				amount += 1;						
				isRated = true;
			}
			else{
				isRated = false;
				Window.alert("Unable to send rating");
			}
		}
	}
	
	
	private void getStoredRatings(){
		RatingHandlerAsync ratingHandler = GWT.create(RatingHandler.class);

		GetRatingCallback callback = new GetRatingCallback();
		ratingHandler.getRating(refID, callback);
	}
	
	/**
	 * Response from server after requesting rating
	 */
	private class GetRatingCallback implements AsyncCallback<ArrayList<Integer>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}

		@Override
		public void onSuccess(ArrayList<Integer> result) {
			score = result.get(0);
			amount = result.get(1);

		}
	}

	public Rating getStarRating(){
		return rating;
	}
}
