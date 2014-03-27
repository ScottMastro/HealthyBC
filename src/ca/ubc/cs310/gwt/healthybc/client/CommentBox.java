package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentBox {
	
	private TextArea box;
	private Button submit;
	private VerticalPanel panel;
	private boolean initialState;
	private String refID;
	
	private final String DEFAULT_TEXT = "Click here to review Clinic";
	
	public CommentBox(String refID){
		initialState = true;
		this.refID = refID;
		
		box = new TextArea();
		box.addStyleName("commentBoxBefore");
		box.setText(DEFAULT_TEXT);
		
		box.setWidth("300px");
		box.setHeight("150px");		
		
		box.addFocusHandler(new FocusHandler(){

			public void onFocus(FocusEvent event) {
				if (initialState)
					change();				
			}
			
		});
		
		submit = new Button("Submit");
		submit.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				submit();
			}

		});
		
		panel = new VerticalPanel();		
		panel.add(box);
		panel.add(submit);
	}
	
	private void change(){
		box.removeStyleName("commentBoxBefore");
		box.addStyleName("commentBoxAfter");

		box.setText("");
		initialState = false;
	}
	
	public VerticalPanel getBox(){
		return panel;
	}

	private void submit() {
		
		String review = box.getText();
		if(review == null || review.isEmpty() || review.equals(DEFAULT_TEXT)){
			Window.alert("Please enter a review before submitting.");
			return;
		}
		
		if(review.length() >= 500){
			Window.alert("Review should be less than 500 characters.");
			return;
		}
		
		submit.setEnabled(false);
		
		RatingHandlerAsync ratingHandler = GWT.create(RatingHandler.class);

		AddReviewCallback callback = new AddReviewCallback();
		
		ratingHandler.addReview(refID, review, callback);
	}
	
	/**
	 * Response from server after requesting rating
	 */
	private class AddReviewCallback implements AsyncCallback<ArrayList<String>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			submit.setEnabled(true);
			Window.alert("Problem connecting to datastore.");

		}

		@Override
		public void onSuccess(ArrayList<String> result) {
			
			if(result != null && !result.isEmpty())
				Window.alert(result.get(0));


		}
	}
}
