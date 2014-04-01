package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;


public class MapInfo implements Serializable {
	
	/**
	 * for serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	private String refID;
	private String name;
	private double latitude;
	private double longitude;
	private String[] languages;
	
	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected MapInfo() {
	}
	
	public MapInfo(String refID, String name, double latitude, double longitude, String languages[]) {
		this.refID = refID;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.languages = languages;
	}
	
	public String getRefID() { return this.refID; }
	public String getName() { return this.name; }
	public double getLatitude() { return this.latitude; }
	public double getLongitude() { return this.longitude; }
	public String[] getLanguages() { return this.languages; }

	
	public boolean equals(String clinicName, Double lat, Double lon){
		boolean match1 = clinicName.equals(name);
		boolean match2 = lat.equals(latitude);
		boolean match3 = lon.equals(longitude);
		
		return match1 && match2 && match3;
	}
}
