package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FileUpload;

public class CSVClickHandler implements ClickHandler{

	private FileUpload clinicFileUpload;
	
	public CSVClickHandler(FileUpload fu) {
		clinicFileUpload = fu;
	}

	@Override
	public void onClick(ClickEvent event) {

	}

}
