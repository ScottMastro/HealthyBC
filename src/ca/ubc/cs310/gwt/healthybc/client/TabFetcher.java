package ca.ubc.cs310.gwt.healthybc.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tabFetch")
public interface TabFetcher extends RemoteService {
	
	public ClinicTabInfo clinicTabInfo(TableInfo ti);

}
