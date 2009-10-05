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
   * Takes a string as a query, and returns the result as an array of objects.
   * @return       List Result list of objects.
   * @param        querystring Query, formatted by QueryBuilder.
   */
  public List query( String querystring );


  /**
   * Persists the objects in the List. Can be also be used to persist previously
   * stored objects.
   * @return       int Success status.
   * @param        objects List of objects to be persisted.
   */
  public int commit( List objects );


}
