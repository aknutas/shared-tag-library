package network;

import network.messages.*;

/**
 * The ClientMessageReceiver interface is used to allow client objects to
 * receive response messages from a remote server.  
 * 
 * @author Andrew Alm
 */
public interface ClientResponder {

	/**
	 * This method will be fired when a Message for this given object
	 * is received.
	 * 
	 * @param message the Message which was received
	 * 
	 * @throws IllegalArgumentException if the message cannot be
	 *         processed 
	 * @throws NullPointerException if the message given is null
	 */
	public void onMessage(Message message) throws NullPointerException, IllegalArgumentException;
	
	/**
	 * This method is used to notify a client that the connection has
	 * been closed.
	 * 
	 * @param connection the connection id
	 * 
	 * @throws IllegalArgumentException if the connection given does
	 *         not match the connection id of this object
	 */
	public void onDisconnect(int connection) throws IllegalArgumentException;
	
	/**
	 * This method is used to notify a client that the connection has
	 * a loop.
	 * 
	 * @param connection the connection id
	 * 
	 * @throws IllegalArgumentException if the connection given does
	 *         not match the connection id of this object
	 */
	public void onLoop(int connection) throws IllegalArgumentException;
	
}
