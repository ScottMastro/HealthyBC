package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.ClinicTabInfo;
import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.TabFetcher;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;

@SuppressWarnings("serial")
public class TabFetcherImpl extends RemoteServiceServlet implements TabFetcher {


	public TabFetcherImpl(){
	}

	
	public ArrayList<ClinicTabInfo> clinicTabInfo(TableInfo ti){
		return ClinicManager.getInstance().getClinicTabInfo(ti);
	}


	public ArrayList<ClinicTabInfo> clinicTabInfo(MapInfo mi) {
		return ClinicManager.getInstance().getClinicTabInfo(mi);

	}

}