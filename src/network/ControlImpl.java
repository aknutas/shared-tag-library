package network;

import java.util.*;


/**
 * Class ControlImpl
 * 
 * @author Antti Knutas
 * 
 */
class ControlImpl implements Control {

  //
  // Fields
  //

  private network.ControlImpl instance;
  private ArrayList threadCollection;
  
  //
  // Constructors
  //
  public ControlImpl () { };
  
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
  private void setInstance ( network.ControlImpl newVar ) {
    instance = newVar;
  }

  /**
   * Get the value of instance
   * @return the value of instance
   */
  private network.ControlImpl getInstance ( ) {
    return instance;
  }

  /**
   * Set the value of threadCollection
   * @param newVar the new value of threadCollection
   */
  private void setThreadCollection ( ArrayList newVar ) {
    threadCollection = newVar;
  }

  /**
   * Get the value of threadCollection
   * @return the value of threadCollection
   */
  private ArrayList getThreadCollection ( ) {
    return threadCollection;
  }

  //
  // Other methods
  //

  /**
   * A command for the network interface to connect to a server address. Return value
   * is 0 for failure, or a number for connection identifier.
   * @return       int
   * @param        address Connection IP
   */
  public int connect( String address )
  {
	return 0;
  }


  /**
   * A command to disconnect a certain client or server connection.
   * @return       int
   * @param        connection The connection ID to be disconnected.
   */
  public int disconnect( int connection )
  {
	return 0;
  }


  /**
   * Use this method to get an instance of the singleton.
   * @return       Database.Access
   */
  public database.Access access(  )
  {
	return null;
  }


}
