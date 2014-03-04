package ca.ubc.cs310.gwt.healthybc.client;

public interface ClinicManager {

	public void addNewClinic(String refID, String name, Double lat, Double lon,
			String hours, String address, String pcode, String email, String phone,
			String languages);
	
	public boolean removeClinic(String refID);
}
