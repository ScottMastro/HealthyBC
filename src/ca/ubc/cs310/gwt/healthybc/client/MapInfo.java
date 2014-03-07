package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

import com.google.gwt.maps.client.base.LatLng;

import ca.ubc.cs310.gwt.healthybc.server.Location;

public class MapInfo implements Serializable {

	/**
	 * for serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double lat;
	private double lng;
	
	public MapInfo(String name, double lat, double lng) {
		this.name = name;
		this.lat = lat;
		this.lng = lng;
	}
	
	public String getName() { return this.name; }
	public double getLat() { return this.lat; }
	
	public MapInfo setName(String n) {
		this.name = n;
		return this;
	}
	
	public MapInfo setLat(double l) {
		this.lat = l;
		return this;
	}
	
	public MapInfo setLng(double l) {
		this.lng = l;
		return this;
	}
}
