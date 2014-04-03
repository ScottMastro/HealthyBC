package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class FormBuilder {

	/**
	 * Create a login form
	 */
	public static final FormPanel createLoginForm(){
		final FormPanel form = new FormPanel();
		form.setAction("/login");

		form.setMethod(FormPanel.METHOD_POST);

		KeyDownHandler formKeypressCallback = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				boolean isEnter = KeyCodes.KEY_ENTER == event.getNativeKeyCode();
				if (isEnter)
					form.submit();
			}
		};

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panel.setSize("400px", "400px");

		panel.add(new HTML("<h2> Login </h2><br/>"));

		form.setWidget(panel);

		panel.add(new HTML("Username : "));

		// Username field
		final TextBox tb = new TextBox();
		tb.setName("username");
		tb.addKeyDownHandler(formKeypressCallback);
		panel.add(tb);

		panel.add(new HTML("<br/> Password : "));

		// Password field
		final PasswordTextBox pb = new PasswordTextBox();
		pb.getElement().setPropertyString("placeholder", "Password");
		pb.setName("password");
		pb.addKeyDownHandler(formKeypressCallback);
		panel.add(pb);

		panel.add(new HTML("<br/> <br/>"));

		// Add a 'submit' button.
		panel.add(new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		// Error handling and verification
		form.addSubmitHandler(new FormPanel.SubmitHandler() {

			public void onSubmit(SubmitEvent event) {
				if (tb.getText().length() == 0 || pb.getText().length() == 0) {
					Window.alert("The username and password fields must not be empty!");
					event.cancel();
				}
			}	
		});

		return form;
	}


	/**
	 * Create Registration form
	 */
	public static final FormPanel createRegisterForm(){

		final FormPanel form = new FormPanel();
		form.setAction("/register");

		form.setMethod(FormPanel.METHOD_POST);
		
		KeyDownHandler formKeypressCallback = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				boolean isEnter = KeyCodes.KEY_ENTER == event.getNativeKeyCode();
				if (isEnter)
					form.submit();
			}
		};

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panel.setSize("400px", "500px");

		panel.add(new HTML("<h2> Register </h2><br/>"));

		form.setWidget(panel);

		panel.add(new HTML("Name : "));

		// Name field
		final TextBox name = new TextBox();
		name.setName("name");
		name.addKeyDownHandler(formKeypressCallback);
		panel.add(name);

		panel.add(new HTML("<br/> Email : "));

		// EMail field
		final TextBox email = new TextBox();
		email.setName("email");
		email.addKeyDownHandler(formKeypressCallback);
		panel.add(email);

		panel.add(new HTML("<br/> Username : "));

		// Username field
		final TextBox tb = new TextBox();
		tb.setName("username");
		tb.addKeyDownHandler(formKeypressCallback);
		panel.add(tb);

		panel.add(new HTML("<br/> Password : "));

		// Password field
		final PasswordTextBox pb = new PasswordTextBox();
		pb.setName("password");
		pb.addKeyDownHandler(formKeypressCallback);
		panel.add(pb);

		panel.add(new HTML("<br/> <br/>"));

		// Add a 'submit' button.
		panel.add(new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				form.submit();
			}
		}));

		// Perform error checking and validation
		form.addSubmitHandler(new FormPanel.SubmitHandler() {

			public void onSubmit(SubmitEvent event) {
				if (name.getText().length() == 0 || email.getText().length() == 0 || tb.getText().length() == 0 || pb.getText().length() == 0) {
					Window.alert("Fields cannot be empty.");
					event.cancel();
				}
			}
		});

		return form;
	}
	
	
}
