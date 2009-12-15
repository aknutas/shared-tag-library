package data;

import java.io.Serializable;
import java.util.Iterator;

import data.messages.RemoteMessage;

/**
 * The BookIteratorResponder class extends the IteratorResponder and is used to
 * handle RemoteIterator objects for Books. 
 * 
 * @author Andrew Alm
 */
public class BookIteratorResponder extends IteratorResponder<Book> {

	/**
	 * Creates a new BookIteratorResponder object from the given Book
	 * Iterator object.
	 * 
	 * @param iter the Iterator of Book objects to use
	 * 
	 * @throws NullPointerException if the iter given is null
	 */
	public BookIteratorResponder(Iterator<Book> iter) throws NullPointerException {
		super(iter);
	}

	/**
	 * This method is used to convert a Book object into an object
	 * suitable for transfer over the network. For this, the Book is
	 * converted to an Integer id for routing purposes.
	 * 
	 * @param book the Book object to serialize
	 * 
	 * @throws NullPointerException if the book given is null
	 */
	@Override
	public Serializable serializeObject(Book book) throws NullPointerException, IllegalArgumentException {
		if(null == book)
			throw new NullPointerException("shelf cannot be null");
		
		return new Integer((new BookResponder(book)).getID());
	}

	/**
	 * Stub method.
	 */
	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message)	throws NullPointerException, IllegalArgumentException {
		return null;
	}

	
	
}
