package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TableServiceAsync {

	public void getTableList(AsyncCallback<TableInfo[]> callback);
}
