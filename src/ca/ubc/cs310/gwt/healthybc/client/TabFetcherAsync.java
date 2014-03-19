package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TabFetcherAsync {
	
	public void clinicTabInfo(TableInfo ti, AsyncCallback<ClinicTabInfo> callback);
}
