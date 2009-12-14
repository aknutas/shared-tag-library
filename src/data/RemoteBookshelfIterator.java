package data;

import network.Control;
import data.messages.IteratorMessage;

/**
 * The RemoteBookshelfIterator extends the RemoteIterator and is used to iterate
 * over a set of bookshelves. This class is used to translate remote message
 * parameters into RemoteBookshelf objects.
 * 
 * @author Andrew Alm
 */
public class RemoteBookshelfIterator extends RemoteIterator<Bookshelf> {

	/**
	 * Creates a new RemoteLibraryIterator with the given network
	 * control, connection, and id.
	 * 
	 * @param network the network Control to use
	 * @param connection the connection to communicate over
	 * @param id the id of the server
	 * 
	 * @throws NullPointerException if the network given is null
	 * @throws RemoteObjectException if the object cannot establish a
	 *         connection
	 */
	public RemoteBookshelfIterator(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
		super(network, connection, id);
	}

	/**
	 * Creates a RemoteBookshelf object from object given. The object
	 * is expected to be an Integer representing the ID of the 
	 * BookshelfResponder on the remote system.
	 * 
	 * @param the parameter to use
	 * 
	 * @throws NullPointerException if the parameter given is null
	 * @throws IllegalArgumentException if the parameter type canont
	 *         be handled by this function
	 */
	@Override
	public Bookshelf createObject(IteratorMessage message) throws NullPointerException, IllegalArgumentException {
		try {
			if(null == message)
				throw new NullPointerException("message cannot be null");
			
			if(IteratorMessage.MSG_MORE != message.getMessageType())
				throw new IllegalArgumentException("illegal message type");
			
			Integer id = message.dequeParameter();
			if(null == id)
				return null;
			
			return new RemoteBookshelf(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}

}
