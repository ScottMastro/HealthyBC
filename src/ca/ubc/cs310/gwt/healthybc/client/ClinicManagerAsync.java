package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClinicManagerAsync {
	
	public void addNewClinic(String refID, String name, Double lat, Double lon, String hours,
			String address, String pcode, String email, String phone, String languages, AsyncCallback<Boolean> callback);
	
	public void removeClinic(String refID, AsyncCallback<Boolean> callback);
}
