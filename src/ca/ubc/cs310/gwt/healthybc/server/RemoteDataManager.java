package ca.ubc.cs310.gwt.healthybc.server;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

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
	 * @throws Exception 
	 */		
	private void uploadToDatabase(Entity e){

		datastore.put(e);
	}

	public void retrieveAllClinics(ClinicManagerImpl manager){
		// Use class Query to assemble a query
		Query q = new Query("Clinic");

		// Use PreparedQuery interface to retrieve results
		PreparedQuery pq = datastore.prepare(q);

		for (Entity result : pq.asIterable()) {

			String refID = result.getKey().getName();
			String name = (String) result.getProperty("name");
			Double lat = Double.parseDouble((String) result.getProperty("latitude"));
			Double lon  =  Double.parseDouble((String) result.getProperty("longitude"));
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


	public void testPush(String string){

		Entity e = new Entity("Other");
		e.setProperty("info", string);

		datastore.put(e);


	}
}





