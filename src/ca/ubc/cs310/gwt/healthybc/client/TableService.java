package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface TableService extends RemoteService {

	public TableInfo[] getTableList();
}
