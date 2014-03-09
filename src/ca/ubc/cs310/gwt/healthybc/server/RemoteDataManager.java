package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class RemoteDataManager {

	
	private DatastoreService datastore;
	private List<Entity> clinicEntities;
	
	// Constructor
	public RemoteDataManager(){
		
		clinicEntities = new ArrayList<Entity>();
		datastore = DatastoreServiceFactory.getDatastoreService();

	}


	/**
	 * Creates a clinic Entity from a Clinic class and then uploads it to the database
	 *
	 * @param Clinc class to convert to an entity and upload
	 */	
	public void addAndUploadClinicEntity(Clinic c){
		
		Entity e = this.addClinicEntity(c);
		this.uploadToDatabase(e);
		
	}
	
	/**
	 * Creates a clinic Entity from a Clinic class
	 *
	 * @param Clinc class to convert to an entity 
	 * @return Entity version of Clinic class
	 */	
	private Entity addClinicEntity(Clinic c){
		
		Entity clinic = new Entity("Clinic", c.getRefID());
		
		clinic.setProperty("name", c.getName());
		clinic.setProperty("latitude", c.getLoc().getLatitude());
		clinic.setProperty("longitude", c.getLoc().getLongitude());
		clinic.setProperty("adress", c.getAddress());
		clinic.setProperty("pcode", c.getPostalCode());
		clinic.setProperty("hours", c.getHoursString());
		clinic.setProperty("email", c.getEmail());
		clinic.setProperty("phone", c.getPhone());
		clinic.setProperty("language", c.getLanguages());
		
		clinicEntities.add(clinic);
		return clinic;
	}
	
	/**
	 * Uploads an Entity to the database
	 *
	 * @param Clinc class to convert to an entity 
	 */		
	private void uploadToDatabase(Entity e){
		//TODO: Throws error?
		datastore.put(e);

	}
	
	/**
	 * Retrieves an Entity from the database
	 *
	 * @param refID of clinic to retrieve
	 * @return Entity version of clinic, returns null if could not find entity with given key
	 */		
	public Entity retrieveFromDatabase(String refID){
		
		Key key = KeyFactory.createKey("Clinic", refID);
		
		try { return datastore.get(key);}
		catch (EntityNotFoundException e) {
			return null;
		}

	}
}





