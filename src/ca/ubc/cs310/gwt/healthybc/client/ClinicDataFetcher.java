package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("dataFetch")
public interface ClinicDataFetcher extends RemoteService {
	public ArrayList<MapInfo> mapInfo();
	
	public ArrayList<TableInfo> tableInfo(String searchBy, String searchKey);
	
}
