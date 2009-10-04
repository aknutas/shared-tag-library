package data;

import java.util.*;
//import javax.jdo.annotations.PersistenceCapable;

/**
 * The VirtualBookshelf class implements the Bookshelf interface and is used
 * for in-memory, temporary Bookshelf objects. Most temporary Bookshelves will
 * be used for browsing/searching persistent bookshelves.
 * 
 * @author Andrew Alm
 */
//@PersistenceCapable
public class VirtualBookshelf implements Bookshelf {

	private String name;
	private Set<Book> bookshelf;
	private Set<Bookshelf> shelves;

	/**
	 * Creates a new Bookshelf object which contains no books.
	 */
	public VirtualBookshelf() {
		this.bookshelf = new HashSet<Book>();
		this.shelves = new HashSet<Bookshelf>();
	}

	/**
	 * Inserts a book onto the VirtualBookshelf, if the book already exists
	 * on the Bookshelf then this method does nothing.
	 * 
	 * @param book the book to add to the bookshelf.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public void insert(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");

		this.bookshelf.add(book);
	}

	/**
	 * Removes a book from the VirtualBookshelf, if and only if the book was
	 * added to this VirtualBookshelf (and not another bookshelf contained
	 * within). If the Bookshelf does not contain the given book, then this
	 * method does nothing.
	 * 
	 * @param book the book to remove from the bookshelf.
	 * 
	 * @return true if the book was removed, otherwise false.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public boolean remove(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");

		return false;
	}

	/**
	 * Determines if the virtual bookshelf is empty.
	 * 
	 * @return true if the book shelf is empty, otherwise false.
	 */
	public boolean empty() {
		for(Bookshelf shelf : this.shelves) {
			if(!shelf.empty())
				return false;
		}

		return this.bookshelf.isEmpty();
	}

	/**
	 * Determines the size of the bookshelf. A virtual bookshelf is the size of
	 * all the books it contains plus the size of all bookshelf objects it
	 * contains, the size may not reflect the exact number of books aviable.
	 * 
	 * @return the size of the shelf.
	 */
	public int size() {
		int size = 0;

		for(Bookshelf shelf : this.shelves)
			size += shelf.size();

		return (size + this.bookshelf.size());
	}

	/**
	 * Returns an Iterator of all the books on the bookshelf. This includes
	 * all eligible books on bookshelves contained within this virtual 
	 * bookshelf.
	 * 
	 * @return an Iterator of books
	 */
	public Iterator<Book> enumerate() {
		// need an iterator that traverses other iterators
		return null;
	}

	/**
	 * Joins two Bookshelves in holy matromony 'til delete do it's part.
	 * 
	 * @param shelf the bookshelf to wed.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		if(shelf instanceof VirtualBookshelf) {
			// merge
		}
		
		
		return null;
	}

	/**
	 * Computes the intersection of two bookshelf objects and returns a new
	 * Bookshelf.
	 * 
	 * @param shelf the bookshelf to intersect with.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf intersect(Bookshelf shelf) {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		if(shelf instanceof VirtualBookshelf) {
			// merge
		}

		return null;
	}

	/**
	 * Computes the difference of two bookshelfs returning a new bookshelf
	 * containing the result.
	 * 
	 * @param shelf the bookshelf to difference with.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf difference(Bookshelf shelf) {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		return null;
	}

	/**
	 * Computes the subset of books contained in a Bookshelf that match part of
	 * the given book.
	 * 
	 * @param book the model book.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public Bookshelf subset(Book book) throws IllegalArgumentException {
		return null;
	}

	
	/**
	 * Adds a bookshelf to this virtual bookshelf, if the shelf already exists
	 * in this VirtualBookshelf, then this method does nothing.
	 *
	 * @param shelf the shelf to add.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	protected void addBookshelf(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		this.shelves.add(shelf);
	}

	/**
	 * Removes a bookshelf from this virtual bookshelf, if the VirtualBookshelf
	 * already contains the shelf then this method does nothing.
	 * 
	 * @param shelf the shelf to remove from the VirtualBookshelf
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	protected void removeBookshelf(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");
		
		this.shelves.remove(shelf);
	}
	
	/**
	 * Creates a shallow-copy clone of this bookshelf.
	 * 
	 * @return a new Bookshelf identical to this.
	 */
	protected VirtualBookshelf shallowCopy() {
		return null;
	}
	
	
	public String getName() {
		return this.name;
	}

	public String setName(String name) throws IllegalArgumentException {
		String temp = this.name;
		this.name = name;
		return name;
	}


}

