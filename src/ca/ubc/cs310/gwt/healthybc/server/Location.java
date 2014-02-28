package ca.ubc.cs310.gwt.healthybc.server;

public class Location {

	private double latitude;
	private double longitude;
	
	public Location( double latitude, double longitude ) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public double getLat() {
		return latitude;
	}
	
	public double getLong() {
		return longitude;
	}
}
