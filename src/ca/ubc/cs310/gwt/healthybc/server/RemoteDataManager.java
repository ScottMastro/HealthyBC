package ca.ubc.cs310.gwt.healthybc.server;
 
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import ca.ubc.cs310.gwt.healthybc.client.Clinic;
 
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
        Logger logger = Logger.getLogger("Datastore Logger");
 
        // Constructor
        public RemoteDataManager() {
                datastore = DatastoreServiceFactory.getDatastoreService();
        }
 
        /**
         * upload a user to the datastore, updating existing entry if it exists
         *
         * @param user user to add to datastore
         */
        public void uploadUserEntity(User user) {
                Entity e = new Entity("User", user.getUserName());
               
                e.setProperty("realname", user.getRealName());
                e.setProperty("username", user.getUserName());
                e.setProperty("hash", User.byteArrayToLong(user.getPasswordHash()));
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
         * Retrieves a clinic rating from the datastore
         *
         * @param refID of clinic to get rating for
         * @return ArrayList of integers where [0] = rating score and [1] = amount of ratings
         */            
        public ArrayList<Integer> getClinicRating(String refID){
 
                Key key = KeyFactory.createKey("Rating", refID);
                Entity existingRating;
                ArrayList<Integer> a = new ArrayList<Integer>();
                try {
                        existingRating = datastore.get(key);
                } catch (EntityNotFoundException e) {
 
                        logger.log(Level.WARNING, "Unable to find rating for clinic " + refID);
                        a.add(0);       a.add(0);
                        return a;
 
                }
 
                //need to do this because of the format the datastore stores the integer
                int score = new Integer(existingRating.getProperty("score").toString());
                int amount = new Integer(existingRating.getProperty("amount").toString());
               
                //just in case
                if(amount == 0)
                        amount = 1;
               
                score = score/amount;
                               
                a.add(score);
                a.add(amount);
 
                return a;
 
        }
 
        /**
         * Updates rating entity in datstore or creates new entity if no ratings currently exist
         *
         * @param refID of clinic to rate
         * @param rating to give to specific clinic
         * @return true if rating was successful, otherwise false
         */    
        public boolean submitClinicRating(String refID, int rating){
                Transaction txn = datastore.beginTransaction();
                try {
                        Key key = KeyFactory.createKey("Rating", refID);
 
                        Entity existingRating = datastore.get(key);
                        long amount = (Long) existingRating.getProperty("amount");
                        long score = (Long) existingRating.getProperty("score");
 
                        score += rating;
                        amount += 1;
                       
                        existingRating.setProperty("amount", amount);
                        existingRating.setProperty("score", score);
 
                        datastore.put(existingRating);
                        txn.commit();
 
                } catch(EntityNotFoundException e){
                        logger.log(Level.INFO, "Unable to find clinic entity " + refID + ", adding new entity");
 
                        Entity newRating = new Entity("Rating", refID);
                        newRating.setProperty("amount", 1);
                        newRating.setProperty("score", rating);
 
                        datastore.put(newRating);
                        txn.commit();  
 
                } finally {
                        if (txn.isActive()) {
                                txn.rollback();
                                return false;
                        }
                }
 
                return true;
 
        }
        
        /**
         * Updates review entity in datstore or creates new entity if no review currently exist
         *
         * @param refID of clinic to review
         * @param review to give to specific clinic (less than 500 characters)
         * @return true if rating was successful, otherwise false
         */    
        public String submitClinicReview(String refID, String review){
                Transaction txn = datastore.beginTransaction();
                String result = "";
               
                try {
                        //TODO: CHANGE KEY TO userID and refID
                        Key key = KeyFactory.createKey("Review", refID);
                        datastore.get(key);
                       
                        result = "Sorry, but you have already reviewed this clinic.";
 
                       
                       
                } catch(EntityNotFoundException e){
 
                        Entity newReview = new Entity("Review", refID);
                        newReview.setProperty("review", review);
 
                        datastore.put(newReview);
                        txn.commit();  
                       
                        result = "Thank you for the review.";
 
                } finally {
                        if (txn.isActive())
                                txn.rollback();
                       
                }
               
                return result;
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