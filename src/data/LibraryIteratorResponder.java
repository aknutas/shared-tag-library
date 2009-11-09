package data;

import java.util.*;
import java.io.*;

import data.messages.RemoteMessage;

/**
 * A LibraryIteratorResponder extends an IteratorResponder for Bookshelf
 * objects
 * 
 * @author Andrew Alm
 */
public class LibraryIteratorResponder extends IteratorResponder<Bookshelf> {

	/**
	 * Creates a new LibraryIteratorResponder from the given Iterator
	 * of Bookshelf objects.
	 * 
	 * @param iter the iterator to use
	 * 
	 * @throws NullPointerException if the iterator given is null
	 */
	public LibraryIteratorResponder(Iterator<Bookshelf> iter) throws NullPointerException {
		super(iter);
	}

	@Override
	public Serializable serializeObject(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		return new Integer((new BookshelfResponder(shelf)).getID());
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
