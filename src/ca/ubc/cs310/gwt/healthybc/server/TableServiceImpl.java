package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs310.gwt.healthybc.client.TableInfo;
import ca.ubc.cs310.gwt.healthybc.client.TableService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TableServiceImpl  extends RemoteServiceServlet implements TableService {

	/**
	 * serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private List<TableInfo> tableList;
	
	public TableServiceImpl() {
		tableList = new ArrayList<TableInfo>();
	}
	
	/**
	 * @return an array of TableInfo
	 */
	@Override
	public TableInfo[] getTableList() {
		return (TableInfo[]) tableList.toArray();
	}
}
