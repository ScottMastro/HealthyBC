package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class DataUploadManager {

	
	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private ArrayList<Entity> clinicEntities;
	private static DataUploadManager instance;
	
	//Singleton Constructor
	private DataUploadManager(){}

	//TODO: JAVADOC
	public static DataUploadManager getInstance(){
		
		if(instance == null)
			instance = new DataUploadManager();
		
		return instance;
	}
	
	//TODO: JAVADOC
	private void addClinicEntity(Clinic c){
		
		Entity clinic = new Entity("Clinic");
		clinic.setProperty("refID", c.getRefID());
		clinic.setProperty("name", c.getName());
		clinic.setProperty("latitude", c.getLoc().getLatitude());
		clinic.setProperty("longitude", c.getLoc().getLongitude());
		clinic.setProperty("adress", c.getAddress());
		clinic.setProperty("pcode", c.getPostalCode());
		clinic.setProperty("hours", c.getHoursString());
		clinic.setProperty("email", c.getEmail());
		clinic.setProperty("phone", c.getPhone());
		clinic.setProperty("language", c.getLanguages());
	}
	
	//TODO: JAVADOC
	private void uploadToDatabase(Entity e){
		//datastore.put(e);

	}
}





