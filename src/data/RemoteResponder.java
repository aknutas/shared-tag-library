package data;

import data.messages.*;
import network.*;
import network.messages.*;

/**
 * The RemoteResponder implements the ServerResponder and is used as the base
 * object for all the responder objects. It handles all functionality the
 * RemoteObject class requires. This method must be implemented and onMessage
 * should be overridden, but called as a last resort.
 *  
 * @author Andrew Alm
 */
public abstract class RemoteResponder implements ServerResponder {

	/**
	 * This method is fired when a RemoteMessage is received that the
	 * RemoteResponder does not handle (not ping). This method should
	 * return a RemoteMessage object to send back to the client.
	 * 
	 * @param message the Message to respond to.
	 * 
	 * @return a RemoteMessage response
	 * 
	 * @throws NullPointerException if the message given is null.
	 * @throws IllegalArgumentException if the message is not of the 
	 *         correct type.
	 */
	public abstract RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException;
	
	/**
	 * This method is used to handle RemoteMessages for which the
	 * extending class cannot handle.
	 * 
	 * @param message the Message to handle
	 * 
	 * @return the response Message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         a RemoteMessage
	 */
	@Override
	public Message onMessage(Message message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof RemoteMessage))
			throw new IllegalArgumentException("illegal message type");
		
		if(RemoteMessage.MSG_PING == ((RemoteMessage)message).getMessageType())
			return new RemoteMessage(RemoteMessage.MSG_PING);
		
		return this.onRemoteMessage((RemoteMessage)message);
	}

}
