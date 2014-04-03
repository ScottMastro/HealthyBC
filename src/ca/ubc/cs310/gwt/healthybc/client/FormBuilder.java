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
		form.setStyleName("leftForm");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panel.setWidth("220px");

		panel.add(new HTML("<h3> Login </h3>"));

		form.setWidget(panel);

		// Username field
		final TextBox tb = new TextBox();
		tb.getElement().setPropertyString("placeholder", "Username");
		tb.setName("username");
		tb.addKeyDownHandler(formKeypressCallback);
		panel.add(tb);

		// Password field
		final PasswordTextBox pb = new PasswordTextBox();
		pb.getElement().setPropertyString("placeholder", "Password");
		pb.setName("password");
		pb.addKeyDownHandler(formKeypressCallback);
		panel.add(pb);

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
		panel.setStyleName("rightForm");
		panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		panel.setWidth("200px");

		panel.add(new HTML("<h3> Register</h3>"));

		form.setWidget(panel);

		// Name field
		final TextBox name = new TextBox();
		name.getElement().setPropertyString("placeholder", "Name");
		name.setName("name");
		name.addKeyDownHandler(formKeypressCallback);
		panel.add(name);

		// EMail field
		final TextBox email = new TextBox();
		email.getElement().setPropertyString("placeholder", "Email");
		email.setName("email");
		email.addKeyDownHandler(formKeypressCallback);
		panel.add(email);

		// Username field
		final TextBox tb = new TextBox();
		tb.getElement().setPropertyString("placeholder", "Username");
		tb.setName("username");
		tb.addKeyDownHandler(formKeypressCallback);
		panel.add(tb);

		// Password field
		final PasswordTextBox pb = new PasswordTextBox();
		pb.getElement().setPropertyString("placeholder", "Password");
		pb.setName("password");
		pb.addKeyDownHandler(formKeypressCallback);
		panel.add(pb);

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
