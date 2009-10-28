package network;

import java.util.*;

import network.messages.Message;
import network.messages.Reply;
import database.QueryBuilder;
import database.Access;

/**
 * Class ControlImpl
 * 
 * @author Antti Knutas
 * 
 */
class ControlImpl implements Control {

  private HashMap threadCollection;
  private long id;
  private int conncounter;
  private HashMap msgqueues;
  
  public ControlImpl () {
	Random random = new Random();
	id = random.nextLong();
	msgqueues = new HashMap();
	threadCollection = new HashMap();
  };

  /**
   * A command for the network interface to connect to a server address. Return value
   * is 0 for failure, or a number for connection identifier.
   * @return       int
   * @param        address Connection IP
   */
  public synchronized int connect( String address )
  {
	//Dummy data
      Random random = new Random();
      return random.nextInt();
  }


  /**
   * A command to disconnect a certain client or server connection.
   * @return       int
   * @param        connection The connection ID to be disconnected.
   */
  public synchronized int disconnect(int connection)
  {
	return 0;
  }
  
  /**
   * A command to send a data object to the specified connection, expecting a
   * reply to the reply object.
   * 
   * @return Reply The reply object.
   * @param connection
   *            The connection ID.
   */
  public synchronized Reply sendMsgGetReply(int connection, Message message)
  {
      Reply reply = new Reply();
      
      return reply;
  }

  /**
   * A command to send a data object to the specified connection, with no
   * direct reply expected.
   * 
   * @param connection
   *            The connection ID.
   */
  public void sendMsgNoReply(int connection, Message message)
  {
      
  }
  
  /**
   * A query of incoming messages. (chats, disconnections, etc)
   * 
   * @param connection
   *            The connection ID.
   */
  public synchronized Map whatsUp()
  {
      return msgqueues;
  }

}
