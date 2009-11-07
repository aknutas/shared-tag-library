package data;

import java.util.*;
import operations.*;
import database.*;
import network.*;
import network.messages.*;

/**
 * The ClientLibrary class implements the Library interface and all of the
 * Bookshelfs that the user may use.
 * 
 * @author Andrew Alm
 */
public class ClientLibrary implements Library, ClientResponder {

	private List<Bookshelf> bookshelves;
	private QueryBuilder database;
	
	/**
	 * Creates a new ClientLibrary instance which restores all bookshelves
	 * from the database.
	 */
	public ClientLibrary() {
		this.database = new QueryBuilderImpl();		
		this.bookshelves = this.database.shelfList();
	}
	
	/**
	 * Adds a bookshelf to the library.
	 * 
	 * @param shelf the bookshelf to add to the library
	 */
	public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
	
		if(shelf instanceof VirtualBookshelf)
			database.shelfStore(shelf);

		if(!this.bookshelves.contains(shelf))
			return this.bookshelves.add(shelf);
	
		return false;
	}

	/**
	 * Removes a bookshelf from the library.
	 */
	public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		this.bookshelves.remove(shelf);
		return true;
	}
	
	/**
	 * Get the master bookshelf of the Library.  The master bookshelf is a shelf
	 * containing all of the books in the library.
	 * 
	 * @return a Bookshelf containing all books.
	 */
	public Bookshelf getMasterShelf() {
		return BookshelfOperations.union(this.bookshelves);
	}
	
	/**
	 * Returns an iterator containing all the bookshelves in this library.
	 */
	public Iterator<Bookshelf> iterator() {
		return this.bookshelves.iterator();
	}

	@Override
	public void onMessageRecive(Message message) {
		// TODO Auto-generated method stub
		
	}

	
	
}

