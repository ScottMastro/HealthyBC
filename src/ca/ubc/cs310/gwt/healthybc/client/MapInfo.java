package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

import ca.ubc.cs310.gwt.healthybc.server.Location;

public class MapInfo implements Serializable {

	/**
	 * for serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Location loc;
	
	public String getName() { return name; }
	public Location getLocation() { return loc; }

	public MapInfo setName(String n) {
		name = n;
		return this;
	}
	
	public MapInfo setLocation(Location l) {
		loc = l;
		return this;
	}
}
