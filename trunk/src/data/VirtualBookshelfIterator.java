package data;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The VirtualBookshelfIterator object implements the Iterator interface for
 * Book object.  It is used to iterate over all the books matching the 
 * comparable object object given, or all books if the comparable given is
 * null.
 * 
 * @author Andrew Alm
 */
public class VirtualBookshelfIterator implements Iterator<Book> {

	private Book nextBook;
	private Comparable<Book> comparable;
	
	private Iterator<Book> currentShelf;
	private Iterator<Bookshelf> bookshelves;
	
	/**
	 * Creates a new VirtualBookshelfIterator with the given books,
	 * bookshelves and comparable to use to iterator over.
	 * 
	 * @param books the books to iterate over
	 * @param bookshelves the bookshelves to iterate 
	 * @param comparable used to check if a book should actually
	 *        exist in this Iterator
	 *        
	 * @throws NullPointerException if the books or bookshelves given
	 *         are null 
	 */
	public VirtualBookshelfIterator(Iterable<Book> books, Iterable<Bookshelf> bookshelves, Comparable<Book> comparable) throws NullPointerException {
		if((null == books) || (null == bookshelves))
			throw new NullPointerException("books or bookshelves cannot be null");
		
		this.nextBook = null;
		this.currentShelf = books.iterator();
		this.bookshelves = bookshelves.iterator();
		this.comparable = comparable;
		
		/* default comparable, everything is equal */
		if(null == this.comparable) {
			this.comparable = new Comparable<Book>() {
				public int compareTo(Book book) {
					return 0;
				}
				public boolean equals(Object obj) {
					return true;
				}
			};
		}
	}
	
	/**
	 * This method is used to determine if the iterator has any more
	 * data available.
	 * 
	 * @return true if next will not throw NullPointerException
	 */
	@Override
	public boolean hasNext() {
		if(null != this.nextBook)
			return true;
		
		if(this.currentShelf.hasNext()) {			
			this.nextBook = this.currentShelf.next();
			if(!this.comparable.equals(this.nextBook))
				this.nextBook = null;
			
			return this.hasNext();
		}
		
		if(!this.bookshelves.hasNext())
			return false;
		
		this.currentShelf = this.bookshelves.next().iterator();
		return this.hasNext();
	}

	/**
	 * Returns the next book in the in the iterator.
	 * 
	 * @return a Book object
	 * 
	 * @throws NoSuchElementException if there are no elements
	 *         available
	 */
	@Override
	public Book next() throws NoSuchElementException {
		if(!this.hasNext())
			throw new NoSuchElementException();
		
		Book next = this.nextBook;
		this.nextBook = null;
		
		return next;
	}

	/**
	 * Optional method in interface (not used).
	 */
	@Override
	public void remove() {}
	
}