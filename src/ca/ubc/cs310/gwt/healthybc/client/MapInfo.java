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
	private LatLng loc;
	
	public MapInfo(String string, LatLng loc) {
		this.name = string;
		this.loc = loc;
	}
	public String getName() { return this.name; }
	public LatLng getLatLng() { return this.loc; }

	public MapInfo setName(String n) {
		this.name = n;
		return this;
	}
	
	public MapInfo setLatLng(LatLng l) {
		this.loc = l;
		return this;
	}
}
