package ca.ubc.cs310.gwt.healthybc.server;
 
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
 



import com.google.appengine.api.datastore.Entity;
 
/**
 * Encapsulates a registered user of the application
 *
 */
public class User {
        private String realName;
        private String userName;
        private String password;
        private String email;
        
        private static final String HASH_ALGORITHM = "SHA-256";
        private static final String CHARSET = "ASCII";
        private static final String ENTITY_USER = "User";
        private static final String PROPERTY_REALNAME = "realname";
        private static final String PROPERTY_PASS = "password";
        private static final String PROPERTY_EMAIL = "email";
       
        private static RemoteDataManager dataManager;
       
        /**
         * construct a User and if it is new, generate a (not necessarily unique but probably unique) salt;
         * not publicly accessible
         *
         * @param name username
         */
        private User(String name) {
                this(name, null, null, null);
        }
 
        /**
         * construct a User and if it is new, generate a (not necessarily unique but probably unique) salt;
         * not publicly accessible
         *
         * @param name username
         * @param email email, ignored if user already exists
         */
        private User(String name, String email) {
                this(name, email, null, null);
        }
       
        /**
         * construct a User and if it is new, generate a (not necessarily unique but probably unique) salt;
         * not publicly accessible
         *
         * @param name username
         * @param email email, ignored if user already exists
         * @param realName real name, ignored if user already exists
         * @param password password, ignored if user already exists
         */
        private User(String name, String email, String realName, String password) {
                userName = name;
               
                Entity userEntity = getDataManager().retrieveEntityFromDatabase(ENTITY_USER, name.toLowerCase());
               
                if (userEntity != null) {
                        this.realName = (String) userEntity.getProperty(PROPERTY_REALNAME);
                        this.password = (String) userEntity.getProperty(PROPERTY_PASS);
                        this.email = (String) userEntity.getProperty(PROPERTY_EMAIL);
                }
                else {
                        this.realName = realName;
                        this.password = hash(password);
                        this.email = email;
                       
                        getDataManager().uploadUserEntity(this);
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
                Entity entity = getDataManager().retrieveEntityFromDatabase(ENTITY_USER, name.toLowerCase());
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
         * @param realName real name for user
         * @param password password for user
         * @return new user; null if username clash is detected or if username is illegal
         */
        public static User createUser(String name, String email, String realName, String password) {
              
                Entity entity = getDataManager().retrieveEntityFromDatabase(ENTITY_USER, name.toLowerCase());
                if (entity != null) {
                        return null;
                }
               
                return new User(name, email, realName, password);
        }
       
        
        private String hash(String s){
        	try {
				MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
				byte[] b = digest.digest(s.getBytes(CHARSET));
				String result = new String(b, CHARSET);
				
				return result;

			} catch (Exception e) {
				// Should never happen
				e.printStackTrace();
			}
        	
			return null;
        }
       
       
        /**
         * internal helper method to ensure data manager is initialized
         *
         * @return initialized data manager
         */
        private static RemoteDataManager getDataManager() {
                if (dataManager == null) {
                        dataManager = new RemoteDataManager();
                }
               
                return dataManager;
        }
       
        public String getRealName() { return realName; }
        public String getUserName() { return userName; }
        public String getPassword() { return password; }
        public String getEmail() { return email; }
        public User setRealName(String name) {
                realName = name;
                getDataManager().uploadUserEntity(this);
                return this;
        }
       
        public User setEmail(String s) {
                email = s;
                getDataManager().uploadUserEntity(this);
                return this;
        }

		public boolean checkPassword(String pass) {
			Logger logger = Logger.getLogger("uploadServletLogger");
			logger.log(Level.SEVERE, pass);
			logger.log(Level.WARNING, password);
			return hash(pass).equals(password);
		}
        
}