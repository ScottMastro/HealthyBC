package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HealthyBC implements EntryPoint {

	public static HealthyBC singleton;
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Private variables
	 */
	private static final long DURATION = 1000 * 60 * 60 * 24;
	private DockLayoutPanel dock;
	private DockLayoutPanel mapTableDock;
	private TabLayoutPanel tabs;
	private ArrayList<String> tabNames;
	private boolean showAdminTools = false;
	private String currentUser = "";
	private Double addressLat;	
	private Double addressLon;
	private MapBuilder mapBuilder;
	private static Widget twitterFeedPanel;
	private SimplePanel historyPanel;
	private Widget historyTable;

	/**
	 * Returns singleton object HealthyBC
	 * @return HealthyBC Returns static object
	 */
	public static HealthyBC get() {
		return singleton;
	}
	
	public String getCurrentUser() {
		return currentUser;
	}
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		singleton = this;
		
		String username = Cookies.getCookie("HBC_username");

		if (username != null && username != ""){
			currentUser = username.trim();
			if (currentUser.equalsIgnoreCase("admin")) {
				showAdminTools = true;
			}
			else {
				showAdminTools = false;
			}
			init();
		} else {
			login();
		}
	}

	/**
	 * Authorizes the user
	 */
	private void login(){
		
		RootPanel.get().clear();
		
		VerticalPanel rtpanel = new VerticalPanel();

		rtpanel.add(new HTML("<h2> Healthy BC : Walk-in Clinics </h2> <br/>"));

		HorizontalPanel hpanel = new HorizontalPanel();
		hpanel.setHeight("520px");

		// Create Login form
		hpanel.add(createLoginForm());

		// Create registration form
		hpanel.add(createRegisterForm()); 

		rtpanel.add(hpanel);
		rtpanel.add(new HTML("Created by: The Blank Slate Team (CPSC 310)"));

		RootPanel.get().add(rtpanel, 20, 20);    

	}

	/**
	 * Create a login form
	 */
	private final FormPanel createLoginForm(){

		final FormPanel form = FormBuilder.createLoginForm();

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			/* Form submission handler */
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				String res = event.getResults();
				if (res.trim().startsWith("success")){
					currentUser = event.getResults().split(":")[1].trim();
					Cookies.setCookie("HBC_username", currentUser);
					showAdminTools = false;
					init();
				} else if (res.trim().startsWith("admin")){
					currentUser = event.getResults().split(":")[1].trim();
					Cookies.setCookie("HBC_username", currentUser);
					showAdminTools = true;
					init();
				} else {
					Window.alert("Error: Login failed - Invalid username or password.");
				}
				
			}
		});

		return form;
	}

	/**
	 * Create Registration form
	 */
	private final FormPanel createRegisterForm(){

		final FormPanel form = FormBuilder.createRegisterForm();

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			/* Form submission handler */
			public void onSubmitComplete(SubmitCompleteEvent event) {
				
				if (event.getResults().trim().startsWith("success")){
					currentUser = event.getResults().split(":")[1].trim();
					Cookies.setCookie("HBC_username", currentUser);
					showAdminTools = false;
					init();
				} else {
					Window.alert("Error: Could not create user. Username may be taken or invalid.");
				}
				
			}
		});

		return form;
	}

	/**
	 * Sets up the interface for the main page.
	 */
	private void init() {
		RootPanel.get().clear();
		
		dock = new DockLayoutPanel(Unit.PCT);
		mapTableDock = new DockLayoutPanel(Unit.PCT);

		tabs = new TabLayoutPanel(2.5, Unit.EM);
		tabNames = new ArrayList<String>();

		twitterFeedPanel = TwitterFeed.getInstance().createTwitterFeed();
		historyPanel = new SimplePanel();
		
		// add home page & logout button
		OptionsTab options = new OptionsTab(this);
		
		Image homeImage = new Image();
	    homeImage.setUrl(GWT.getModuleBaseURL()+"images/healthyBC.png");

		tabs.add(homeImage, "Home");
		tabs.add(options.getOptionsTab(), "Options");
		tabs.add(twitterFeedPanel, "Twitter");
		tabs.add(historyPanel, "History");

		tabNames.add("Home");
		tabNames.add("Options");
		tabNames.add("Twitter");
		tabNames.add("History");

		createMap(null, null);
		createTable(null, null);
		createUploadForm();

		populateHistory();
		
		dock.addWest(mapTableDock, 30);
		dock.addEast(tabs, 70);

		RootLayoutPanel r = RootLayoutPanel.get();
		r.add(dock);
		r.forceLayout();
	}

	public void search(String searchBy, String searchKey){
		mapTableDock.clear();
		createTable(searchBy, searchKey);
		createMap(searchBy, searchKey);
	}

	public void setAddress(Double lat, Double lon){
		addressLat = lat;
		addressLon = lon;
		mapBuilder.setAddress(lat, lon);
	}

	// --------------------------------------------------------------
	// Create Map
	// --------------------------------------------------------------

	/**
	 * Calls required methods to create the Google map interface
	 */
	private void createMap(String searchBy, String searchKey) {
		loadMapAPI(searchBy, searchKey);
	}

	/**
	 * Initiates Google Map API
	 */
	private void loadMapAPI(final String searchBy, final String searchKey) {
		boolean sensor = false;

		// Load libraries needed for maps
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();

		// need this API to draw overlays on the map
		loadLibraries.add(LoadLibrary.DRAWING);

		Runnable onLoad = new Runnable() {
			@Override
			public void run() {
				// callback on successful API load
				buildMap(searchBy, searchKey);
			}
		};

		LoadApi.go(onLoad, loadLibraries, sensor);
	}

	/**
	 * On successful API load, retrieves data and constructs map
	 */
	private void buildMap(String searchBy, String searchKey) {
		ClinicDataFetcherAsync clinicFetcher = GWT.create(ClinicDataFetcher.class);

		boolean hasAddress = (addressLat != null && addressLon != null);
		
		mapBuilder = new MapBuilder(this, hasAddress, addressLat, addressLon);
		clinicFetcher.mapInfo(searchBy, searchKey, mapBuilder);
	}

	/**
	 * Called when map is ready to be displayed from MapBuilder
	 * @param mapContainer is the map to display
	 */
	public void addMap(SimplePanel mapContainer){
		mapTableDock.addSouth(mapContainer, 50);
	}


	// --------------------------------------------------------------
	// Create Table
	// --------------------------------------------------------------

	/**
	 * Calls required methods to create the table
	 */
	private void createTable(String searchBy, String searchKey) {
		buildTable(searchBy, searchKey);
	}

	/**
	 * Retrieves data and constructs the table
	 */
	private void buildTable(String searchBy, String searchKey) {
		ClinicDataFetcherAsync tableFetcher = GWT.create(ClinicDataFetcher.class);

		TableInfoListCallback callback = new TableInfoListCallback();
		tableFetcher.tableInfo(searchBy, searchKey, callback);
	}

	/**
	 * Calls the server to retrieve clinic data
	 */
	private class TableInfoListCallback implements AsyncCallback<ArrayList<TableInfo>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}

		@Override
		public void onSuccess(ArrayList<TableInfo> result) {
			CellTable<TableInfo> table = new TableBuilder().buildTable(result);

			final SingleSelectionModel<TableInfo> ssm = new SingleSelectionModel<TableInfo>();
			table.setSelectionModel(ssm);

			ssm.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				@Override
				public void onSelectionChange(final SelectionChangeEvent event)
				{

					final TableInfo selected = ssm.getSelectedObject();
					getTabFromTableInfo(selected);
				}
			});

			ScrollPanel panel = new ScrollPanel(table);
			panel.setAlwaysShowScrollBars(true);

			mapTableDock.addNorth(panel,50);
		}
	}

	// --------------------------------------------------------------
	// Create Footer (Upload form and Credits)
	// --------------------------------------------------------------

	/**
	 * Create footer and add form to parse remote data
	 */
	private void createUploadForm(){
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSize("100%", "100%");
		hp.getElement().setAttribute("cellpadding", "10");
		if (showAdminTools == true ){
			HTML uploadForm = new HTML("<form method='POST' action='/uploadURL'/>"
					+ "Upload data from URL: <input name='urlstring' type='text' /> &nbsp;"
					+ "<input type='submit' value='Submit' /> </form> <br />"
					+ "<form enctype='multipart/form-data' "
					+ "method='POST' action='/uploadServlet'/>"
					+ "Upload CSV data : <input name='userfile1' type='file' />"
					+ "<input type='submit' value='Submit' /> </form>");
			hp.add(uploadForm);
			hp.setCellHorizontalAlignment(uploadForm, HasHorizontalAlignment.ALIGN_LEFT);

		} else {
			HTML footer = new HTML("<b>Team Members:</b> Alex Tan, Ben Liang, Dhananjay Bhaskar and Scott Mastromatteo <br/>"
					+ "<br/> <b>Contact Us:</b> <a href='mailto:theblanksl8@gmail.com'>theblanksl8 AT gmail DOT com</a> &nbsp;"
					+ "&copy; 2014 The Blank Slate Team<br />");
			hp.add(footer);
			hp.setCellHorizontalAlignment(footer, HasHorizontalAlignment.ALIGN_LEFT);
		}

		Button logoutButton = new Button("Logout", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Cookies.removeCookie("HBC_username");
				currentUser = "";
				login();
			}
		});
		hp.add(logoutButton);
		hp.setCellHorizontalAlignment(logoutButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		dock.addSouth(hp, 15);
	}


	// --------------------------------------------------------------
	// Create Tabs
	// --------------------------------------------------------------

	private boolean removeTab(String name){

		for (int i = 0; i <= tabNames.size() -1; i++){
			if (tabNames.get(i).equals(name)){

				tabs.remove(i);
				tabNames.remove(i);
				return true;
			}
		}

		return false;
	}

	private int findTab(String name){

		for (int i = 0; i <= tabNames.size() -1; i++){
			if (tabNames.get(i).equals(name))
				return i;
		}

		return 0;
	}

	private void getTabFromTableInfo(TableInfo ti){
		TabFetcherAsync tabFetcher = GWT.create(TabFetcher.class);

		ClinicTabCallback callback = new ClinicTabCallback();
		tabFetcher.clinicTabInfo(ti, callback);
	}

	public void getTabFromMapInfo(MapInfo mi){
		TabFetcherAsync tabFetcher = GWT.create(TabFetcher.class);

		ClinicTabCallback callback = new ClinicTabCallback();
		tabFetcher.clinicTabInfo(mi, callback);
	}

	/**
	 * Calls the server to retrieve data
	 */
	private class ClinicTabCallback implements AsyncCallback<ArrayList<ClinicTabInfo>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();

		}

		@Override
		public void onSuccess(ArrayList<ClinicTabInfo> result) {
			if(result == null){
				Window.alert("Could not find information about this clinic");                          
			}
			else{

				VerticalPanel container = new VerticalPanel();

				ClinicTabInfo t = result.get(0);
				removeTab("Clinic Information");
				DockLayoutPanel clinicPanel = new DockLayoutPanel(Unit.PCT);

				boolean hasEmail = !(t.getEmail() == null) && !t.getEmail().isEmpty();
				String emailString = "";
				String distanceString = "";

				if(hasEmail)
					emailString = "<LI><b>Email: </b><br>" + t.getEmail();

				if(addressLat != null && addressLon != null)
					distanceString = "<center><font size='6'>Distance is about " +
							t.getDistance(addressLat, addressLon) + " km away</font></center>";

				HTML info = new HTML("<br>"
						+ "<h1 style='margin:0px'>" + t.getName() + "</h1>"
						+ "<font size='4'>"
						+ "<center>" + t.getAddress() + " " + t.getPostalCode() + "</center><br>"
						+ "<UL>"
						+ "<LI><b>Hours: </b>" + t.getHours() + "<br><br>"
						+ "<LI><b>Available Languages: </b>" + t.getLanguages() 
						+ "<LI><b>Phone: </b>" + t.getPhone() + "<br>"
						+ emailString
						+ "</UL></font>"
						+ distanceString
						);

				HorizontalPanel socialContainer = new HorizontalPanel();

				String sURL = "http://healthy-bc-310.appspot.com/?clinicID=" + t.getRefID();
				HTML gHTML = new HTML("<g:plusone><div class=\"g-plusone\" data-annotation=\"inline\" data-width=\"50\" data-href=\" style=\"padding-bottom:15px !important;\""
						+ sURL
						+ "\"></div></g:plusone>");
				HTML fbHTML = new HTML("<div id=\"fb-wrapper\"><fb:like href=\""
						+ sURL
						+ "\" layout=\"standard\" action=\"like\" show_faces=\"false\" share=\"false\"></fb:like></div>");

				socialContainer.add(gHTML);
				socialContainer.add(fbHTML);

				container.add(info);
				container.add(socialContainer);

				clinicPanel.addWest(container, 60);

				if(hasEmail){
					VerticalPanel emailPanel = new VerticalPanel();

					EmailBox email = new EmailBox(t.getEmail());
					emailPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

					emailPanel.add(new HTML("<h2 style='color:gray'>Compose email to clinic</h2>"));
					emailPanel.add(email.getBox());				
					clinicPanel.addEast(emailPanel, 40);
				}

				tabs.add(clinicPanel, "Clinic Information");

				tabNames.add(tabs.getWidgetCount() -1, "Clinic Information");
				tabs.selectTab(findTab("Clinic Information"));

				removeTab("View Ratings");

				RatingTab ratingTab = new RatingTab(t.getRefID(), currentUser);

				tabs.add(ratingTab.getRatingTab(), "View Ratings");
				tabNames.add(tabs.getWidgetCount() -1, "View Ratings");

				saveClinicVisit(t.getRefID(), currentUser, t.getName(), t.getAddress());
				
				forceRender();
			}
		}
	}

	/**
	 * Save a clicked clinic to the data store for viewing history purposes
	 */
	private void saveClinicVisit(String refID, String currentUser, final String clinicName, final String clinicAddress) {
		// Try and save the clinic to user history
		HistoryHandlerAsync historyHandler = GWT.create(HistoryHandler.class);

		AsyncCallback<String> callback = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				// Couldn't save our visit to the datastore
			}

			@Override
			public void onSuccess(String dateString) {
				// Successfully saved to datastore
				// Returns dateString on successful storage of visit
				
				HistoryTab.getInstance().addClinicToTable(clinicName, clinicAddress, dateString);
			}
		};
		historyHandler.saveClinicVisit(refID, currentUser, clinicName, clinicAddress, callback);
	}

	/**
	 * Gets and adds current users clinic viewing history
	 */
	private void populateHistory() {
		HistoryHandlerAsync historyHandler = GWT.create(HistoryHandler.class);
		
		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				historyTable = HistoryTab.getInstance().buildTable(result);
				historyPanel.add(historyTable);
			}
		};
		
		historyHandler.getHistory(currentUser, callback);
	}
	
	/**
	 * JSNI method to force re-rendering of Like/+1 buttons
	 */
	public static native String forceRender() /*-{
		$wnd.FB.XFBML.parse($doc.getElementById("fb-wrapper"));
		$wnd.gapi.plusone.go();
	}-*/;
}
