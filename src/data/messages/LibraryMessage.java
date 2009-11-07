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
public class LibraryMessage extends RemoteMessage {
	
	private static final long serialVersionUID = 1L;
	
	/* message types */
	public static final int MSG_MORE = 1;
	public static final int MSG_ITERATOR = 2;
	public static final int MSG_MASTER = 3;
	
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

		if(messageType < RemoteMessage.MSG_HELLO || messageType > LibraryMessage.MSG_MASTER)
			throw new IllegalArgumentException("illegal message type");
	}
	
}
