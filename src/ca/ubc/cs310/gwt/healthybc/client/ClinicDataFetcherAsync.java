package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClinicDataFetcherAsync {
	public void mapInfo(String searchBy, String searchKey, AsyncCallback<ArrayList<MapInfo>> callback);
	
	public void tableInfo(String searchBy, String searchKey, AsyncCallback<ArrayList<TableInfo>> callback);
	
}
