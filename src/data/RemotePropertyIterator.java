package data;

import java.util.Map.Entry;

import network.Control;
import data.messages.IteratorMessage;

/**
 * The RemotePropertyItertor class extends the RemoteIterator class for
 * properties (Entry<String, String>).
 * 
 * @author Andrew Alm
 */
public class RemotePropertyIterator extends
	RemoteIterator<Entry<String, String>> {

    /**
     * Creates a new RemotePropertyIterator from the given network, connection
     * and id.
     * 
     * @param network
     *            the network control object to use
     * @param connection
     *            the connection id to send with
     * @param id
     *            the id for the RoutedMessage
     * 
     * @throws NullPointerException
     *             if the network given is null
     * @throws RemoteObjectException
     *             if the connection failed
     */
    public RemotePropertyIterator(Control network, int connection, int id)
	    throws NullPointerException, RemoteObjectException {
	super(network, connection, id);
    }

    /**
     * Deserializes the IteratorMessage object passed in.
     * 
     * @param message
     *            the IteratorMessage passed in
     * 
     * @return the Entry representing the tag
     * 
     * @throws NullPointerException
     *             if the message given is null
     */
    @Override
    public Entry<String, String> createObject(IteratorMessage message)
	    throws NullPointerException, IllegalArgumentException {
	if (null == message)
	    throw new NullPointerException("message cannot be null");

	return message.dequeParameter();
    }

}
