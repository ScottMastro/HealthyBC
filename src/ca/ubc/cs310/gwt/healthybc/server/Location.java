package ca.ubc.cs310.gwt.healthybc.server;

public class Location {
	private double latitude;
	private double longitude;
	
	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	public Location() {
	}
	
	public Location(double lat, double lon) {
		latitude = lat;
		longitude = lon;
	}
	
	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }
}
