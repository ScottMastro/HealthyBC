package ca.ubc.cs310.gwt.healthybc.server;

import ca.ubc.cs310.gwt.healthybc.client.Clinic;
import ca.ubc.cs310.gwt.healthybc.client.ClinicHours;

public class DatasetLineParser {
	
	private static String cvsSplitBy = "\\t";
	
	public static Clinic parseInputLine(String line){
		
		if (line == null){
			return null;
		}
		
		String[] cells = line.split(cvsSplitBy);
		
		// check length and ignore file header
		if (cells.length != 25 || cells[0].equals("SV_TAXONOMY")){	
			return null;
		}
		
		String name = cells[3];
		String refID = cells[7];
		String phone = cells[9];
		String email = cells[11];
		String languages = cells[13];	/* no necessarily comma separated */
		String hours = cells[14];
		String street_no = cells[15];
		String street_name = cells[16];
		String street_type = cells[17];
		String city = cells[19];
		String pcode = cells[21];
		
		// Perform validation
		
		if(!refID.startsWith("SL")){
			return null;
		}
		
		try {
			
			Double lat = Double.parseDouble(cells[22]);
			Double lon = Double.parseDouble(cells[23]);
			long phonenumber = Long.parseLong(phone);
			
			String address = street_no + " " + street_name + " " + street_type + " " + city;
			address = address.replaceAll("\\s+", " ");	/* remove additional spaces */
			
			ClinicHours newhrs = new ClinicHours(hours);
			
			Clinic newClinic = new Clinic(refID, name, newhrs, lat, lon, address, pcode, email, phone, languages);
			
			return newClinic;
		
		} catch (NumberFormatException e){
			System.out.println("def");
			return null;
		}
		
	}

}
