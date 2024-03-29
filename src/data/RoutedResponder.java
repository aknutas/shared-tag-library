package data;

import java.util.HashMap;
import java.util.Map;

import data.messages.RemoteMessage;
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
     * Creates a new RoutedResponder object. Each RoutedResponder is put into a
     * static routing table and given a unique id.
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

    /**
     * This method is used to route a message to the proper RoutedResponder
     * object, returning the ResponseMessage response generated by the
     * responder.
     * 
     * @param message
     *            the RoutedMessage to respond too.
     * 
     * @return the RoutedMessage response message
     * 
     * @throws NullPointerException
     *             if the message given is null
     */
    public static RoutedMessage routeMessage(RoutedMessage message)
	    throws NullPointerException {
	if (null == message)
	    throw new NullPointerException("message cannot be null");

	RoutedResponder responder = RoutedResponder.responders
		.get(((RoutedMessage) message).getID());
	if (null == responder)
	    return new RoutedMessage(RoutedMessage.MSG_ERROR, message.getID());

	return responder.onRoutedMessage(message);
    }

    /**
     * Puts the given RemoteRouter into the responder map.
     * 
     * @param responder
     *            the responder to put into the responder map.
     * 
     * @return the id given to the responder.
     * 
     * @throws NullPointerException
     */
    public static synchronized int putResponder(RoutedResponder responder)
	    throws NullPointerException {
	if (null == responder)
	    throw new NullPointerException("responder cannot be null");

	int id = RoutedResponder.nextID;
	RoutedResponder.responders.put(id, responder);
	RoutedResponder.nextID += 1;

	return id;
    }

    /**
     * This throws an IllegalArgumentException, it is required due to
     * Inheritance, however is never called.
     * 
     * @param message
     *            the RemoteMessage to respond to
     * 
     * @return does not return
     * 
     * @throws IllegalArgumentException
     *             if the method is called
     */
    public RemoteMessage onRemoteMessage(RemoteMessage message)
	    throws IllegalArgumentException {
	throw new IllegalArgumentException();
    }

    /**
     * This method is called to handle a message once the proper responder has
     * been found.
     * 
     * @param message
     *            the RoutedMessage to respond to
     * 
     * @return a RoutedMessage response object
     * 
     * @throws NullPointerException
     *             if the message given is null
     * @throws IllegalArgumentException
     *             if the message given is of an invalid type.
     */
    public abstract RoutedMessage onRoutedMessage(RoutedMessage message)
	    throws NullPointerException, IllegalArgumentException;

}
