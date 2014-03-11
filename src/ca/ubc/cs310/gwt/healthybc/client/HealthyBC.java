package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
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

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		init();
	}

	private void init() {
		createUI();
		createButton();
		createTable();
		loadMapApi();
		
		RootLayoutPanel r = RootLayoutPanel.get();
		r.add(layout);
	    r.add(buttonPanel);
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

		final FormPanel form = new FormPanel();
	    form.setMethod(FormPanel.METHOD_POST);
	    form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setAction("/healthybc/uploadServlet");
	    
	        // then there's a button they can click which calls form.submit();
	    form.setWidget(new Button("Submit", (new ClickHandler() {
	      @Override
	      public void onClick(ClickEvent event) {
	        form.submit();
	      }
	    })));
	    
	    buttonPanel.add(form);
		
	    final FileUpload clinicFileUpload = new FileUpload();
		clinicFileUpload.setName("Add Clinics");
		buttonPanel.add(clinicFileUpload);

		// Add an event handler to the form.
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				// This event is fired just before the form is submitted. We can take
				// this opportunity to perform validation.
			
				if (clinicFileUpload.getFilename().length() == 0) {
					Window.alert("The text box must not be empty");
					event.cancel();
				}
				else if (!clinicFileUpload.getFilename().endsWith(".csv")){
					Window.alert("Can only upload .csv files");
					event.cancel();
				}
					
			}
		});
		
	    form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	        @Override
	    	public void onSubmitComplete(SubmitCompleteEvent event) {
	          // When the form submission is successfully completed, this event is
	          // fired. Assuming the service returned a response of type text/html,
	          // we can get the result text here (see the FormPanel documentation for
	          // further explanation).
	          Window.alert(event.getResults());
	        }
	      });

	}

	private void addClinic() {
		// Initialize the service proxy
		clinicManager = GWT.create(ClinicManager.class);

		// Set up the callback object
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				// TODO: Do something with errors
			}

			public void onSuccess(Boolean result) {
				System.out.println(result);
			}

		};

		String refID = "123";
		String name = "False Clinic";
		double lat = 123.23;
		double lon = 321.32;
		String hours = "6AM-8PM weekdays";
		String address = "123 Whitmore Street";
		String pcode = "V1A 2B3";
		String email = "contact@falseclinic.ca";
		String phone = "1234567";
		String languages = "English/French/German";

		clinicManager.addNewClinic(refID, name, lat, lon, hours, address, pcode, email, phone, languages, callback);
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

			layout.add(panel);
			layout.setWidgetLeftRight(panel, 0, Unit.PCT, 50, Unit.PCT);
			layout.add(buttonPanel);
			layout.setWidgetLeftRight(buttonPanel, 50, Unit.PCT, 0, Unit.PCT);
		}
	}

	private void createTable() {
		//TODO: get real parsed data in here; the following uses mock objects
		ClinicDataParserAsync clinicParser = GWT.create(ClinicDataParser.class);

		TableInfoListCallback callback = new TableInfoListCallback();
		clinicParser.MocktableInfo(callback);
	}

	private void createMap() {
		//TODO: implement

		// Vancouver center coordinates
		LatLng vanCity = LatLng.newInstance(49.2569425,-123.123904);

		InfoWindowOptions iwOptions = InfoWindowOptions.newInstance();
		infoWindow = InfoWindow.newInstance(iwOptions);

		MapOptions options = MapOptions.newInstance();
		options.setZoom(13);
		options.setCenter(vanCity);

		map = new MapWidget(options);
		map.setSize("100%", "100%");
		//		map.getElement().setId("mapWidget");

		//		MockClinicObject mco = new MockClinicObject();
		//		ArrayList<MapInfo> clinics = mco.MockMapInfo();
		ArrayList<MapInfo> clinics = new ArrayList<MapInfo>();
		clinics.add(new MapInfo("Test Clinic 1", 49.265082, -123.244573));
		clinics.add(new MapInfo("Test Clinic 2", 49.263671, -123.146184));
		clinics.add(new MapInfo("Test Clinic 3", 48.42349, -123.366963));

		displayClinics(map, clinics);

		mapContainer.add(map);
		//		layout.setWidgetLeftRight(map, 50, Unit.PCT, 0, Unit.PCT);
		map.triggerResize();
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
					infoWindow.setContent("<div style=\"max-width:250px; line-height:normal; white-space:nowrap; overflow:auto;\">" + desc + "</div>");
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
