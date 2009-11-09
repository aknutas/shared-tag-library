package data.messages;

import java.io.*;
import java.util.*;
import network.messages.*;

/**
 * The RemoteMessage class is an abstract class which extends Message and
 * provides all the required functionality for describing messages sent by the
 * data structures.  This class is abstract but doesn't not have any abstract
 * methods, it is expected that the extending class will define message types 
 * as static constants (though technically not required).
 * 
 * @author Andrew Alm
 */
public class RemoteMessage extends Message {

	private static final long serialVersionUID = 2649706187934307008L;
	
	public static final int MSG_PING = 0;
	public static final int MSG_ERROR = 1;
	
	
	
	/* private data */
	private final int messageType;
	private final Queue<Serializable> parameters;
	
	/**
	 * Creates a new RemoteMessage with the given message type.
	 * 
	 * @param messageType
	 */
	public RemoteMessage(int messageType) {
		this.messageType = messageType;
		this.parameters = new LinkedList<Serializable>();
	}
	
	/**
	 * Gets the message type of this LibraryMessage.
	 * 
	 * @return an int representing the message type.
	 */
	public int getMessageType() {
		return this.messageType;
	}
	
	/**
	 * Adds a parameter to the parameter queue, when the message is 
	 * received parameters can be dequeued in the same order they 
	 * were put on by the sender. 
	 * 
	 * @param parameter the parameter to add
	 * 
	 * @return true if the parameter was added, otherwise false.
	 */
	public boolean queueParameter(Serializable parameter) {
		return this.parameters.offer(parameter);
	}
	
	/**
	 * Retrieves the first parameter queued returning it. If the
	 * parameter queue is empty then null is returned.
	 * 
	 * @return the next parameter added, or null if no more exist
	 */
	@SuppressWarnings("unchecked")
	public <T> T dequeParameter() {
		try { /* not sure why this is unchecked... */
			return (T)this.parameters.poll();
		}
		catch(RuntimeException ex) {
			return null;
		}
	}
	
}
