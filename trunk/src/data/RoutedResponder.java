package data;

import java.util.HashMap;
import java.util.Map;

import data.messages.*;

/**
 * The RemoteRouter class extends the RemoteResponder and implements a static
 * structure for routing RoutedMessages to the proper responder.
 * 
 * @author Andrew Alm
 */
public abstract class RoutedResponder extends RemoteResponder {

	private static Map<Integer, RoutedResponder> responders;
	private static int nextID;
	
	static {
		RoutedResponder.responders = new HashMap<Integer, RoutedResponder>();
		RoutedResponder.nextID = 0;
	}
	
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof RoutedMessage))
			throw new IllegalArgumentException("illegal message type");
		
		RoutedResponder responder = RoutedResponder.responders.get(((RoutedMessage)message).getID());
		if(null == responder)
			return new RemoteMessage(RemoteMessage.MSG_ERROR);
		
		return responder.onRoutedMessage((RoutedMessage)message);
	}
	
	/**
	 * This method is called to handle a message once the proper
	 * responder has been found.
	 * 
	 * @param message the RoutedMessage to respond to
	 * 
	 * @return a RoutedMessage response object
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is of an
	 *         invalid type.
	 */
	public abstract RoutedMessage onRoutedMessage(RoutedMessage message) throws NullPointerException, IllegalArgumentException;
	
	/**
	 * Puts the given RemoteRouter into the responder map.
	 * 
	 * @param responder the responder to put into the responder map.
	 * 
	 * @return the id given to the responder.
	 * 
	 * @throws NullPointerException
	 */
	public static synchronized int putResponder(RoutedResponder responder) throws NullPointerException {
		if(null == responder)
			throw new NullPointerException("responder cannot be null");
		
		int id = RoutedResponder.nextID;
		RoutedResponder.responders.put(id, responder);
		RoutedResponder.nextID += 1;
		
		return id;
	}
	
}
