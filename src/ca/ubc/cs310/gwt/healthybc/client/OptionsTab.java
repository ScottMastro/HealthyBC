package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;



public class OptionsTab {

	private TextBox clinicName;
	private ListBox clinicLanguage;
	private TextBox addressBox;
	private Button nameSearch;
	private Button languageSearch;
	private Button addressButton;
	private HealthyBC main;

	private String[] languages = {"English", 
			//"Afghani",
			"Afrikaans", "Arabic",
			//"Bengali",
			"Bicol", "Cantonese", "Farsi", "Filipino",	"French", "German", "Greek", "Hindi",
			"Japanese", "Korean", "Mandarin", "Polish", "Punjabi", "Russian",
			//"Serbian",
			"Spanish", "Tagalog", "Urdu",
			//"Vietnamese"
	};

	public OptionsTab(HealthyBC main){
		this.main = main;

		clinicName = new TextBox();
		clinicLanguage = new ListBox();
		addressBox = new TextBox();

		clinicLanguage.setWidth("150px");
		clinicLanguage.setHeight("32px");

		addressBox.setWidth("300px");

		nameSearch = new Button();
		nameSearch.setText("Search");
		nameSearch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				searchName();
			}		
		});

		languageSearch = new Button();
		languageSearch.setText("Search");
		languageSearch.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				searchLanguage();
			}		
		});

		for(int i = 0; i <= languages.length - 1; i++)
			clinicLanguage.addItem(languages[i]);

		addressButton = new Button();
		addressButton.setText("Place on map");
		addressButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				placeAddressOnMap();
			}
		});

	}

	public VerticalPanel getOptionsTab(){
		VerticalPanel panel = new VerticalPanel();
		DockPanel searchPanel = new DockPanel();

		VerticalPanel labels = new VerticalPanel();
		labels.getElement().setAttribute("cellpadding", "10");

		Label n = new Label("Search by name:");
		Label l = new Label("Search by language:");

		labels.add(n);
		labels.add(l);

		searchPanel.add(labels, DockPanel.WEST);

		VerticalPanel keys = new VerticalPanel();
		keys.getElement().setAttribute("cellpadding", "5");

		keys.add(clinicName);
		keys.add(clinicLanguage);

		searchPanel.add(keys, DockPanel.WEST);

		VerticalPanel buttons = new VerticalPanel();
		buttons.getElement().setAttribute("cellpadding", "5");

		buttons.add(nameSearch);
		buttons.add(languageSearch);

		searchPanel.add(buttons, DockPanel.WEST);

		HorizontalPanel address = new HorizontalPanel();
		address.getElement().setAttribute("cellpadding", "5");

		Label a = new Label("Add your address:");

		address.add(a);
		address.add(addressBox);
		address.add(addressButton);

		panel.add(searchPanel);
		panel.add(new HTML("<hr>"));
		panel.add(address);


		return panel;
	}


	private void searchName() {
		main.search("name", clinicName.getText());
	}	


	private void searchLanguage() {
		main.search("language", clinicLanguage.getItemText(clinicLanguage.getSelectedIndex()));
	}


	private void placeAddressOnMap() {
		geoCode();
	}


	private void geoCode(){

		TabFetcherAsync geoCoder = GWT.create(TabFetcher.class);
		GeoCodeCallback callback = new GeoCodeCallback();

		geoCoder.geoCode(addressBox.getText(), callback);
	}

	/**
	 * Response from server after requesting rating
	 */
	private class GeoCodeCallback implements AsyncCallback<ArrayList<Double>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("Could not recoginize given address.");
		}

		@Override
		public void onSuccess(ArrayList<Double> result) {
			if(result!=null && result.size() == 2)
				main.setAddress(result.get(0), result.get(1));
		}

	}
}







