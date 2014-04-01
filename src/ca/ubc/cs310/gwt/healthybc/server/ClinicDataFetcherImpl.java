package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.ClinicDataFetcher;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;

@SuppressWarnings("serial")
public class ClinicDataFetcherImpl extends RemoteServiceServlet implements ClinicDataFetcher {


	public ClinicDataFetcherImpl(){
	}


	public ArrayList<MapInfo> mapInfo(){
		return ClinicManager.getInstance().getMapInfo();
	}

	public ArrayList<TableInfo> tableInfo(String searchBy, String searchKey){
		if(searchBy == null && searchKey == null)
			return ClinicManager.getInstance().getTableInfo();
		else
			return ClinicManager.getInstance().getSearchTableInfo(searchBy, searchKey);
	}

}