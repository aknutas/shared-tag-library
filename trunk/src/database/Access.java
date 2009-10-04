package database;

import java.util.ArrayList;
import java.util.List;


/**
 * Interface Access
 * The singleton object that both manages and queues access for the database.
 * 
 * It is synchronized for relatively safe multithreading.
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
   * Returns the singleton object reference.
   * @return       Database.Access
   */
  public database.Access access(  );


  /**
   * Takes a string as a query, and returns an array of objects.
   * @return       ArrayList
   * @param        querystring
   */
  public List query( String querystring );


  /**
   * Persists the objects in the list, according to the query parameters.
   * @return       int
   * @param        objects
   * @param        query
   */
  public int commit( List objects, String query );


}
