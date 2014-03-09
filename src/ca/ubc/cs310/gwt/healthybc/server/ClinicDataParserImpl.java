package ca.ubc.cs310.gwt.healthybc.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.ClinicDataParser;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;

public class ClinicDataParserImpl extends RemoteServiceServlet implements ClinicDataParser {
	
	/**
	 * serialization
	 */
	private static final long serialVersionUID = 1L;
	
	private String csvFile = "data/walkinclinics.csv";
	private ArrayList<Clinic> parsedClinicdata = new ArrayList<Clinic>();
	private BufferedReader br = null;
	private String line = "";
	private String cvsSplitBy = ";";
	private boolean isParsed = false;
	
	public ClinicDataParserImpl(){
	}
	
	private void parse() {
		if (isParsed) {
			return;
		}
		else {
			PrivilegedAction< ArrayList<Clinic> > action = new PrivilegedAction< ArrayList<Clinic> >() {
				@Override
				public ArrayList<Clinic> run() {
					try {
						br = new BufferedReader(new FileReader(csvFile));
						
						while ((line = br.readLine()) != null) {
				 
						    // use semicolon as separator
							String[] cells = line.split(cvsSplitBy);
							
							String name = cells[0];
							String refID = cells[1];
							String phone = cells[2];
							String website = cells[3];
							String email = cells[4];
							String wc_acess = cells[5];
							String languages = cells[6];
							String street_no = cells[7];
							String street_name = cells[8];
							String street_type = cells[9];
							String city = cells[10];
							String pcode = cells[11];
							String latitude = cells[12];
							String longitude = cells[13]; 
							String desc = cells[14];
							String hours = cells[15];
							
							String address = street_no + " " + street_name + " " + street_type + " " + city;
							ClinicHours newhrs = new ClinicHours(hours);
							Location newLoc = new Location( Double.parseDouble(latitude), Double.parseDouble(longitude));
							
							Clinic newClinic = new Clinic(refID, name, newhrs, newLoc, address, pcode, email, phone, languages);
							
							parsedClinicdata.add(newClinic);
						}
						
						isParsed = true;
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (br != null) {
							try {
								br.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
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