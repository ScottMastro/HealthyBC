package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import org.cobogw.gwt.user.client.ui.Rating;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


public class StarRating {

	private Rating netRating;
	private Rating myRating;
	private String refID;
	private int score;
	private int amount;

	public StarRating(String clinicRefID){
		refID = clinicRefID;
		score = 0;
		amount = 0;

		//temporary until getStoredRating() gets real ratings
		netRating = new Rating(0, 10);
		netRating.setReadOnly(true);
		
		//TODO: store rating with user, get later
		myRating = new Rating(0, 10);


		getStoredRatings();
		setClickListener();
	}


	private void setClickListener(){
		myRating.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {

					RatingHandlerAsync ratingHandler = GWT.create(RatingHandler.class);

					AddRatingCallback callback = new AddRatingCallback();
					ratingHandler.setRating(refID, myRating.getValue(), callback);
					
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
		}

		@Override
		public void onSuccess(ArrayList<Boolean> result) {
			if(result.get(0)){
				amount += 1;
				getStoredRatings();
			}
			else{
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

			netRating.setValue(score);
			netRating.setTitle("Out of " + String.valueOf(amount) + " ratings.");

		}
	}

	public VerticalPanel getStarRating(){
		VerticalPanel vp = new VerticalPanel();
		
		Label netLabel = new Label("Total Rating:");
		vp.add(netLabel);
		vp.add(netRating);
		
		Label myLabel = new Label("My Rating:");
		vp.add(myLabel);
		vp.add(myRating);
		
		return vp;
	}
}
