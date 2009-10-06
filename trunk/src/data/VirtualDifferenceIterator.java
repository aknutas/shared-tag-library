package data;

import java.util.*;

/**
 * This iterator is used in the Difference operation, and ensures only books
 * in the first set, not in the second set are shown.
 * 
 * @author Andrew Alm
 */
public class VirtualDifferenceIterator implements Iterator<Book> {

	private Iterator<Book> it;
	private Bookshelf shelf1;
	private Bookshelf shelf2;
	private Book next;
	
	public VirtualDifferenceIterator(Bookshelf shelf1, Bookshelf shelf2) {
		this.it = shelf1.enumerate();
		this.shelf1 = shelf1;
		this.shelf2 = shelf2;
	}
	
	public boolean hasNext() {
		if(!it.hasNext())
			return false;
		
		next = it.next();
		if(shelf2.contains(next))
			return this.hasNext();
		
		return true;
	}

	@Override
	public Book next() {
		if(!this.hasNext())
			throw new NoSuchElementException();
		
		return next;
	}

	@Override
	public void remove() {}

}
