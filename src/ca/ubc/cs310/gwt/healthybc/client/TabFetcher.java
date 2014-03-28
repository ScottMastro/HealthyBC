package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tabFetch")
public interface TabFetcher extends RemoteService {
	
	public ArrayList<ClinicTabInfo> clinicTabInfo(TableInfo ti);

	public ArrayList<ClinicTabInfo> clinicTabInfo(MapInfo mi);

	public ArrayList<Boolean> sendEmail(String text, String emailAddress, String title);
}
