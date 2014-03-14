package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;


public class ClinicManager {

	private List<Clinic> clinics;
	private ArrayList<MapInfo> mapInfo;
	private ArrayList<TableInfo> tableInfo;

	private RemoteDataManager dataManager;

	private static ClinicManager singleton;

	public static ClinicManager getInstance(){
		if(singleton == null)
			singleton = new ClinicManager();

		return singleton;
	}

	private ClinicManager() {
		clinics = new ArrayList<Clinic>();
		tableInfo = new ArrayList<TableInfo>();
		mapInfo = new ArrayList<MapInfo>();

		dataManager= new RemoteDataManager();
		initalizeClinicData();
	}

	private void initalizeClinicData(){
		dataManager.retrieveAllClinics(this);

		for(Clinic clinic : clinics) {
			TableInfo newTableInfo = new TableInfo(clinic.getName(), clinic.getAddressString(), clinic.getEmail());
			MapInfo newMapInfo = new MapInfo(clinic.getName(), clinic.getLatitude(), clinic.getLongitude());

			tableInfo.add(newTableInfo);
			mapInfo.add(newMapInfo);			

		}

	}

	public List<Clinic> getClinics(){

		if(clinics.isEmpty())
			refreshFromDatastore();		

		return clinics;
	}

	public void refreshFromDatastore(){
		clinics.clear();
		dataManager.retrieveAllClinics(this);
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


		ClinicHours newHours = new ClinicHours(hours);
		Clinic newClinic = new Clinic(refID, name, newHours, lat, lon, address, pcode, email, phone, languages);

		dataManager.addAndUploadClinicEntity(newClinic);

		//#TODO: ADD CLINIC TO MAP/TABLE INFO

		return clinics.add(newClinic);
	}

	/**
	 * Removes clinic from the ArrayList of clinics
	 *
	 * @param refID 	ID of the clinic to be removed; if null or empty, return false
	 * @return true if clinic with corresponding refID is removed
	 */
	public boolean removeClinic(String refID) {

		//#TODO: REMOVE CLINIC TO MAP/TABLE INFO

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

	public ArrayList<MapInfo> getMapInfo() {
		return mapInfo;		
	}

	public ArrayList<TableInfo> getTableInfo() {
		return tableInfo;
	}

}
