package ca.ubc.cs310.gwt.healthybc.server;

public class Clinic {
	private String refID;
	private String name;
	private ClinicHours hours;
	private Location loc;
	private String addr;
	private String pcode;
	private String email;
	private String phone;
	private String lang;
	
	public String getRefID() { return refID; }
	public String getName() { return name; }
	public String getHoursString() { return hours.getHours(); }
	public Location getLoc() { return loc; }
	public String getAddressString() { return addr + pcode; }
	public String getEmail() { return email; }
	public String getPhone() { return phone; }
	public String getLanguages() { return lang; }
}
