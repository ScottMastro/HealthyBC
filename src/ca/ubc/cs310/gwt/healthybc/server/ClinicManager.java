package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.ubc.cs310.gwt.healthybc.client.Clinic;
import ca.ubc.cs310.gwt.healthybc.client.ClinicHours;
import ca.ubc.cs310.gwt.healthybc.client.ClinicTabInfo;
import ca.ubc.cs310.gwt.healthybc.client.MapInfo;
import ca.ubc.cs310.gwt.healthybc.client.TableInfo;


public class ClinicManager {

	private List<Clinic> clinics;
	private ArrayList<MapInfo> mapInfo;
	private ArrayList<TableInfo> tableInfo;

	private RemoteDataManager dataManager;

	private static ClinicManager singleton;

	/**
	 * Singleton method
	 * @return single instance of ClinicManager
	 */
	public static ClinicManager getInstance(){
		if(singleton == null)
			singleton = new ClinicManager();

		return singleton;
	}

	/**
	 * private constructor (singleton)
	 */
	private ClinicManager() {
		clinics = new ArrayList<Clinic>();
		tableInfo = new ArrayList<TableInfo>();
		mapInfo = new ArrayList<MapInfo>();

		dataManager= new RemoteDataManager();
		initalizeClinicData();
	}

	private void initalizeClinicData(){
		dataManager.retrieveAllClinics(this);
	}


	/**
	 * Returns list of clinics stored in this class,
	 * fetches all clinics from datastore if list is empty
	 *
	 * @return list of clinics stored in class
	 */
	public List<Clinic> getClinics(){

		if(clinics.isEmpty())
			refreshFromDatastore();		

		return clinics;
	}

	/**
	 * Replaces the list of clinics stored in this class with all clinics in the datastore
	 */
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
	 * @return true if clinic is successfully added, false if clinic is already in the list
	 * or if ID is null/empty
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

		TableInfo newTableInfo = new TableInfo(refID, name, address, email, newClinic.getLanguages());
		MapInfo newMapInfo = new MapInfo(refID, name, lat, lon, newClinic.getLanguages());

		tableInfo.add(newTableInfo);
		mapInfo.add(newMapInfo);

		return clinics.add(newClinic);
	}

	/**
	 * Removes clinic from the ArrayList of clinics and from MapInfo and TableInfo arrays
	 *
	 * @param refID 	ID of the clinic to be removed; if null or empty, return false
	 * @return true if clinic with corresponding refID is removed, false if clinic is not in list
	 * or
	 */
	public boolean removeClinic(String refID) {

		if (refID == null || refID.isEmpty()) {
			return false;
		}

		for(Clinic clinic : clinics) {
			if(clinic.getRefID().equals(refID)) {

				for(MapInfo mi : mapInfo){
					if (mi.equals(clinic.getName(), clinic.getLatitude(), clinic.getLongitude())){
						mapInfo.remove(mi);
						continue;
					}
				}

				for(TableInfo ti : tableInfo){
					if (ti.equals(clinic.getName(), clinic.getAddress(), clinic.getEmail())){
						tableInfo.remove(ti);
						continue;
					}
				}


				clinics.remove(clinic);
				return true;
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

	public ArrayList<ClinicTabInfo> getClinicTabInfo(TableInfo ti) {
		return getClinicTabInfoFromRefID(ti.getRefID());
	}

	public ArrayList<ClinicTabInfo> getClinicTabInfo(MapInfo mi) {
		return getClinicTabInfoFromRefID(mi.getRefID());
	}

	/**
	 * Finds clinic with given refID and creates ClinicTabInfo object to return
	 *
	 * @param refID of desired clinic
	 * @return a list of the single desired ClinicTabInfo object
	 */
	private ArrayList<ClinicTabInfo> getClinicTabInfoFromRefID(String refID){

		for (Clinic clinic : clinics) {

			if(refID.equals(clinic.getRefID())){

				String name = clinic.getName();
				String hours = clinic.getHoursString();
				Double latitude = clinic.getLatitude();
				Double longitude = clinic.getLongitude();
				String addr = clinic.getAddress();
				String pcode = clinic.getPostalCode();
				String email = clinic.getEmail();
				String phone = clinic.getPhone();
				String lang = clinic.getLanguage();

				ClinicTabInfo cti = new ClinicTabInfo(refID, name, hours, latitude,
						longitude, addr, pcode, email, phone, lang);

				ArrayList<ClinicTabInfo> a = new ArrayList<ClinicTabInfo>();
				a.add(cti);
				return a;

			}
		}

		return null;
	}

	public ArrayList<TableInfo> getSearchTableInfo(String searchBy,
			String searchKey) {

		ArrayList<TableInfo> list = new ArrayList<TableInfo>();
		searchKey = searchKey.toLowerCase();

		if(searchBy.equals("name")){
			for(TableInfo info : tableInfo){

				if(info.getName().toLowerCase().contains(searchKey))
					list.add(info);
			}
		}

		if(searchBy.equals("language")){
			for(TableInfo info : tableInfo){

				String[] languages = info.getLanguages();
				int n = languages.length;

				for(int i = 0; i <= n-1; i++)
					if(languages[i].toLowerCase().equals(searchKey)){
						list.add(info);
						break;
					}
			}
		}


		return list;
	}

	public ArrayList<MapInfo> getSearchMapInfo(String searchBy, String searchKey) {
		ArrayList<MapInfo> list = new ArrayList<MapInfo>();
		searchKey = searchKey.toLowerCase();

		if(searchBy.equals("name")){
			for(MapInfo info : mapInfo){

				if(info.getName().toLowerCase().contains(searchKey))
					list.add(info);
			}
		}

		if(searchBy.equals("language")){
			for(MapInfo info : mapInfo){

				String[] languages = info.getLanguages();
				int n = languages.length;

				for(int i = 0; i <= n-1; i++)
					if(languages[i].toLowerCase().equals(searchKey)){
						list.add(info);
						break;
					}
			}
		}


		return list;
	}
}


