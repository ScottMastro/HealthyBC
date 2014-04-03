package ca.ubc.cs310.gwt.healthybc.client;

public class Clinic {
	private String refID;
	private String name;
	private ClinicHours hours;
	private Double latitude;
	private Double longitude;
	private String addr;
	private String pcode;
	private String email;
	private String phone;
	private String lang;
	private String[] languages;


	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected Clinic() {
	}

	public Clinic(String refID, String name, ClinicHours hours, Double lat, Double lon, String addr, String pcode,
			String email, String phone, String lang) {
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
		lang = lang.replace("and ", "");
		lang = lang.replace(" ", "");
		this.languages = lang.split(",");
	}

	public String getRefID() { return refID; }
	public String getName() { return name; }
	public String getHoursString() { return hours.getHours(); }
	public Double getLatitude() { return latitude; }
	public Double getLongitude() { return longitude; }
	public String getEmail() { return email; }
	public String getPhone() { return phone; }
	public String getLanguage() { return lang; }
	public String getAddress() { return addr; }
	public String getPostalCode() { return pcode; }
	public String[] getLanguages() { return languages; }


}
