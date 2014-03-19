package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HealthyBC implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	@SuppressWarnings("unused")
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";


	private DockLayoutPanel dock;
	private DockLayoutPanel mapTableDock;
	private TabLayoutPanel tabs;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		init();
	}

	/**
	 * Sets up the interface for the main page.
	 */
	private void init() {

		dock = new DockLayoutPanel(Unit.PCT);
		mapTableDock = new DockLayoutPanel(Unit.PCT);
		//add title
		dock.addNorth(new HTML("<font size='7'><b>The Blank Slate</b> - CPSC 310</font>"), 10);

		tabs = new TabLayoutPanel(2.5, Unit.EM);
		tabs.add(new HTML("this content"), "this");
		tabs.add(new HTML("that content"), "that");
		tabs.add(new HTML("the other content"), "the other");
		
		createMap();
		createTable();
		createUploadForm();

		dock.addWest(mapTableDock, 35);
		dock.addEast(tabs, 65);
		
		RootLayoutPanel r = RootLayoutPanel.get();
		r.add(dock);
		r.forceLayout();
	}

	// --------------------------------------------------------------
	// Create Map
	// --------------------------------------------------------------

	/**
	 * Calls required methods to create the Google map interface
	 */
	private void createMap() {
		loadMapAPI();
	}

	/**
	 * Initiates Google Map API
	 */
	private void loadMapAPI() {
		boolean sensor = false;

		// Load libraries needed for maps
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();

		// need this API to draw overlays on the map
		loadLibraries.add(LoadLibrary.DRAWING);
		//	    loadLibraries.add(LoadLibrary.ADSENSE);
		//	    loadLibraries.add(LoadLibrary.GEOMETRY);
		//	    loadLibraries.add(LoadLibrary.PANORAMIO);
		//	    loadLibraries.add(LoadLibrary.PLACES);
		//	    loadLibraries.add(LoadLibrary.WEATHER);
		//	    loadLibraries.add(LoadLibrary.VISUALIZATION);

		Runnable onLoad = new Runnable() {
			@Override
			public void run() {
				// callback on successful API load
				buildMap();
			}
		};

		LoadApi.go(onLoad, loadLibraries, sensor);
	}

	/**
	 * On successful API load, retrieves data and constructs map
	 */
	private void buildMap() {
		ClinicDataFetcherAsync clinicParser = GWT.create(ClinicDataFetcher.class);

		MapBuilder callback = new MapBuilder(this);
		clinicParser.mapInfo(callback);
	}

	/**
	 * Called when map is ready to be displayed from MapBuilder
	 * @param mapContainer is the map to display
	 */
	public void addMap(SimplePanel mapContainer){
		mapTableDock.addNorth(mapContainer, 50);
	}


	// --------------------------------------------------------------
	// Create Table
	// --------------------------------------------------------------

	/**
	 * Calls required methods to create the table
	 */
	private void createTable() {
		buildTable();
	}

	/**
	 * Retrieves data and constructs the table
	 */
	private void buildTable() {
		ClinicDataFetcherAsync clinicParser = GWT.create(ClinicDataFetcher.class);

		TableInfoListCallback callback = new TableInfoListCallback();
		clinicParser.tableInfo(callback);
	}

	/**
	 * Calls the server to retrieve data
	 */
	private class TableInfoListCallback implements AsyncCallback<ArrayList<TableInfo>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}

		@Override
		public void onSuccess(ArrayList<TableInfo> result) {
			CellTable<TableInfo> table = new TableBuilder().buildTable(result);
			ScrollPanel panel = new ScrollPanel(table);
			panel.setAlwaysShowScrollBars(true);

			mapTableDock.addSouth(panel,50);
		}
	}

	// --------------------------------------------------------------
	// Create Upload Form
	// --------------------------------------------------------------

	/**
	 * Sets up form to browse for and send local file to servlet
	 */
	private void createUploadForm(){

		dock.addSouth(new HTML("<form style='padding:20px;' enctype='multipart/form-data' "
				+ "method='POST' action='/uploadServlet'/>"
				+ "Add new data : <input name='userfile1' type='file' />"
				+ "<input type='submit' value='Submit' /> </form>"), 10);
	}
}
