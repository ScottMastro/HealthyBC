package ca.ubc.cs310.gwt.healthybc.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Encapsulates a registered user of the application
 *
 */
public class User {
	private String userName;
	private byte[] passwordHash;
	private String salt;
	
	private static final String HASH_ALGORITHM = "SHA-256";
	private static final String ENCODING_CHARSET = "UTF-8";
	
	/**
	 * construct a User and generate a (not necessarily unique but probably unique) salt
	 * 
	 * @param name username
	 */
	public User(String name) {
		userName = name;
		
		//TODO: try to load password hash from database
		passwordHash = null;
		
		//TODO: try to load salt from database, and only generate new salt if user doesn't exist in database 
		salt = StringGenerator.getInstance().generateString(20, 25, true, true);
		//TODO: store salt in database
	}
	
	/**
	 * hash the password with user's salt and set it
	 * 
	 * @param password input password, cannot be empty or null
	 * @return true if password is set, false if password is not set
	 */
	public boolean setPassword(String password) {
		//basic sanity tests
		if (password == null || password.trim().isEmpty()) {
			return false;
		}
		
		byte[] hashedResult = attemptHash(getDigest(), password + salt);
		
		if (Arrays.equals(hashedResult, passwordHash)) {
			//this means newly entered password is the same as the old password
			return false;
		}
		
		passwordHash = hashedResult;
		
		//TODO: store new hash in database
		
		return true;
	}
	
	/**
	 * helper method to get MessageDigest algorithm object and deal with checked exception
	 * it may throw
	 * 
	 * @return a digest object
	 * @throws RuntimeException(wrapping NoSuchAlgorithmException) if default hash algorithm is
	 * unsupported by the JVM
	 */
	private MessageDigest getDigest() {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(HASH_ALGORITHM);
		}
		catch (NoSuchAlgorithmException e) {
			//should not be able to happen, but if it did...
			throw new RuntimeException(e);
		}
		
		return digest;
	}
	
	/**
	 * helper method to hash an input string in UTF-8 and deal with checked exception
	 * the algorithm object may throw
	 * 
	 * @param algorithm the MessageDigest algorithm object to use
	 * @param input the input string
	 * @return hash as array of signed char
	 * @throws RuntimeException(wrapping UnsupportedEncodingException) if default encoding charset is ever
	 * unsupported by the JVM
	 */
	private byte[] attemptHash(MessageDigest algorithm, String input) {
		byte[] result = null;
		try {
			result = algorithm.digest(input.getBytes(ENCODING_CHARSET));
		} catch (UnsupportedEncodingException e) {
			//also should not be able to happen, but if it did...
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	/**
	 * check the input password is correct 
	 * 
	 * @param password input password
	 * @return true if password is correct
	 */
	public boolean checkPassword(String password) {
		//basic time-saving tests
		if (password == null || password.trim().isEmpty()) {
			return false;
		}
		
		byte[] hashedResult = attemptHash(getDigest(), password + salt);
		
		return Arrays.equals(hashedResult, passwordHash);
	}
	
	public String getUserName() { return userName; }
}
