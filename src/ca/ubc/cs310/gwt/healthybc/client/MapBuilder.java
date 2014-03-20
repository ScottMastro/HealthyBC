package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;

public class MapBuilder implements AsyncCallback<ArrayList<MapInfo>> {
	
	private InfoWindow infoWindow;
	private SimplePanel mapContainer = new SimplePanel();
	private MapWidget map;
	private HealthyBC main;
	
	public MapBuilder(HealthyBC h){
		main = h;
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
			options.setTitle(clinic.getRefID());

			final Marker marker = Marker.newInstance(options);
			final String desc = clinic.getName();
			final MapInfo mi = clinic;

			ClickMapHandler handler = new ClickMapHandler() {
				public void onEvent(ClickMapEvent e) {
					System.out.println(desc);
					infoWindow.setContent("<div class=\"markerContent\" style=\"line-height:normal; white-space:nowrap;\">" + desc + "</div>");
					infoWindow.open(map, marker);
					main.getTabFromMapInfo(mi);
				} 
			};
			marker.addClickHandler(handler);
		}
	}
}
