package data.messages;

import java.io.*;
import network.messages.*;
import java.util.*;

/**
 * The LibraryMessage extends Message and is used to send messages between
 * two Libraries.
 * 
 * @author Andrew Alm
 */
public class LibraryMessage extends TrackedMessage {

	private static final long serialVersionUID = 441639840954967391L;
	
	/* message types */
	public static final int MSG_MASTER = 2;
	public static final int MSG_ITERATOR = 3;
	
	/**
	 * Creates a new LibraryMessage with the given messageType. The messageType
	 * is considered immutable.
	 * 
	 * @param messageType the type of Message (see constants)
	 * 
	 * @throws IllegalArgumentException if the messageType given is not equal
	 *         to one of the static constants defined in the class.
	 */
	public LibraryMessage(int messageType) throws IllegalArgumentException {
		super(messageType);

		if(messageType < RemoteMessage.MSG_PING || messageType > LibraryMessage.MSG_ITERATOR)
			throw new IllegalArgumentException("illegal message type");
	}
	
}
