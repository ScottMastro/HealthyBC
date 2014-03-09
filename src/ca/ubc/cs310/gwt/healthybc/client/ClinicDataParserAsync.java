package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClinicDataParserAsync {
	public void MockMapInfo(AsyncCallback<ArrayList<MapInfo>> callback);
	
	public void MocktableInfo(AsyncCallback<ArrayList<TableInfo>> callback);
}
