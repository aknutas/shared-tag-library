package network;

import network.messages.*;

/**
 * The ClientMessageReceiver interface is used to allow client objects to
 * receive response messages from a remote server.  
 * 
 * @author Andrew Alm
 */
public interface ClientMessageReceiver {

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
	public void onMessageRecive(Message message);
	
}
