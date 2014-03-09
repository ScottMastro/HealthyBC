package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import ca.ubc.cs310.gwt.healthybc.client.ClinicManager;

public class ClinicManagerImpl extends RemoteServiceServlet implements ClinicManager {

	private List<Clinic> clinics;
	
	public ClinicManagerImpl() {
		clinics = new ArrayList<Clinic>();
	}

	/**
	 * Adds a new Clinic to the ArrayList of clinics, with no duplicate (defined as same
	 * refID) allowed
	 *
	 * @param refID 	ID of the clinic to be constructed, cannot be null or empty
	 * @param name		full name of the clinic to be constructed
	 * @param lat       latitude of the clinic to be constructed
	 * @param lon	    longitude of the clinic to be constructed
	 * @param hours	    hours of the clinic to be constructed
	 * @param address   address of the clinic to be constructed
	 * @param pcode     postal code of the clinic to be constructed
	 * @param email		email address of the clinic to be constructed
	 * @param phone		phone number of the clinic to be constructed
	 * @param languages	languages understood by the clinic to be constructed
	 * 
	 * @return true if clinic is successfully added
	 */
	@Override
	public boolean addNewClinic(String refID, String name, Double lat, Double lon,
			String hours, String address, String pcode, String email, String phone,
			String languages) {
		
		if (refID == null || refID.isEmpty()) {
			return false;
		}
		
		for (Clinic c : clinics) {
			if (c.getRefID().equals(refID)) {
				return false;
			}
		}
		
		Location location = new Location(lat, lon);

		ClinicHours newHours = new ClinicHours(hours);
		Clinic newClinic = new Clinic(refID, name, newHours, location, address, pcode, email, phone, languages);
		
		return clinics.add(newClinic);
	}

	/**
	 * Removes clinic from the ArrayList of clinics
	 *
	 * @param refID 	ID of the clinic to be removed; if null or empty, return false
	 * @return true if clinic with corresponding refID is removed
	 */
	@Override
	public boolean removeClinic(String refID) {
		if (refID == null || refID.isEmpty()) {
			return false;
		}

		for(Clinic clinic : clinics) {
			if(clinic.getRefID().equals(refID)) {
				if (clinics.remove(clinic)) {
					return true;
				}
				else {
					throw new RuntimeException("Not supposed to happen!");
				}
			}
		}
		
		return false;
	}

	@Override
	public Clinic[] getClinicList() {
		return (Clinic[]) clinics.toArray();
	}
}
