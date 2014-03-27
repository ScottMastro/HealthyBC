package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CommentBox {
	
	private TextArea box;
	private Button submit;
	private VerticalPanel panel;
	private boolean initialState;
	
	public CommentBox(){
		initialState = true;
		
		box = new TextArea();
		box.addStyleName("commentBoxBefore");
		box.setText("Enter review of Clinic");
		
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
		// TODO Auto-generated method stub
		
	}
}
