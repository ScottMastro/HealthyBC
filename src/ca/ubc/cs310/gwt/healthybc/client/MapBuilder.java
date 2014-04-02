package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.MapEventType;
import com.google.gwt.maps.client.events.MapHandlerRegistration;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

public class MapBuilder implements AsyncCallback<ArrayList<MapInfo>> {
	
	private InfoWindow infoWindow;
	private SimplePanel mapContainer = new SimplePanel();
	private FixedMap map;
	private HealthyBC main;
	private Double latCentre;
	private Double lonCentre;
	private boolean setAddress = false;
	
	public MapBuilder(HealthyBC h, Double latCentre, Double lonCentre){
		main = h;
		
		setAddress = true;		
		// Vancouver center coordinates
		if(latCentre == null)
			this.latCentre = 49.2569425;
		else
			this.latCentre = latCentre;
		if(lonCentre == null)
			this.lonCentre = -123.123904;
		else
			this.lonCentre = lonCentre;
	}
	
	public MapBuilder(HealthyBC h){
		main = h;
		
		this.latCentre = 49.2569425;
		this.lonCentre = -123.123904;
	}
	
	public MapWidget getMap(){
		return map;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		caught.printStackTrace();
	}
	
	/**
	 * Sets up map on successful result return
	 */
	@Override
	public void onSuccess(ArrayList<MapInfo> result) {
		LatLng centre = LatLng.newInstance(latCentre, lonCentre);

		InfoWindowOptions iwOptions = InfoWindowOptions.newInstance();
		infoWindow = InfoWindow.newInstance(iwOptions);

		MapOptions options = MapOptions.newInstance();
		options.setZoom(13);
		options.setCenter(centre);

		map = new FixedMap(options);
		map.setSize("100%", "100%");

		displayClinics(map, result);
		
		mapContainer.add(map);
		main.addMap(mapContainer);
		map.triggerResize();
	}
	
	/**
	 * Displays each clinic as a marker on the map
	 * @param map the map to display markers on
	 * @param clinics the clinic information used to find marker positioning
	 */
	private void displayClinics(final MapWidget map, ArrayList<MapInfo> clinics) {
		for (MapInfo clinic : clinics) {
			MarkerOptions options = MarkerOptions.newInstance();
			options.setMap(map);
			options.setClickable(true);
			options.setTitle(clinic.getName());
			options.setPosition(LatLng.newInstance(clinic.getLatitude(), clinic.getLongitude()));

			final Marker marker = Marker.newInstance(options);
			final String desc = clinic.getName();
			final MapInfo mi = clinic;

			ClickMapHandler handler = new ClickMapHandler() {
				public void onEvent(ClickMapEvent e) {
					infoWindow.setContent("<div class=\"markerContent\" style=\"line-height:normal; white-space:nowrap;\">" + desc + "</div>");
					infoWindow.open(map, marker);
					main.getTabFromMapInfo(mi);
				} 
			};
			marker.addClickHandler(handler);
		}
		
		if(setAddress){
			MarkerOptions options = MarkerOptions.newInstance();
			options.setMap(map);
			options.setClickable(true);
			options.setTitle("My Address");
			options.setPosition(LatLng.newInstance(latCentre, lonCentre));

			final Marker marker = Marker.newInstance(options);
			final String desc = "My Address";

			ClickMapHandler handler = new ClickMapHandler() {
				public void onEvent(ClickMapEvent e) {
					infoWindow.setContent("<div class=\"markerContent\" style=\"line-height:normal; white-space:nowrap;\">" + desc + "</div>");
					infoWindow.open(map, marker);
				} 
			};
			marker.addClickHandler(handler);
			
		}
	}
	
	private class FixedMap extends MapWidget{

		public FixedMap(MapOptions options) {
			super(options);
			}
		
		 @Override
		 protected void onAttach() {
		     super.onAttach();
		     Timer timer = new Timer() {

		         @Override
		         public void run() {
		             resize();
		         }
		     };
		     timer.schedule(5);
		 }

		 /*
		  * This method is called to fix the Map loading issue when opening
		  * multiple instances of maps in different tabs
		  * Triggers a resize event to be consumed by google api in order to resize view
		  * after attach.
		  *
		  */
		 public void resize() {
		     LatLng center = this.getCenter();
		     MapHandlerRegistration.trigger(this, MapEventType.RESIZE);        
		     this.setCenter(center);
		 }
		
	}
}
