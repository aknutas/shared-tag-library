package data;

import network.Control;
import data.messages.IteratorMessage;

/**
 * The RemoteBookIterator extends the RemoteIterator for a Book object. It is
 * used to Iterator over a Remote collection of books.
 * 
 * @author Andrew Alm
 */
public class RemoteBookIterator extends RemoteIterator<Book> {

    /**
     * Creates a new RemotebookIterator with the given network, connection and
     * id.
     * 
     * @param network
     *            the network Control object to use
     * @param connection
     *            the connection id to talk over
     * @param id
     *            the RoutedMessage id to use
     * 
     * @throws NullPointerException
     *             if the network given is null
     * @throws RemoteObjectException
     *             if the RemoteObjectCould not be created
     */
    public RemoteBookIterator(Control network, int connection, int id)
	    throws NullPointerException, RemoteObjectException {
	super(network, connection, id);
    }

    /**
     * Deserializes the first parameter of an IteratorMessage into a Book
     * object.
     * 
     * @param message
     *            the message to Deserialize
     * 
     * @return the Book message that was deserialized, or null if if there was
     *         an error
     * 
     * @throws NullPointerException
     *             if the message given is null
     * @throws IllegalArgumentException
     *             if the message given is not of the type MSG_MORE
     */
    @Override
    public Book createObject(IteratorMessage message)
	    throws NullPointerException, IllegalArgumentException {
	try {
	    if (null == message)
		throw new NullPointerException("message cannot be null");

	    if (IteratorMessage.MSG_MORE != message.getMessageType())
		throw new IllegalArgumentException("illegal message type");

	    Integer id = message.dequeParameter();
	    if (null == id)
		return null;

	    return new RemoteBook(this.network, this.connection, id.intValue());
	} catch (RemoteObjectException ex) {
	    return null;
	}
    }

}
