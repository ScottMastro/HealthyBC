package ca.ubc.cs310.gwt.healthybc.server;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import ca.ubc.cs310.gwt.healthybc.client.ClinicManager;
import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.ClinicDataParser;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;

public class ClinicDataParserImpl extends RemoteServiceServlet implements ClinicDataParser {
	
	/**
	 * serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Clinic> parsedClinicdata = new ArrayList<Clinic>();
	private boolean isParsed = false;
	private ClinicManagerImpl mymanager;
	
	public ClinicDataParserImpl(){
	}
	
	private void parse() {
		if (isParsed) {
			return;
		}
		else {
			PrivilegedAction< List<Clinic> > action = new PrivilegedAction< List<Clinic> >() {
				@Override
				public List<Clinic> run() {
					mymanager = new ClinicManagerImpl();
					mymanager.refreshFromDatastore();
					parsedClinicdata = mymanager.getClinics();
					isParsed = true;
					return parsedClinicdata;
				}
			};
			AccessController.doPrivileged(action);
		}
	}
	
	public ArrayList<MapInfo> MockMapInfo(){
		parse();
		
		ArrayList<MapInfo> testMapInfo = new ArrayList<MapInfo>();
		for (Clinic myclinic : parsedClinicdata){
			MapInfo newMapInfo = new MapInfo(myclinic.getName(), myclinic.getLoc().getLatitude(), myclinic.getLoc().getLongitude());
			testMapInfo.add(newMapInfo);
		}
		return testMapInfo;
	}

	public ArrayList<TableInfo> MocktableInfo(){
		parse();
		
		ArrayList<TableInfo> testTableInfo = new ArrayList<TableInfo>();
		for (Clinic myclinic : parsedClinicdata){
			TableInfo newTableInfo = new TableInfo(myclinic.getName(), myclinic.getAddressString(), myclinic.getEmail());
			testTableInfo.add(newTableInfo);
		}
		return testTableInfo;
	}

}