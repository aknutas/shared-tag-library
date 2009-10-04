package network;

import java.util.*;
import database.QueryBuilder;
import database.Access;

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
  private ControlImpl () {
	  
  };
  
  //
  // Methods
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
  public network.Control access(  )
  {
		if (instance==null)
		{
			instance = new ControlImpl();
		}
		return instance;
  }


}
