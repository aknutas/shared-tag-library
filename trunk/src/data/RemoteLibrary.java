package data;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import network.Control;
import data.messages.LibraryMessage;
import data.messages.RemoteMessage;

/**
 * The RemoteLibrary class implements the Library interface and is used to
 * represent a remote library connection on the client side.
 * 
 * @author Andrew Alm
 */
public class RemoteLibrary extends RemoteObject implements Library {

    /**
     * Creates a new RemoteLibrary object with the given connection and network.
     * 
     * @param connection
     *            the connection to use
     * @param network
     *            the network control to use
     * 
     * @throws NullPointerException
     *             if the network given is null
     */
    public RemoteLibrary(int connection, Control network)
	    throws NullPointerException, RemoteObjectException {
	super(connection, network);
    }

    /**
     * Gets the master Bookshelf of this RemoteLibrary object, or returns null
     * on error.
     * 
     * @return the Bookshelf object, or null on error
     */
    @Override
    public Bookshelf getMasterShelf() {
	try {
	    RemoteMessage response = this.send(new LibraryMessage(
		    LibraryMessage.MSG_MASTER), 5000);

	    if ((null == response) || !(response instanceof LibraryMessage))
		return null;

	    Integer id = response.dequeParameter();
	    if (null == id)
		return null;

	    return new RemoteBookshelf(this.network, this.connection, id
		    .intValue());
	} catch (RemoteObjectException ex) {
	    return null;
	}
    }

    /**
     * Gets an iterator of all the Bookshelf objects contained in the
     * RemoteLibrary. If there is an error retrieving the iterator, then null is
     * returned.
     * 
     * @return an iterator of Bookshelf objects or null
     */
    @Override
    public Iterator<Bookshelf> iterator() {
	try {
	    RemoteMessage response = this.send(new LibraryMessage(
		    LibraryMessage.MSG_ITERATOR), 5000);

	    if ((null == response) || !(response instanceof LibraryMessage))
		return null;

	    if (LibraryMessage.MSG_ERROR == response.getMessageType())
		return null;

	    Integer id = response.dequeParameter();
	    if (null == id)
		return null;

	    return new RemoteBookshelfIterator(this.network, this.connection,
		    id.intValue());
	} catch (RemoteObjectException ex) {
	    return null;
	}
    }

    @Override
    public String getProperty(String name) throws NullPointerException {
	// TODO: implement (copy some code)
	return null;
    }

    @Override
    public Iterable<Entry<String, String>> enumerateProperties() {
	// TODO: implement (copy some code)
	return null;
    }

    /**
     * Gets the bookshelf of the given name from the library. If the library
     * does not contain a given bookshelf then null is returned.
     * 
     * @param name
     *            the name of the bookshelf
     * 
     * @return the Bookshelf object
     * 
     * @throws NullPointerException
     *             if the name given is null
     */
    @Override
    public Bookshelf getBookshelf(String name) throws NullPointerException {
	if (null == name)
	    throw new NullPointerException("name cannot be null");

	try {
	    RemoteMessage message = new LibraryMessage(
		    LibraryMessage.MSG_BOOKSHELF);
	    message.queueParameter(name);
	    message = this.send(message, 5000);

	    if (!(message instanceof LibraryMessage)
		    || LibraryMessage.MSG_BOOKSHELF != message.getMessageType())
		return null;

	    int bookshelfID = message.dequeParameter();
	    return new RemoteBookshelf(this.network, this.connection,
		    bookshelfID);
	} catch (Exception ex) {
	    return null;
	}
    }

    /**
     * Gets an iterator of bookshelves with the names given in the Collection.
     * If a bookshelf name in the collection does not exist the Iterator will be
     * a small size than the collection.
     * 
     * @param names
     *            the name of the Bookshelf
     * 
     * @return an Iterator of Bookshelf objects
     * 
     * @throws NullPointerException
     *             if the names collection given
     *             isremoteLibrary1.getBookshelf("programming"); null
     */
    @Override
    public Iterable<Bookshelf> getBookshelf(Collection<String> names)
	    throws NullPointerException {
	if (null == names)
	    throw new NullPointerException("names cannot be null");

	RemoteMessage message = new LibraryMessage(
		LibraryMessage.MSG_BOOKSHELVES);

	for (String name : names)
	    message.queueParameter(name);

	message = this.send(message, 5000);

	if (!(message instanceof LibraryMessage)
		|| LibraryMessage.MSG_BOOKSHELVES != message.getMessageType())
	    return null;

	Integer id = message.dequeParameter();
	if (null == id)
	    return null;

	/* create Iterable object to return (delayed execution) */
	final Control network = this.network;
	final int connection = this.connection;
	final int iteratorID = id;

	return new Iterable<Bookshelf>() {
	    public Iterator<Bookshelf> iterator() {
		try {
		    return new RemoteBookshelfIterator(network, connection,
			    iteratorID);
		} catch (RemoteObjectException ex) {
		    return null;
		}
	    }
	};
    }

    /**
     * This method is used to get an iterator containing the names of all of the
     * bookshelves the library contains.
     * 
     * @return and iterator of strings
     */
    @Override
    public Iterable<String> getBookshelfNames() {
	try {
	    RemoteMessage message = new LibraryMessage(
		    LibraryMessage.MSG_BOOKSHELF_NAMES);
	    message = this.send(message, 5000);

	    if (!(message instanceof LibraryMessage)
		    || LibraryMessage.MSG_BOOKSHELF_NAMES != message
			    .getMessageType())
		return null;

	    /* create collection of strings (names) from iterator */
	    Collection<String> names = new LinkedList<String>();
	    String name = message.dequeParameter();

	    while (name != null) {
		names.add(name);
		name = message.dequeParameter();
	    }

	    return names;
	} catch (Exception ex) {
	    return null;
	}
    }

    /**
     * Operation not permitted.
     */
    @Override
    public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
	return false;
    }

    /**
     * Operation not permitted.
     */
    @Override
    public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
	return false;
    }

    /**
     * Operation not permitted.
     */
    public String setProperty(String name, String value)
	    throws NullPointerException {
	return null;
    }

}
