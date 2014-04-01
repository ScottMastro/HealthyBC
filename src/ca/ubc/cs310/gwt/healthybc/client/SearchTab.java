package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchTab {

	private TextBox clinicName;
	private ListBox clinicLanguage;
	private Button nameSearch;
	private Button languageSearch;
	private HealthyBC main;

	private String[] languages = {"English", "Afghani", "Afrikaans", "Arabic",	"Bengali", "Bicol",	"Cantonese",
			"Farsi", "Filipino",	"French", "German", "Greek", "Hindi", "Japanese", "Korean", "Mandarin",
			"Polish", "Punjabi", "Russian", "Serbian", "Spanish", "Tagalog", "Urdu", "Vietnamese"};

	public SearchTab(HealthyBC main){
		this.main = main;
		clinicName = new TextBox();
		clinicLanguage = new ListBox();
		
		clinicLanguage.setWidth("150px");
		clinicLanguage.setHeight("32px");
		


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

	}

	public DockPanel getSearchTab(){
		DockPanel panel = new DockPanel();
		
		VerticalPanel labels = new VerticalPanel();
		labels.getElement().setAttribute("cellpadding", "10");

		Label n = new Label("Search by name:");
		Label l = new Label("Search by language:");
		
		labels.add(n);
		labels.add(l);
		
		panel.add(labels, DockPanel.WEST);
		
		VerticalPanel keys = new VerticalPanel();
		keys.getElement().setAttribute("cellpadding", "5");

		keys.add(clinicName);
		keys.add(clinicLanguage);

		panel.add(keys, DockPanel.WEST);

		VerticalPanel buttons = new VerticalPanel();
		buttons.getElement().setAttribute("cellpadding", "5");
		
		buttons.add(nameSearch);
		buttons.add(languageSearch);

		panel.add(buttons, DockPanel.WEST);

		/*
		
		HorizontalPanel name = new HorizontalPanel();
		name.getElement().setAttribute("cellpadding", "5");
		Label n = new Label("Search by name:");
		name.setCellVerticalAlignment(n, HasVerticalAlignment.ALIGN_MIDDLE);

		
		name.add(n);
		name.add(clinicName);
		name.add(nameSearch);
		
		HorizontalPanel language = new HorizontalPanel();
		language.getElement().setAttribute("cellpadding", "5");
		Label l = new Label("Search by language:");
		l.getElement().setAttribute("font-size", "24pt");
		language.setCellVerticalAlignment(l, HasVerticalAlignment.ALIGN_MIDDLE);


		
		language.add(l);
		language.add(clinicLanguage);
		language.add(languageSearch);
		
		VerticalPanel panel = new VerticalPanel();

		panel.add(name);
		panel.add(new HTML("<hr>"));
		panel.add(language);

*/
		return panel;
	}


	private void searchName() {
		main.search("name", clinicName.getText());
	}	


	protected void searchLanguage() {
		main.search("language", clinicLanguage.getItemText(clinicLanguage.getSelectedIndex()));
	}

}
