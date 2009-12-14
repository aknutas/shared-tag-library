package data;

import java.util.HashMap;
import java.util.Map;

import data.messages.RoutedMessage;

/**
 * The RemoteRouter class extends the RemoteResponder and implements a static
 * structure for routing RoutedMessages to the proper responder.
 * 
 * @author Andrew Alm
 */
public abstract class RoutedResponder extends RemoteResponder {

	private static Map<Integer, RoutedResponder> responders;
	private static int nextID;
	
	private final int id;
	
	static {
		RoutedResponder.responders = new HashMap<Integer, RoutedResponder>();
		RoutedResponder.nextID = 0;
	}
	
	/**
	 * Creates a new RoutedResponder object. Each RoutedResponder is
	 * put into a static routing table and given a unique id.
	 */
	public RoutedResponder() {
		this.id = RoutedResponder.putResponder(this);
	}
	
	/**
	 * Gets the ID of this RoutedResponder.
	 * 
	 * @return the ID
	 */
	public int getID() {
		return this.id;
	}
	
//	/**
//	 * This method is called when a RemoteMessage is received. This
//	 * method routes the RemoteMesssage to the proper responder.
//	 * 
//	 * @param message the Message to respond to
//	 * 
//	 * @throws NullPointerException if the message given is null
//	 * @throws IllegalArgumentException if the message type cannot
//	 *         be processed by this handler. 
//	 */
//	public  RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
//		if(null == message)
//			throw new NullPointerException("message cannot be null");
//		
//		if(!(message instanceof RoutedMessage))
//			throw new IllegalArgumentException("illegal message type");
//		
//		RoutedResponder responder = RoutedResponder.responders.get(((RoutedMessage)message).getID());
//		if(null == responder)
//			return new RemoteMessage(RemoteMessage.MSG_ERROR);
//		
//		return responder.onRoutedMessage((RoutedMessage)message);
//	}
	
	public static RoutedMessage routeMessage(RoutedMessage message) {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		RoutedResponder responder = RoutedResponder.responders.get(((RoutedMessage)message).getID());
		if(null == responder)
			return new RoutedMessage(RoutedMessage.MSG_ERROR, message.getID());
		
		return responder.onRoutedMessage(message);
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
