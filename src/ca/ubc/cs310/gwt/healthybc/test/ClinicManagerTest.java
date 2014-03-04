package ca.ubc.cs310.gwt.healthybc.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs310.gwt.healthybc.client.ClinicManager;
import ca.ubc.cs310.gwt.healthybc.server.ClinicManagerImpl;

public class ClinicManagerTest {

	private ClinicManager cm;
	
	@Before
	public void setup() {
		cm = new ClinicManagerImpl();
	}
	
	/**
	 * set up a dummy clinic to test ClinicManagerImpl functionalities
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
		
		cm.addNewClinic(refID, name, lat, lon, hours, address, pcode, email, phone, languages);
		
		try {
			assertTrue(cm.removeClinic(refID));
		}
		catch (RuntimeException re) {
			fail("RuntimeException happened. Stacktrace as follows.");
			re.printStackTrace();
		}
	}
}
