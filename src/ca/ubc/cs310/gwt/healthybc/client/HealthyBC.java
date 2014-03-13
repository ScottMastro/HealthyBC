package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HealthyBC implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	private LayoutPanel layout;
	private VerticalPanel buttonPanel;
	private MapWidget map;
	private InfoWindow infoWindow;
	private SimplePanel mapContainer;
	private ClinicManagerAsync clinicManager = GWT.create(ClinicManager.class);
	private DockLayoutPanel p;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		init();
	}

	private void init() {
		
		p = new DockLayoutPanel(Unit.PCT);
		p.addNorth(new HTML("<font size='7'><b>The Blank Slate</b> - CPSC 310</font>"), 10);
		
		createUI();
		createButton();
		createTable();
		loadMapApi();
		
		RootLayoutPanel r = RootLayoutPanel.get();
		r.add(p);
		r.forceLayout();
	}


	private void createUI() {
		layout = new LayoutPanel();
		buttonPanel = new VerticalPanel();

		mapContainer = new SimplePanel();
		layout.add(mapContainer);
		layout.setWidgetLeftRight(mapContainer, 50, Unit.PCT, 0, Unit.PCT);
	}

	private void createButton(){

	    p.addSouth(new HTML("<form style='padding:20px;' enctype='multipart/form-data' "
	    		+ "method='POST' action='/uploadServlet'/>"
	    		+ "Add new data : <input name='userfile1' type='file' />"
	    		+ "<input type='submit' value='Submit' /> </form>"), 10);
	}

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
			
			p.addWest(panel,35);
		}
	}

	private void createTable() {
		//TODO: get real parsed data in here; the following uses mock objects
		ClinicDataParserAsync clinicParser = GWT.create(ClinicDataParser.class);

		TableInfoListCallback callback = new TableInfoListCallback();
		clinicParser.MocktableInfo(callback);
	}
	
	private class MapInfoListCallback implements AsyncCallback<ArrayList<MapInfo>> {
		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}
		
		@Override
		public void onSuccess(ArrayList<MapInfo> result) {
			// Vancouver center coordinates
			LatLng vanCity = LatLng.newInstance(49.2569425,-123.123904);

			InfoWindowOptions iwOptions = InfoWindowOptions.newInstance();
			infoWindow = InfoWindow.newInstance(iwOptions);

			MapOptions options = MapOptions.newInstance();
			options.setZoom(13);
			options.setCenter(vanCity);

			map = new MapWidget(options);
			map.setSize("100%", "100%");

			displayClinics(map, result);

			mapContainer.add(map);
			p.addEast(mapContainer, 65);
			map.triggerResize();
		}
	}
	
	private void createMap() {
		ClinicDataParserAsync clinicParser = GWT.create(ClinicDataParser.class);
		
		MapInfoListCallback callback = new MapInfoListCallback();
		clinicParser.MockMapInfo(callback);
	}

	private void displayClinics(final MapWidget map, ArrayList<MapInfo> clinics) {
		for (MapInfo clinic : clinics) {
			MarkerOptions options = MarkerOptions.newInstance();
			options.setMap(map);
			options.setClickable(true);
			options.setTitle(clinic.getName());
			options.setPosition(LatLng.newInstance(clinic.getLatitude(), clinic.getLongitude()));

			final Marker marker = Marker.newInstance(options);
			final String desc = clinic.getName();

			ClickMapHandler handler = new ClickMapHandler() {
				public void onEvent(ClickMapEvent e) {
					System.out.println(desc);
					infoWindow.setContent("<div class=\"markerContent\" style=\"line-height:normal; white-space:nowrap;\">" + desc + "</div>");
					infoWindow.open(map, marker);
				} 
			};
			marker.addClickHandler(handler);
		}
	}

	private void loadMapApi() {
		boolean sensor = false;

		// Load libraries needed for maps
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
		//	    loadLibraries.add(LoadLibrary.ADSENSE);
		loadLibraries.add(LoadLibrary.DRAWING);		// need this api to draw overlays on the map
		//	    loadLibraries.add(LoadLibrary.GEOMETRY);
		//	    loadLibraries.add(LoadLibrary.PANORAMIO);
		//	    loadLibraries.add(LoadLibrary.PLACES);
		//	    loadLibraries.add(LoadLibrary.WEATHER);
		//	    loadLibraries.add(LoadLibrary.VISUALIZATION);

		Runnable onLoad = new Runnable() {
			@Override
			public void run() {
				// callback on successful api load
				createMap();
			}
		};

		LoadApi.go(onLoad, loadLibraries, sensor);
	}
}
