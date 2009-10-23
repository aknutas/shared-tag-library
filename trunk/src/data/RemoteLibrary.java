package data;
import java.util.Iterator;

import network.*;

import java.util.*;

/**
 * The RemoteLibrary class implements the Library interface and is used to
 * represent a remote library (client). 
 * 
 * @author Andrew Alm
 */
public class RemoteLibrary implements Library {

	private Connection connection;
	private Library parent;
	private List<RemoteBookshelf> openShelves;
	private long shelfId;
	
	/**
	 * Creates a new RemoteLibrary from the specified parent Library. If the
	 * parent given is not null then this instance will act as a server, else
	 * this instance will act as a client.
	 * 
	 * @param connection the connection to use
	 * @param parent the parent Library to use (can be null)
	 * 
	 * @throws NullPointerException if the connection given is null
	 */
	public RemoteLibrary(Connection connection, Library parent) throws NullPointerException {
		if(null == connection)
			throw new IllegalArgumentException("connection cannot be null");
		
		this.connection = connection;
		this.parent = parent;
		this.openShelves = new LinkedList<RemoteBookshelf>();
		this.shelfId = 0;
		//this.connection.addConnectionListener(this);
	}
	
	/**
	 * Creates a new RemoteLibrary that acts as a client instance of the library.
	 * 
	 * @param connection the connection to use.
	 */
	public RemoteLibrary(Connection connection) {
		this(connection, null);
	}

	/**
	 * Adds a bookshelf to the RemoteLibrary. This method only works if the the
	 * remote library is in 'server' mode... a remote client should not be able
	 * to add bookshelves to the server, clients import into, not vis-versa.
	 * 
	 * @param shelf the shelf to add
	 * 
	 * @return true if the operation was successful (on server), otherwise
	 *   false (client)
	 * 
	 * @throws NullPointerException if the shelf given is null
	 */
	public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
		if(shelf == null)
			throw new NullPointerException("shelf cannot be null");
		
		if(null != this.parent)
			return this.addBookshelf(shelf);
		
		return false;
	}

	/**
	 * Removes a bookshelf from the RemoteLibrary. This method only works if 
	 * the remote library is in 'server' mode... a remote client should not be
	 * able to add bookshelves to the server, clients import into, not 
	 * vis-versa.
	 * 
	 * @param shelf the shelf to remove
	 * 
	 * @return true if the operation was successful (on server), otherwise
	 *   false (client)
	 * 
	 * @throws NullPointerException if the shelf given is null
	 */
	public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		if(null != this.parent)
			return this.parent.removeBookshelf(shelf);
			
		return false;
	}		
	
	/**
	 * Gets the master bookshelf of this library. This method operates as
	 * expected for both client and server instances.
	 *
	 * @return a Bookshelf containing all books in the library.
	 */
	public Bookshelf getMasterShelf() {
		if(null != this.parent)
			return this.parent.getMasterShelf();
		
		// nm = new NetworkMessage(this.connection);
		// nm.setType(MASTER_SHELF);
		// nm.send();
		// int bid = nm.recv(); //?
		//this.connection.sendMessage(MASTER_SHELF, )
		return null;
		//return new RemoteBookshelf();
		//return new RemoteBookshelf(this.connection);
	}

	public Iterator<Bookshelf> iterator() {
		if(null != this.parent)
			return this.iterator();
		
		//NetworkMessage nm = new NetworkMessage(this.connection, NetworkMessage.LIB_ITERATE_REQUEST);
		//nm.send();
		
		//int bid = -1;
		//List<Bookshelf> shelves = new LinkedList<Bookshelf>();
		
		//do {
		//	bid = (Integer)nm.recv();
		//	shelves.add(new RemoteBookshelf(this.connection));
		//} while(bid > 0);
		
		//return shelves.iterator();
		return null;
	}

	public void recvMesg(NetworkMessage message) {
		if(null == parent)
			return;
		
		// if messsage for a shelf:
		//   for each s in remoteshelves:
		//     if s is shelf:
		//       s.recvMesg(message)
		
		// switch on msg type:
		// if LIB_MASTER_REQUEST:
		//   rbs = new RemoteBookshelf(message.getConnection(), this.parent.getMasterShelf());
		//   this.remoteShelves.add(rbs);
		//   message.addData(rbs.getId());
		//   message.send();
		//   
		// if LIB_ITERATE_REQUEST:
		//   for(Bookshelf s : this) {
		//     message.addData(s.getId());
		//   message.send();
	}
	
}
