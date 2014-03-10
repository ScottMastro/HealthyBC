package ca.ubc.cs310.gwt.healthybc.client;

import ca.ubc.cs310.gwt.healthybc.server.Clinic;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("clinic")
public interface ClinicManager extends RemoteService{

	public boolean addNewClinic(String refID, String name, Double lat, Double lon,
			String hours, String address, String pcode, String email, String phone,
			String languages);
	
	public boolean removeClinic(String refID);
	
}
