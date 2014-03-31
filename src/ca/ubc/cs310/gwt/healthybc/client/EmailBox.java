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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EmailBox {

	private TextArea box;
	private TextBox title;
	private Button send;
	private VerticalPanel panel;
	private boolean boxInitialState;
	private boolean titleInitialState;
	private String email;

	private final String DEFAULT_BOX_TEXT = "Click here to write an email to send to the clinic\n\n"
			+ "Be sure to include your own contact information so the clinic can get back to you.";
	private final String DEFAULT_TITLE_TEXT = "Add email subject here";

	public EmailBox(String clinicEmail){
		boxInitialState = true;
		titleInitialState = true;

		this.email = clinicEmail;

		box = new TextArea();
		box.addStyleName("boxBefore");
		box.setText(DEFAULT_BOX_TEXT);

		box.setWidth("300px");
		box.setHeight("150px");		

		box.addFocusHandler(new FocusHandler(){

			public void onFocus(FocusEvent event) {
				if (boxInitialState)
					changeBox();				
			}

		});

		send = new Button("Send");
		send.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				send();
			}

		});

		title = new TextBox();
		title.setText(DEFAULT_TITLE_TEXT);
		title.setStyleName("boxBefore");
		title.setWidth("300px");

		title.addFocusHandler(new FocusHandler(){

			public void onFocus(FocusEvent event) {
				if (titleInitialState)
					changeTitle();				
			}

		});

		panel = new VerticalPanel();
		panel.setStyleName("emailBoxPadding");
		panel.add(title);
		panel.add(box);
		panel.add(send);
	}

	protected void changeTitle() {
		title.removeStyleName("boxBefore");
		title.addStyleName("boxAfter");

		title.setText("");
		titleInitialState = false;
	}

	private void changeBox(){
		box.removeStyleName("boxBefore");
		box.addStyleName("boxAfter");

		box.setText("");
		boxInitialState = false;
	}

	public VerticalPanel getBox(){
		return panel;
	}

	private void send() {

		String body = box.getText();
		if(body == null || body.isEmpty() || body.equals(DEFAULT_BOX_TEXT)){
			Window.alert("Please write an email before sending.");		
			return;
		}

		String subject = title.getText();
		if(subject == null || subject.isEmpty() || subject.equals(DEFAULT_TITLE_TEXT)){
			Window.alert("Please write a subject before sending.");		
			return;
		}

		disable();
		
		TabFetcherAsync emailHandler = GWT.create(TabFetcher.class);
		EmailCallback callback = new EmailCallback();

		emailHandler.sendEmail(body, email, subject, callback);
	}

	/**
	 * Response from server after requesting rating
	 */
	private class EmailCallback implements AsyncCallback<ArrayList<Boolean>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			enable();
			Window.alert("Problem connecting.");

		}

		@Override
		public void onSuccess(ArrayList<Boolean> result) {

			if(result != null && result.get(0))
				Window.alert("Email sent.");
			else if(!result.get(0)){
				Window.alert("Failed to send email.");
				enable();
			}

		}
	}

	private void enable(){
		send.setEnabled(true);		
		box.setEnabled(true);
		title.setEnabled(true);
	}

	public void disable(){
		send.setEnabled(false);
		box.setEnabled(false);
		title.setEnabled(false);
	}
}