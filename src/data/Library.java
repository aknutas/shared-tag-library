package data;

import java.util.*;

/**
 * The Library contains a set of bookshelves which can be used together.  The
 * library also provides a master bookshelf off all books in the library.
 *
 * @author Andrew Alm
 */
public interface Library extends Properties, Iterable<Bookshelf> {

	/**
	 * Adds a Bookshelf to the library.
	 * 
	 * @param shelf the Bookshelf to add.
	 *
	 * @throws NullPointerException if the shelf given is null.
	 */
	public boolean addBookshelf(Bookshelf shelf) throws NullPointerException, IllegalArgumentException;

	/**
	 * Removes a Bookshelf from the library.
	 *
	 * @param shelf the Bookshelf to remove.
	 *
	 * @throws NullPointerException if the shelf given is null
	 */
	public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException;
	
	/**
	 * This method is used to get an iterator containing the names of
	 * all of the bookshelves the library contains.
	 * 
	 * @return and iterator of strings
	 */
	public Collection<String> getBookshelfNames();
	
	/**
	 * Gets the bookshelf of the given name from the library. If the
	 * library does not contain a given bookshelf then null is
	 * returned.
	 * 
	 * @param name the name of the bookshelf
	 * 
	 * @return the Bookshelf object
	 * 
	 * @throws NullPointerException if the name given is null
	 */
	public Bookshelf getBookshelf(String name) throws NullPointerException;
	
	/**
	 * Gets an iterator of bookshelves with the names given in the
	 * Collection. If a bookshelf name in the collection does not
	 * exist the Iterator will be a small size than the collection.
	 * 
	 * @param names the name of the Bookshelf
	 * 
	 * @return an Iterator of Bookshelf objects 
	 * 
	 * @throws NullPointerException if the names collection given isremoteLibrary1.getBookshelf("programming");
	 *         null
	 */
	public Iterator<Bookshelf> getBookshelf(Collection<String> names) throws NullPointerException;
	
	/**
	 * Gets the master shelf, a union of every bookshelf.
	 *
	 * @return a Bookshelf.
	 */
	public Bookshelf getMasterShelf();
}
