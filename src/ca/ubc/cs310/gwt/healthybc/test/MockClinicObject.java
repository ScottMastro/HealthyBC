package ca.ubc.cs310.gwt.healthybc.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;
import ca.ubc.cs310.gwt.healthybc.server.Clinic;
import ca.ubc.cs310.gwt.healthybc.server.ClinicHours;
import ca.ubc.cs310.gwt.healthybc.server.Location;

public class MockClinicObject {
	
	private String csvFile = "data/walkinclinics.csv";
	private ArrayList<Clinic> parsedClinicdata;
	private BufferedReader br = null;
	private String line = "";
	private String cvsSplitBy = ";";
	
	public MockClinicObject(){
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
		
	}


	public ArrayList<MapInfo> MockMapInfo(){
		ArrayList<MapInfo> testMapInfo = null;
		
		return testMapInfo;
	}

	public ArrayList<TableInfo> MocktableInfo(){
		ArrayList<TableInfo> testTableInfo = null;
		
		return testTableInfo;
	}

}