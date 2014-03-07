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
	
<<<<<<< HEAD
	public MapInfo(String name, Location loc) {
		this.name = name;
		this.loc = loc;
	}
	
	public String getName() { return name; }
	public Location getLocation() { return loc; }
=======
	public MapInfo(String string, LatLng loc) {
		this.name = string;
		this.loc = loc;
	}
	public String getName() { return this.name; }
	public LatLng getLatLng() { return this.loc; }
>>>>>>> maps

	public MapInfo setName(String n) {
		this.name = n;
		return this;
	}
	
	public MapInfo setLatLng(LatLng l) {
		this.loc = l;
		return this;
	}
}
