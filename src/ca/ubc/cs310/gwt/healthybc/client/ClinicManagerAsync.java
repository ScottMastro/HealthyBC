package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ca.ubc.cs310.gwt.healthybc.server.Clinic;
import ca.ubc.cs310.gwt.healthybc.server.Location;

public interface ClinicManagerAsync extends ClinicManager {
	
	public ClinicManagerAsync addNewClinic(AsyncCallback<Boolean> callback, String refID,
			String name, Location loc, String hours, String address, String pcode, String email,
			String phone, String languages);
	
	public ClinicManagerAsync removeClinic(AsyncCallback<Boolean> callback, String refID);
	
	public void getClinicList(AsyncCallback<Clinic[]> callback);
}
