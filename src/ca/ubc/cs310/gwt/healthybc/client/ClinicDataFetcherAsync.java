package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClinicDataFetcherAsync {
	public void mapInfo(AsyncCallback<ArrayList<MapInfo>> callback);
	
	public void tableInfo(AsyncCallback<ArrayList<TableInfo>> callback);
}
