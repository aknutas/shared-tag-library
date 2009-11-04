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
public class LibraryMessage extends DataMessage {
	
	/* message types */
	public static final int CLOSE_REQUEST = 0;
	public static final int CLOSE_RESPONSE = 1;
	public static final int MASTER_REQUEST = 2;
	public static final int MASTER_RESPONSE = 3;
	public static final int ITERATE_REQUEST = 4;
	public static final int ITERATE_RESPONSE = 5;
	
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

		if(messageType < LibraryMessage.CLOSE_REQUEST || messageType > LibraryMessage.ITERATE_RESPONSE)
			throw new IllegalArgumentException("illegal message type");		
	}
	
}
