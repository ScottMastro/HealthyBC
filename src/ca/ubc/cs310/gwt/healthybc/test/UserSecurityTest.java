package ca.ubc.cs310.gwt.healthybc.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs310.gwt.healthybc.server.User;

public class UserSecurityTest {

	User user;
	
	@Before
	public void setup() {
		user = User.createUser("wah123", "wah123@wah.com", "Wah");
	}
	
	@Test
	public void testUser() {
		//TODO: Should not be able to add a User with the same username
		
		String password = "12345";
		assertTrue(user.setPassword(password));
		
		//should not be able to set same password 
		assertFalse(user.setPassword(password));
		
		//should not be able to set null and empty password
		password = null;
		assertFalse(user.setPassword(password));
		
		password = " ";
		assertFalse(user.setPassword(password));
		
		password = "";
		assertFalse(user.setPassword(password));
		
		//password should match
		password = "12345";
		assertTrue(user.checkPassword(password));
		
		//these passwords should not match
		password = "12345 ";
		assertFalse(user.checkPassword(password));

		password = "abcde";
		assertFalse(user.checkPassword(password));
		
		password = "";
		assertFalse(user.checkPassword(password));
		
		password = " ";
		assertFalse(user.checkPassword(password));
		
		password = null;
		assertFalse(user.checkPassword(password));
	}
}
