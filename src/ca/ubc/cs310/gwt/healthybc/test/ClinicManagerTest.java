package ca.ubc.cs310.gwt.healthybc.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs310.gwt.healthybc.server.ClinicManager;

public class ClinicManagerTest {

	private ClinicManager cm;
	
	@Before
	public void setup() {
		cm = ClinicManager.getInstance();
	}
	
	/**
	 * set up dummy clinics to test ClinicManagerImpl functionalities
	 */
	@Test
	public void testClinicManagerImplementation() {
		String refID = "123";
		String name = "False Clinic";
		double lat = 123.23;
		double lon = 321.32;
		String hours = "6AM-8PM weekdays";
		String address = "123 Whitmore Street";
		String pcode = "V1A 2B3";
		String email = "contact@falseclinic.ca";
		String phone = "1234567";
		String languages = "English/French/German";
		
		//add a normal clinic
		boolean testValue = cm.addNewClinic(refID, name, lat, lon, hours, address, pcode, email, phone, languages);
		assertTrue(testValue);
		
		//add a duplicate
		testValue = cm.addNewClinic(refID, name, lat, lon, hours, address, pcode, email, phone, languages);
		assertFalse(testValue);
		
		//try adding with null refID
		testValue = cm.addNewClinic(null, name, lat, lon, hours, address, pcode, email, phone, languages);
		assertFalse(testValue);
		
		//try adding with empty refID
		testValue = cm.addNewClinic("", name, lat, lon, hours, address, pcode, email, phone, languages);
		assertFalse(testValue);
		
		try {
			assertTrue(cm.removeClinic(refID));
			
			//try removing  with null refID
			assertFalse(cm.removeClinic(null));
			
			//try removing with empty refID
			assertFalse(cm.removeClinic(""));
		}
		catch (RuntimeException re) {
			re.printStackTrace();
			fail("See printed stack trace.");
		}
	}
}
