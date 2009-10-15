package data;

import java.util.*;

import operations.*;
import database.*;

/**
 * The ClientLibrary class implements the Library interface and all of the
 * Bookshelfs that the user may use.
 * 
 * @author Andrew Alm
 */
public class ClientLibrary implements Library {

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
	public void addBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		if(!this.bookshelves.contains(shelf))
			this.bookshelves.add(shelf);
	}

	/**
	 * Removes a bookshelf from the library.
	 */
	public void removeBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		this.bookshelves.remove(shelf);
	}
	
	public Iterable<Bookshelf> enumerateBookshelves() {
		return this.bookshelves;
	}
	
	public void saveBookshelf(Bookshelf shelf) throws IllegalArgumentException, NullPointerException {
		if(null == shelf)
			throw new NullPointerException("the shelf given is null");
		
		if(!(shelf instanceof VirtualBookshelf))
			throw new IllegalArgumentException("the shelf must be a virtual bookshelf");
		
		this.database.shelfStore(shelf);
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

	/* unit tests */
	public static void main(String []args) {
		Library lib = new ClientLibrary();
		Bookshelf shelf1 = new VirtualBookshelf();
		Bookshelf shelf2 = new VirtualBookshelf();
		
		for(Bookshelf shelf : lib.enumerateBookshelves()) {
			System.out.print("[");
			
			Iterator<Book> it = shelf.enumerate();
			
			if(!it.hasNext()) {
				System.out.println(" ]");
				continue;
			}
			
			for(Book book = it.next(); it.hasNext(); book = it.next())
				System.out.print(" (" + book.getProperty("title") + "," + book.getProperty("author") + ")");
			
			System.out.println(" ]");
		}
		
		for(int i = 0; i < 50; ++i) {	
			shelf1.insert(new VirtualBook(Integer.toString(i), Integer.toString(50 - i)));
			shelf2.insert(new VirtualBook(Integer.toString(50 - i), Integer.toString(i)));
		}
		lib.addBookshelf(shelf1);
		
		lib.saveBookshelf(shelf1);
	}
	
}
