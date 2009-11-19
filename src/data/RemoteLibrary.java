package data;

import java.util.*;
import java.util.Map.Entry;

import data.messages.*;
import network.*;



/**
 * The RemoteLibrary class implements the Library interface and is used to
 * represent a remote library connection on the client side.
 * 
 * @author Andrew Alm
 */
public class RemoteLibrary extends RemoteObject implements Library {
	
	/**
	 * Creates a new RemoteLibrary object with the given connection
	 * and network.
	 * 
	 * @param connection the connection to use
	 * @param network the network control to use
	 * 
	 * @throws NullPointerException if the network given is null
	 */
	public RemoteLibrary(int connection, Control network) throws NullPointerException, RemoteObjectException {
		super(connection, network);
	}
		
	/**
	 * Gets the maste {
		
	}r Bookshelf of this RemoteLibrary object, or
	 * returns null on error.
	 * 
	 * @return the Bookshelf object, or null on error
	 */
	@Override
	public Bookshelf getMasterShelf() {
		try {
			RemoteMessage response = this.send(new LibraryMessage(LibraryMessage.MSG_MASTER), 5000);
			
			if((null == response) || !(response instanceof LibraryMessage))
				return null;
			
			Integer id = response.dequeParameter();
			if(null == id)
				return null;
			
			return new RemoteBookshelf(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}

	/**
	 * Gets an iterator of all the Bookshelf objects contained in the
	 * RemoteLibrary. If there is an error retrieving the iterator, 
	 * then null is returned.
	 * 
	 * @return an iterator of Bookshelf objects or null
	 */
	@Override
	public Iterator<Bookshelf> iterator() {
		try {
			RemoteMessage response = this.send(new LibraryMessage(LibraryMessage.MSG_ITERATOR), 5000);
			
			if((null == response) || !(response instanceof LibraryMessage))
				return null;
			
			if(LibraryMessage.MSG_ERROR == response.getMessageType())
				return null;
			
			Integer id = response.dequeParameter();
			if(null == id)
				return null;
			
			return new RemoteBookshelfIterator(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
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
	
	@Override
	public String getProperty(String name) throws NullPointerException {
		// TODO Auto-generate method stub
		return null;
	}

	@Override
	public String setProperty(String name, String value) throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<Entry<String, String>> enumerateProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookshelf getBookshelf(String name) throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Bookshelf> getBookshelf(Collection<String> names)
			throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getBookshelfNames() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
