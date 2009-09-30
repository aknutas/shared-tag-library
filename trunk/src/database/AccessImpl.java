package database;

import java.util.ArrayList;


/**
 * Class AccessImpl
 */
class AccessImpl implements Access {

  //
  // Fields
  //

  private database.AccessImpl instance;
  
  //
  // Constructors
  //
  public AccessImpl () { };
  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  /**
   * Set the value of instance
   * @param newVar the new value of instance
   */
  private void setInstance ( database.AccessImpl newVar ) {
    instance = newVar;
  }

  /**
   * Get the value of instance
   * @return the value of instance
   */
  private database.AccessImpl getInstance ( ) {
    return instance;
  }

  //
  // Other methods
  //

  /**
   * Takes a string as a query, and returns an array of objects.
   * @return       ArrayList
   * @param        querystring
   */
  public ArrayList query( String querystring )
  {
	return null;
  }


  /**
   * Persists the objects in the list, according to the query parameters.
   * @return       int
   * @param        objects
   * @param        query
   */
  public int commit( ArrayList objects, String query )
  {
	return 0;
  }


  /**
   * @return       int
   */
  private int initialize(  )
  {
	return 0;
  }


  /**
   * @return       Database.AccessImpl
   */
  public database.AccessImpl access(  )
  {
	  return null;
  }


}
