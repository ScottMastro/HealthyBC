package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ca.ubc.cs310.gwt.healthybc.server.Clinic;

public interface ClinicManagerAsync {
	
	public void addNewClinic(String refID, String name, Double lat, Double lon, String hours,
			String address, String pcode, String email, String phone, String languages, AsyncCallback<Boolean> callback);
	
	public void removeClinic(String refID, AsyncCallback<Boolean> callback);
	
	public void getClinicList(AsyncCallback<Clinic[]> callback);
}
