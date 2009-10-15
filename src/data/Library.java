package data;

/**
 * The Library contains a set of bookshelfs which can be used together.  The
 * library also provides a master bookshelf off all books in the library.
 *
 * @author Andrew Alm
 */
public interface Library {

	/**
	 * Adds a Bookshelf to the library.
	 * 
	 * @param shelf the Bookshelf to add.
	 *
	 * @throws NullPointerException if the shelf given is null.
	 */
	void addBookshelf(Bookshelf shelf) throws NullPointerException;

	/**
	 * Removes a Bookshelf from the library.
	 *
	 * @param shelf the Bookshelf to remove.
	 *
	 * @throws NullPointerException if the shelf given is null
	 */
	void removeBookshelf(Bookshelf shelf) throws NullPointerException;

	public void saveBookshelf(Bookshelf shelf) throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Returns an iterable object containing all of the bookshelves in the
	 * library.
	 * 
	 * @return an Iterable object of bookshelves.
	 */
	Iterable<Bookshelf> enumerateBookshelves();
	
	/**
	 * Gets the master shelf, a union of every bookshelf.
	 *
	 * @return a Bookshelf.
	 */
	Bookshelf getMasterShelf();
}
