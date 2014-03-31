package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;
import java.util.Date;

import org.cobogw.gwt.user.client.ui.Rating;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RatingTab {

	private TextArea reviewBox;
	private Button submit;
	private DockPanel panel;
	private boolean initialBoxState;

	private Rating netRating;
	private Rating myRating;
	private String refID;
	private int score;
	private int amount;
	private int myScore;
	private String currentUser;
	private ArrayList<CommentBlock> commentBlocks;

	private final String DEFAULT_TEXT = "Click here to review clinic (Limit: 500 characters)";

	public RatingTab(String refID, String currentUser){
		this.currentUser = currentUser;
		this.refID = refID;
		initialBoxState = true;
		commentBlocks = new ArrayList<CommentBlock>();

		VerticalPanel reviewPanel = createReviewBox();
		VerticalPanel starPanel = makeStarRating();

		HorizontalPanel ratingPanel = new HorizontalPanel();
		ratingPanel.add(starPanel);
		ratingPanel.add(reviewPanel);

		panel = new DockPanel();
		panel.add(ratingPanel, DockPanel.NORTH);

		getAllReviews();
	}

	public DockPanel getRatingTab(){
		return panel;
	}

	private VerticalPanel createReviewBox(){
		reviewBox = new TextArea();
		reviewBox.addStyleName("boxBefore");
		reviewBox.setText(DEFAULT_TEXT);

		reviewBox.setWidth("500px");
		reviewBox.setHeight("150px");		

		reviewBox.addFocusHandler(new FocusHandler(){

			public void onFocus(FocusEvent event) {
				if (initialBoxState)
					change();				
			}

		});

		submit = new Button("Submit");
		submit.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				submitReview();
			}

		});

		VerticalPanel tempPanel = new VerticalPanel();		
		tempPanel.add(reviewBox);
		tempPanel.add(submit);
		tempPanel.addStyleName("paddedPanel");

		return tempPanel;
	}

	private void change(){
		reviewBox.removeStyleName("boxBefore");
		reviewBox.addStyleName("boxAfter");

		reviewBox.setText("");
		initialBoxState = false;
	}

	private void submitReview() {

		String review = reviewBox.getText();
		if(review == null || review.isEmpty() || review.equals(DEFAULT_TEXT)){
			Window.alert("Please enter a review before submitting.");
			return;
		}

		if(review.length() >= 500){
			Window.alert("Review should be less than 500 characters.");
			return;
		}

		disableReviews();

		RatingHandlerAsync ratingHandler = GWT.create(RatingHandler.class);
		AddReviewCallback callback = new AddReviewCallback();

		ratingHandler.addReview(refID, review, currentUser, callback);
	}

	/**
	 * Response from server after requesting rating
	 */
	private class AddReviewCallback implements AsyncCallback<ArrayList<String>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			enableReviews();
			Window.alert("Problem connecting to datastore.");

		}

		@Override
		public void onSuccess(ArrayList<String> result) {

			if(result != null && !result.isEmpty())
				Window.alert(result.get(0));

		}
	}

	private void getAllReviews(){

		RatingHandlerAsync reviewHandler = GWT.create(RatingHandler.class);
		GetAllReviewsCallback callback = new GetAllReviewsCallback();

		reviewHandler.getAllReviews(refID, callback);
	}

	/**
	 * Response from server after requesting rating
	 */
	private class GetAllReviewsCallback implements AsyncCallback<ArrayList<String>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("Problem connecting to datastore.");
		}

		@Override
		public void onSuccess(ArrayList<String> result) {
			// result in order: date, user information, review, rating (0 if does not exist)

			commentBlocks = new ArrayList<CommentBlock>();

			for(int i = 0; i<= result.size()-1; i+=4){
				CommentBlock block = new CommentBlock(
						result.get(i),
						result.get(i+1),
						result.get(i+2),
						result.get(i+3));

				commentBlocks.add(block);
				panel.add(block.getBlock(), DockPanel.SOUTH);
			}

		}
	}

	private void enableReviews(){
		submit.setEnabled(true);
		reviewBox.setEnabled(true);
	}

	private void disableReviews(){
		submit.setEnabled(false);
		reviewBox.setEnabled(false);
	}


	private VerticalPanel makeStarRating(){
		score = 0;
		amount = 0;

		//temporary until getStoredRating() gets real ratings
		netRating = new Rating(0, 10);
		netRating.setReadOnly(true);
		myRating = new Rating(0, 10);


		getStoredRatings();
		setClickListener();

		VerticalPanel tempPanel = new VerticalPanel();
		tempPanel.setStyleName("paddedPanel");
		Label netLabel = new Label("Total Rating:");
		tempPanel.add(netLabel);
		tempPanel.add(netRating);

		Label myLabel = new Label("My Rating:");
		tempPanel.add(myLabel);
		tempPanel.add(myRating);

		return tempPanel;
	}


	private void setClickListener(){
		myRating.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {

				RatingHandlerAsync ratingHandler = GWT.create(RatingHandler.class);

				AddRatingCallback callback = new AddRatingCallback();
				ratingHandler.setRating(refID, myRating.getValue(), currentUser, callback);

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
		ratingHandler.getRating(refID, currentUser, callback);
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
			myScore = result.get(2);

			netRating.setValue(score);
			netRating.setTitle("Out of " + String.valueOf(amount) + " ratings.");
			myRating.setValue(myScore);

		}
	}



	private class CommentBlock {

		private String review;
		private String user;
		private String date;
		private Rating rating;


		public CommentBlock(String date, String user, String review, String rating){
			this.review = review;
			this.user = user;

			try{
				this.rating = new Rating(Integer.valueOf(rating), 10);
			}catch(Exception e){
				e.printStackTrace();
				this.rating = new Rating(0, 10);
			}

			this.rating.setReadOnly(true);

			try{
				Date time = new Date(Long.valueOf(date));
				this.date = time.toString();
			}catch(Exception e){
				e.printStackTrace();
				this.date = "";
			}
		}

		public VerticalPanel getBlock(){
			VerticalPanel panel = new VerticalPanel();
			panel.setWidth("1000px");
			panel.add(new HTML("<hr>"
					+ "<font size='4'>" + user + "</font><br>"
					+ "<i>" + date + "</i><br>"));
			panel.add(rating);
			panel.add(new Label(review));


			return panel;

		}
	}
}

