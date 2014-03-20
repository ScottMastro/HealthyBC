package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs310.gwt.healthybc.client.Clinic;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class RemoteDataManager {
	private DatastoreService datastore;
	private List<Entity> clinicEntities;

	// Constructor
	public RemoteDataManager() {
		clinicEntities = new ArrayList<Entity>();
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	
	/**
	 * upload a user to the datastore, updating existing entry if it exists
	 * 
	 * @param user user to add to datastore
	 */
	public void uploadUserEntity(User user) {
		Entity e = new Entity("User", user.getUserName());
		
		e.setProperty("username", user.getUserName());
		e.setProperty("hash", user.getPasswordHash());
		e.setProperty("salt", user.getSalt());
		e.setProperty("email", user.getEmail());
		
		uploadToDatabase(e);
	}

	/**
	 * Creates a clinic Entity from a Clinic class and then uploads it to the database
	 * @param Clinc class to convert to an entity and upload
	 */	
	public void addAndUploadClinicEntity(Clinic c) {
		Entity e = this.addClinicEntity(c);
		this.uploadToDatabase(e);
	}

	/**
	 * Creates a clinic Entity from a Clinic class
	 *
	 * @param Clinc class to convert to an entity 
	 * @return Entity version of Clinic class
	 */	
	private Entity addClinicEntity(Clinic c) {
		Entity clinic = new Entity("Clinic", c.getRefID());

		clinic.setProperty("name", c.getName());
		clinic.setProperty("latitude", c.getLatitude());
		clinic.setProperty("longitude", c.getLongitude());
		clinic.setProperty("address", c.getAddress());
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
	 * @throws Exception 
	 */		
	private void uploadToDatabase(Entity e) {
		datastore.put(e);
	}

	public void retrieveAllClinics(ClinicManager manager) {
		// Use class Query to assemble a query
		Query q = new Query("Clinic");

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asIterable()) {

			String refID = result.getKey().getName();
			String name = (String) result.getProperty("name");
			Double lat = (Double) result.getProperty("latitude");
			Double lon  =  (Double) result.getProperty("longitude");
			String address = (String) result.getProperty("address");
			String pcode = (String) result.getProperty("pcode");
			String hours = (String) result.getProperty("hours");
			String email = (String) result.getProperty("email");
			String phone = (String) result.getProperty("phone");
			String language = (String) result.getProperty("language");

			manager.addNewClinic(refID, name, lat, lon, hours, address, pcode, email, phone, language);
		}
	}


	/**
	 * Retrieves an Entity from the database
	 *
	 * @param entityType to retrieve (ex. Clinic)
	 * @param key of entity to retrieve
	 * @return Entity version of clinic, returns null if could not find entity with given key
	 */		
	public Entity retrieveEntityFromDatabase(String entityType, String key) {
		Key k = KeyFactory.createKey(entityType, key);

		try { return datastore.get(k); }
		catch (EntityNotFoundException e) {
			return null;
		}
	}


	/**
	 * Pushes a simple string to datastore, used for testing
	 * TODO: remove before final demo
	 * @param string to push
	 */		
	public void testPush(String string) {
		Entity e = new Entity("Other");
		e.setProperty("info", string);

		datastore.put(e);
	}
}





