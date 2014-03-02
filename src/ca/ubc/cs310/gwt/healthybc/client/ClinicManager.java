package ca.ubc.cs310.gwt.healthybc.client;

import ca.ubc.cs310.gwt.healthybc.server.Location;

public interface ClinicManager {

	public ClinicManager addNewClinic(String refID, String name, Location loc,
							String address, String pcode, String email, String phone,
							String languages);
	
	public ClinicManager removeClinic(String refID);
}
