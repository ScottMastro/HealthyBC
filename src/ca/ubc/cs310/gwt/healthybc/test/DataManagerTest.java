package ca.ubc.cs310.gwt.healthybc.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;



import ca.ubc.cs310.gwt.healthybc.server.Clinic;
import ca.ubc.cs310.gwt.healthybc.server.ClinicHours;
import ca.ubc.cs310.gwt.healthybc.server.RemoteDataManager;
import ca.ubc.cs310.gwt.healthybc.server.Location;

public class DataManagerTest {

	private Clinic c;
	private RemoteDataManager um;
	private final LocalServiceTestHelper helper =
		        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }
	
	@Before
	public void setup() {
		um = new RemoteDataManager();
	}
	
	@Test
	public void testClinicManagerImplementation() {
		
		String refID = "123";
		String name = "False Clinic";
		double lat = 123.23;
		double lon = 321.32;
		Location loc = new Location(lat, lon);
		ClinicHours hours = new ClinicHours("6AM-8PM weekdays");
		String address = "123 Whitmore Street";
		String pcode = "V1A 2B3";
		String email = "contact@falseclinic.ca";
		String phone = "1234567";
		String languages = "English/French/German";
		
		//create test clinic
		c = new Clinic(refID, name, hours, loc, address, pcode, email, phone, languages);
		
		//try to upload clinic
		um.addAndUploadClinicEntity(c);
		
		//try to retrieve clinic
		Entity e = um.retrieveFromDatabase(refID);
		assertFalse(e == null);
		assertTrue(e.getKey().equals(KeyFactory.createKey("Clinic", "123")));
	}
	
}

