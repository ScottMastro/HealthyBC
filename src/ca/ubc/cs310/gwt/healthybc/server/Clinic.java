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
	
	public Clinic(String refID, String name, ClinicHours hours, Location loc,
			String addr, String pcode, String email, String phone, String lang) {
		this.refID = refID;
		this.name = name;
		this.hours = hours;
		this.loc = loc;
		this.addr = addr;
		this.pcode = pcode;
		this.email = email;
		this.phone = phone;
		this.lang = lang;
	}
	public String getRefID() {
		return refID;
	}
	public String getName() {
		return name;
	}
	public ClinicHours getHours() {
		return hours;
	}
	public Location getLoc() {
		return loc;
	}
	public String getAddr() {
		return addr;
	}
	public String getPcode() {
		return pcode;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getLang() {
		return lang;
	}
	
}
