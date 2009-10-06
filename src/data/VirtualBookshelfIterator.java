package data;

import java.util.*;

/**
 * The VirtualBookshelfIterator implements the Iterator interface for a Book
 * object and is able to iterate over every book in a virtual bookshelf.
 *
 * @author Andrew Alm
 */
public class VirtualBookshelfIterator implements Iterator<Book> {

	private Iterator<Bookshelf> shelves;
	private Iterator<Book> books;
	private Iterator<Book> current;

	/**
	 * Creates a new VirtualBookshelfIterator from the given VirtualBookshelf.
	 *
	 * @param shelf the VirtualBookshelf to iterate over.
	 *
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public VirtualBookshelfIterator(Iterator<Bookshelf> shelves, Iterator<Book> books) throws IllegalArgumentException {
		if(null == shelves || null == books)
			throw new IllegalArgumentException("the shelves or books iterator is null");

		this.shelves = shelves;
		this.books = books;
		this.current = books;
	}

	/**
	 * Determines whether there is another Book in the iterator.
	 *
	 * @return true if there is another book, otherwise false
	 */
	public boolean hasNext() {
		if(this.current.hasNext())
			return true;
		else if(!this.shelves.hasNext())
			return false;
		else
			this.current = this.shelves.next().enumerate();

		return this.hasNext();
	}

	/**
	 * Retreives the next book from the Bookshelf.
	 *
	 * @return the next book in the iterator.
	 *
	 * @throws NoSuchElementException if there are no more books to return.
	 */
	public Book next() {
		if(!this.hasNext())
			throw new NoSuchElementException();

		return this.current.next();
	}

	/**
	 * Optional... not implemented, probably wont ever need to...
	 */
	public void remove() {}

}