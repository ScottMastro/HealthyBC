package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

<<<<<<< HEAD
import org.apache.http.conn.routing.RouteInfo.LayerType;

=======
import ca.ubc.cs310.gwt.healthybc.server.Location;
>>>>>>> maps
import ca.ubc.cs310.gwt.healthybc.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
<<<<<<< HEAD
import com.google.gwt.user.cellview.client.CellTable;
=======
import com.google.gwt.maps.client.events.MapEvent;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.Event;
>>>>>>> maps
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HealthyBC implements EntryPoint {
	
	private MapWidget map;
	
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
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		init();
	}
	
	private void init() {
		createUI();
		createTable();
		loadMapApi();

		RootLayoutPanel.get().forceLayout();
	}
	
	private void createUI() {
		layout = new LayoutPanel();
		RootLayoutPanel.get().add(layout);
	}
	
	private void createTable() {
		TableBuilder builder = new TableBuilder();
		//TODO: get real parsed data in here; the following uses mock objects
		TableInfo tabInfo1 = new TableInfo("Blah Clinic", "123 McKee Place", "blah@blahclinic.ca");
		TableInfo tabInfo2 = new TableInfo("Random Clinic", "234 Random Street", "rand@randomclinic.ca");
		TableInfo tabInfo3 = new TableInfo("Superman Clinic", "434 Superman Street", "supes@supermanclinic.ca");
		
		CellTable<TableInfo> table = builder.buildTable(tabInfo1, tabInfo2, tabInfo3);
		
		layout.add(table);
		layout.setWidgetLeftRight(table, 0, Unit.PCT, 50, Unit.PCT);
	}
	
	private void createMap() {
		//TODO: implement
		
		// Vancouver center coordinates
		LatLng vanCity = LatLng.newInstance(49.2569425,-123.123904);
		
		MapOptions options = MapOptions.newInstance();
		options.setZoom(13);
		options.setCenter(vanCity);
		
		map = new MapWidget(options);
		map.setSize("1200px", "800px");
		map.getElement().setId("mapWidget");
		
<<<<<<< HEAD
		layout.add(map);
		layout.setWidgetLeftRight(map, 50, Unit.PCT, 0, Unit.PCT);
=======
		ArrayList<MapInfo> clinics = new ArrayList<MapInfo>();
		clinics.add(new MapInfo("Test Clinic 1", LatLng.newInstance(49.265082, -123.244573)));
		clinics.add(new MapInfo("Test Clinic 2", LatLng.newInstance(49.263671, -123.146184)));
		clinics.add(new MapInfo("Test Clinic 3", LatLng.newInstance(48.42349, -123.366963)));
		
		displayClinics(map, clinics);
		
		RootLayoutPanel.get().add(map);
		
>>>>>>> maps
	}
	
	private void displayClinics(final MapWidget map, ArrayList<MapInfo> clinics) {
		for (MapInfo clinic : clinics) {
			MarkerOptions options = MarkerOptions.newInstance();
			options.setMap(map);
			options.setClickable(true);
			options.setTitle(clinic.getName());
			options.setPosition(clinic.getLatLng());

			final Marker marker = Marker.newInstance(options);
			
			final String desc = clinic.getName();
			
			final InfoWindowOptions iwOptions = InfoWindowOptions.newInstance();
			final InfoWindow infoWindow = InfoWindow.newInstance(iwOptions);
			
			ClickMapHandler handler = new ClickMapHandler() {
				public void onEvent(ClickMapEvent e) {
					System.out.println(desc);
//					infoWindow.setContent("<span style=\"display:inline-block;height:80px;width:180px;\">" + desc + "</span>");
					infoWindow.setContent("<div style=\"max-width:400px; line-height:normal; white-space:nowrap; overflow:auto;\">" + desc + "</div>");
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
	    loadLibraries.add(LoadLibrary.DRAWING);		// need this api to draw overlays/markers on the map
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
