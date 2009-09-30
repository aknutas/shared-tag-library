package network;

import java.util.*;
import database.QueryBuilder;
import database.Access;


/**
 * Interface Control
 * This class allows for control and polling of the networking threads.
 */
public interface Control {

  //
  // Fields
  //

  
  //
  // Methods
  //


  //
  // Accessor methods
  //

  //
  // Other methods
  //

  /**
   * A command for the network interface to connect to a server address. Return value
   * is 0 for failure, or a number for connection identifier.
   * @return       int
   * @param        address Connection IP
   */
  public int connect( String address );


  /**
   * A command to disconnect a certain client or server connection.
   * @return       int
   * @param        connection The connection ID to be disconnected.
   */
  public int disconnect( int connection );


  /**
   * Use this method to get an instance of the singleton.
   * @return       Database.Access
   */
  public database.Access access(  );


}
