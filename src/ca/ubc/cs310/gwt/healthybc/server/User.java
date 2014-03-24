package ca.ubc.cs310.gwt.healthybc.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.google.appengine.api.datastore.Entity;

/**
 * Encapsulates a registered user of the application
 *
 */
public class User {
	private String userName;
	private byte[] passwordHash;
	private String email;
	private String salt;
	
	private static final String HASH_ALGORITHM = "SHA-256";
	private static final String ENCODING_CHARSET = "UTF-8";
	
	private static final String ENTITY_USER = "User";
	private static final String PROPERTY_HASH = "hash";
	private static final String PROPERTY_EMAIL = "email";
	private static final String PROPERTY_SALT = "salt";
	
	public static final int MAX_NAME_LENGTH = 20;
	
	private static final RemoteDataManager dataManager = new RemoteDataManager();
	
	/**
	 * construct a User and if it is new, generate a (not necessarily unique but probably unique) salt;
	 * not publicly accessible
	 * 
	 * @param name username
	 */
	private User(String name) {
		this(name, null);
	}
	
	/**
	 * construct a User and if it is new, generate a (not necessarily unique but probably unique) salt;
	 * not publicly accessible
	 * 
	 * @param name username
	 * @param email email, ignored if user already exists
	 */
	private User(String name, String email) {
		userName = name;
		
		Entity userEntity = dataManager.retrieveEntityFromDatabase(ENTITY_USER, name.toLowerCase());
		
		if (userEntity != null) {
			passwordHash = (byte[]) userEntity.getProperty(PROPERTY_HASH);
			salt = (String) userEntity.getProperty(PROPERTY_SALT);
			this.email = (String) userEntity.getProperty(PROPERTY_EMAIL);
		}
		else {
			passwordHash = null; 
			salt = StringGenerator.getInstance().generateString(20, 25, true, true);
			dataManager.uploadUserEntity(this);
			this.email = email;
		}
	}
	
	/**
	 * attempt to fetch a user with input name from data store; returning null if
	 * name does not exist or if name is illegal
	 * 
	 * @param name input name
	 * @return user with corresponding username; null if none exists or illegal name
	 */
	public static User getUser(String name) {
		if (isAcceptableName(name)) {
			return null;
		}
		
		Entity entity = dataManager.retrieveEntityFromDatabase(ENTITY_USER, name.toLowerCase());
		if (entity == null) {
			return null;
		}
		
		return new User(name);
	}
	
	/**
	 * attempt to create a new user with input name; return null if user with same
	 * name already exists or if name is illegal
	 * 
	 * @param name input name
	 * @param email email for user
	 * @return new user; null if name clash is detected or if name is illegal
	 */
	public static User createUser(String name, String email) {
		if (isAcceptableName(name)) {
			return null;
		}
		
		Entity entity = dataManager.retrieveEntityFromDatabase(ENTITY_USER, name.toLowerCase());
		if (entity != null) {
			return null;
		}
		
		return new User(name, email);
	}
	
	/**
	 * sanity-check an input string to check if it's an allowable name 
	 * 
	 * @param input input string
	 * @return true if name is acceptable according to our criteria
	 */
	public static boolean isAcceptableName(String input) {
		//TODO: formalize username criteria
		if (input == null) {
			return false;
		}
		
		//check if string is too long
		if (input.length() > MAX_NAME_LENGTH) {
			return false;
		}
		
		//check if string contains white space using regex
		Pattern pattern = Pattern.compile("\\s");
		if (pattern.matcher(input).find()) {
			return false;
		}
		
		//check if string starts with a number
		if (Character.isDigit(input.charAt(0))) {
			return false;
		}
		
		//check if string contains non-ASCII characters
		char eachChar;
		for (int i = 0; i < input.length(); ++i) {
			eachChar = input.charAt(i);
			if (Character.UnicodeBlock.of(eachChar) != Character.UnicodeBlock.BASIC_LATIN) {
				return false;
			}
		}
		
		return true;
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
		
		dataManager.uploadUserEntity(this);
		
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
	public byte[] getPasswordHash() { return passwordHash; }
	public String getSalt() { return salt; }
	public String getEmail() { return email; }
	public User setEmail(String s) {
		email = s;
		dataManager.uploadUserEntity(this);
		return this;
	}
}
