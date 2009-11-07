package data;

import java.util.*;

/**
 * 
 * @author Andrew Alm
 */
public class VirtualBookshelfIterator implements Iterator<Book> {

	private Book nextBook;
	private Comparable<Book> comparable;
	
	private Iterator<Book> currentShelf;
	private Iterator<Bookshelf> bookshelves;
	
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

	@Override
	public Book next() {
		if(!this.hasNext())
			throw new NoSuchElementException();
		
		Book next = this.nextBook;
		this.nextBook = null;
		
		return next;
	}

	@Override
	public void remove() {}
	
}