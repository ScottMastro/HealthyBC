package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

public class MapInfo implements Serializable {
	
	/**
	 * for serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double latitude;
	private double longitude;
	
	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected MapInfo() {
	}
	
	public MapInfo(String name, double latitude, double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public String getName() { return this.name; }
	public double getLatitude() { return this.latitude; }
	public double getLongitude() { return this.longitude; }
	
	public MapInfo setName(String n) {
		name = n;
		return this;
	}
	
	public MapInfo setLatitude(double l) {
		this.latitude = l;
		return this;
	}
	
	public MapInfo setLongitude(double l) {
		this.longitude = l;
		return this;
	}
}
