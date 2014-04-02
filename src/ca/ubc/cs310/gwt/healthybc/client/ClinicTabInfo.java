package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClinicTabInfo implements Serializable {

	//note: it makes more sense to have a Clinic member in this class
	//however it is not serializable and therefore will not work in the RPC
	//so don't try to use it here

	private String refID;
	private String name;
	private String hours;
	private Double latitude;
	private Double longitude;
	private String addr;
	private String pcode;
	private String email;
	private String phone;
	private String lang;

	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected ClinicTabInfo() {}

	public ClinicTabInfo(String refID, String name, String hours, Double lat,
			Double lon, String addr, String pcode, String email, String phone, String lang){
		this.refID = refID;
		this.name = name;
		this.hours = hours;
		this.latitude = lat;
		this.longitude = lon;
		this.addr = addr;
		this.pcode = pcode;
		this.email = email;
		this.phone = phone;
		this.lang = lang;
	}

	public String getDistance(Double lat, Double lon){
		double d = distFrom(lat, lon, this.latitude, this.longitude, "K");
		
		String distance = String.valueOf(d);
		int decimal = distance.indexOf(".");
		distance = distance.substring(0, decimal + 2);
		
		return distance;
		
	}

	/**
	 * Calculates the distance in km between two lat/long points
	 * using the haversine formula
	 */
	private double distFrom(double lat1, double lng1, double lat2, double lng2, String unit) {
		double theta = lng1 - lng2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return (dist);

	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public String getRefID() { return refID; }
	public String getName() { return name; }
	public String getHours() { return hours; }
	public String getAddressString() { return addr + pcode; }
	public Double getLatitude() { return latitude; }
	public Double getLongitude() { return longitude; }
	public String getEmail() { return email; }
	public String getPhone() { return phone; }
	public String getLanguages() { return lang; }
	public String getAddress() { return addr; }
	public String getPostalCode() { return pcode; }

	public boolean equals(String refID){
		return refID.equals(this.refID);
	}
}