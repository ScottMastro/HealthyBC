package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parser")
public interface ClinicDataParser extends RemoteService {
	public ArrayList<MapInfo> MockMapInfo();
	
	public ArrayList<TableInfo> MocktableInfo();
}
