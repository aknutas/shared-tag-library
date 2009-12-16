package data;

import java.io.Serializable;
import java.util.Iterator;

/**
 * A BookshelfIteratorResponder extends an IteratorResponder for Bookshelf
 * objects
 * 
 * @author Andrew Alm
 */
public class BookshelfIteratorResponder extends IteratorResponder<Bookshelf> {

    /**
     * Creates a new LibraryIteratorResponder from the given Iterator of
     * Bookshelf objects.
     * 
     * @param iter
     *            the iterator to use
     * 
     * @throws NullPointerException
     *             if the iterator given is null
     */
    public BookshelfIteratorResponder(Iterator<Bookshelf> iter)
	    throws NullPointerException {
	super(iter);
    }

    /**
     * This method is used to serialize a bookshelf into an Integer.
     * 
     * @param the
     *            Bookshelf to serialize
     * 
     * @return a Serializable object
     * 
     * @throws NullPointerException
     *             if the shelf given is null
     */
    @Override
    public Serializable serializeObject(Bookshelf shelf)
	    throws NullPointerException {
	if (null == shelf)
	    throw new NullPointerException("shelf cannot be null");

	return new Integer((new BookshelfResponder(shelf)).getID());
    }

}
