package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MapServiceAsync {

	public void getMapList(AsyncCallback<MapInfo[]> callback);
}
