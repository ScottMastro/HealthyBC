package ca.ubc.cs310.gwt.healthybc.server;

import ca.ubc.cs310.gwt.healthybc.client.TableInfo;
import ca.ubc.cs310.gwt.healthybc.client.TableService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TableServiceImpl  extends RemoteServiceServlet implements TableService {

	@Override
	public TableInfo[] getTableList() {
		// TODO Auto-generated method stub
		return null;
	}
}
