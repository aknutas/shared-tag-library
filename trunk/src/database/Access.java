package database;

import java.util.ArrayList;
import java.util.List;


/**
 * Interface Access
 * The singleton object that both manages and queues access for the database.
 * 
 * It is synchronized for relatively safe multithreading.
 * 
 * Instance of this is protected with AccessImpl,
 * use AccessImpl.getInstance() to access.
 * 
 * @author Antti Knutas
 * 
 */
public interface Access {

  //
  // Fields
  //
	

  
  //
  // Methods
  //
  
  /**
   * Orders the persistance manager to close connections and finish.
   */
  public void finalize(  );


  /**
   * Takes a string as a query, and returns the result as an array of objects.
   * @return       List Result list of objects.
   * @param        querystring Query, formatted by QueryBuilder.
   */
  @SuppressWarnings("unchecked")
public List query( String querystring );

	/**
	 * Persists a single object.
	 * @return       int Success status.
	 * @param        object The persisted object.
	 */
  public int commitOne( Object object );
  
	/**
	 * Removes a single object from the persistence graph.
	 * @return       int Success status.
	 * @param        object The persisted object.
	 */
public int removeOne( Object object );

	/**
	 * Persists the objects in the list.
	 * @return       int Success status.
	 * @param        objects List of objects to be persisted.
	 */
  public int commit( List objects );


}
