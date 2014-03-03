package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import ca.ubc.cs310.gwt.healthybc.client.ClinicManager;

public class ClinicManagerImpl implements ClinicManager {

	private ArrayList<Clinic> clinics;

	/**
	 * Adds a new Clinic to the ArrayList of clinics
	 *
	 * @param refID 	ID of the clinic to be constructed
	 * @param name		full name of the clinic to be constructed
	 * @param lat       latitude of the clinic to be constructed
	 * @param lon	    longitude of the clinic to be constructed
	 * @param hours	    hours of the clinic to be constructed
	 * @param address   address of the clinic to be constructed
	 * @param pcode     postal code of the clinic to be constructed
	 * @param email		email address of the clinic to be constructed
	 * @param phone		phone number of the clinic to be constructed
	 * @param languages	languages understood by the clinic to be constructed
	 */
	@Override
	public void addNewClinic(String refID, String name, Double lat, Double lon,
			String hours, String address, String pcode, String email, String phone,
			String languages) {

		Location location = new Location(lat, lon);

		ClinicHours newHours = new ClinicHours(hours);
		Clinic newClinic = new Clinic(refID, name, newHours, location, address, pcode, email, phone, languages);
		
		if (clinics == null) {
			clinics = new ArrayList<Clinic>();
		}
		clinics.add(newClinic);
	}

	/**
	 * Removes clinic from the ArrayList of clinics
	 *
	 * @param refID 	ID of the clinic to be removed
	 */
	@Override
	public void removeClinic(String refID) {

		for(Clinic clinic : clinics) {
			if(clinic.getRefID().equals(refID)) {
				if (clinics.remove(clinic)) {
					return;
				}
				else {
					throw new RuntimeException("Not supposed to happen!");
				}
			}
			
			throw new RuntimeException("removeClinic did not find clinic to be removed.");
		}
	}
}
