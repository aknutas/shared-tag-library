package data;

import network.Control;

/**
 * The RemoteLibraryIterator extends the RemoteIterator and is used to iterate
 * over a set of bookshelves. This class is used to translate remote message
 * parameters into RemoteBookshelf objects.
 * 
 * @author Andrew Alm
 */
public class RemoteLibraryIterator extends RemoteIterator<Bookshelf> {

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
	public RemoteLibraryIterator(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
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
	public Bookshelf createObject(Object parameter) throws NullPointerException, IllegalArgumentException {
		try {
			if(null == parameter)
				throw new NullPointerException("parameter cannot be null");
			
			if(!(parameter instanceof Integer))
				throw new NullPointerException("illegal parameter type");
			
			return new RemoteBookshelf(this.network, this.connection, ((Integer)parameter).intValue());
		}
		catch(Exception ex) { //TODO CHANGE ME TO REMOTEOBJECTEXCEPTION
			return null;
		}
	}

}
