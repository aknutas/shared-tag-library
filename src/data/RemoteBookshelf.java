package data;

import java.util.*;
import java.util.Map.*;
import data.messages.*;
import network.*;

/**
 * A RemoteBookshelf extends the RemoteObject class and implements the
 * Bookshelf interface. This class accessing a Bookshelf on a remote system
 * using the Bookshelf interface.
 * 
 * @author Andrew Alm
 */
public class RemoteBookshelf extends RemoteObject implements Bookshelf {

	private final int id;
	
	/**
	 * Creates a new RemoteBookshelf object with the given network,
	 * connection, and id.
	 * 
	 * @param network the network Control object to use
	 * @param connection the connection id
	 * @param id the id of the the RemoteBookshelf (given by server)
	 * 
	 * @throws NullPointerException if the network given is null.
	 * @throws RemoteObjectException if the RemoteBookshelf cannot be
	 *         created.
	 */
	public RemoteBookshelf(Control network, int connection, int id)throws NullPointerException, RemoteObjectException {
		super(connection, network, 5000);
		
		this.id = id;
	}

	/**
	 * Determines whether the RemoteBookshelf object is empty. An
	 * empty RemoteBookshelf has a size of zero.
	 * 
	 * @return true if the RemoteBookshelf is empty
	 */
	@Override
	public boolean empty() {
		return (0 == this.size());
	}
	
	/**
	 * Gets the size of a RemoteBookshel.  The size is the number of
	 * books in the shelf (this may be greater than the number of
	 * books an iterator returns). If there is an error this method
	 * will return 0, although 0 may indicate an empty bookshelf.
	 * 
	 * @return the size of the RemoteBookshelf
	 */
	@Override
	public int size() {
		RemoteMessage response = this.send(new BookshelfMessage(BookshelfMessage.MSG_SIZE, this.id));
		if(null == response)
			return 0;
		
		if(RemoteMessage.MSG_ERROR == response.getMessageType())
			return 0;
		
		Integer size = response.dequeParameter();
		if(null == size)
			return 0;
		
		return size.intValue();
	}

	/**
	 * Gets the value of the property of the given name, or null if
	 * the property name is not found (or there is another error).
	 * 
	 * @param name the name of the property
	 * 
	 * @return the String value of the property
	 * 
	 * @throws NullPointerException if the name given is null.
	 */
	@Override
	public String getProperty(String name) throws NullPointerException {
		if(null == name)
			throw new NullPointerException("name cannot be null");
		
		/* send message */
		BookshelfMessage message = new BookshelfMessage(BookshelfMessage.MSG_GET, this.id);
		message.queueParameter(name);
		RemoteMessage response = this.send(message, 5000);
		if(null == response)
			return null;
		
		if(!(response instanceof BookshelfMessage))
			return null;
		
		if(BookshelfMessage.MSG_GET != response.getMessageType())
			return null;
		
		return response.dequeParameter();
	}
	
	/**
	 * This method is used to set a property of the given name to the
	 * given value, returning the old value.
	 * 
	 * @param name the name of the property
	 * @param value the value of the property
	 * 
	 * @return the old value of the property.
	 * 
	 * @throws NullPointerException if the property given is null.
	 */
	@Override
	public String setProperty(String name, String value) throws NullPointerException {
		if((null == name) || (null == value))
			throw new NullPointerException("name or value cannot be null");
		
		/* send message */
		BookshelfMessage message = new BookshelfMessage(BookshelfMessage.MSG_SET, this.id);
		message.queueParameter(name);
		message.queueParameter(value);
		RemoteMessage response = this.send(message, 5000);
		if(null == response)
			return null;
		
		if(!(response instanceof BookshelfMessage))
			return null;
		
		if(BookshelfMessage.MSG_SET != response.getMessageType())
			return null;
		
		return response.dequeParameter();
	}

	/**
	 * Gets an iterator containing all of the properties of the 
	 * RemoteBookshelf object. If there is an error getting the 
	 * iterator then null is returned.
	 * 
	 * @return an Iterator of String-String pairings, or null on
	 *         error
	 */
	@Override
	public Iterator<Entry<String, String>> enumerateProperties() {
		try {
			BookshelfMessage message = new BookshelfMessage(BookshelfMessage.MSG_PROPERTY_ITERATOR, this.id);
			RemoteMessage response = this.send(message, 5000);
			if(null == response)
				return null;
			
			if(!(response instanceof BookshelfMessage))
				return null;
			
			if(BookshelfMessage.MSG_PROPERTY_ITERATOR != response.getMessageType())
				return null;
			
			Integer id = response.dequeParameter();
			if(null == id)
				return null;
			
			return new RemotePropertyIterator(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}
	
	/**
	 * Gets an iterator of books on the RemoteBookshelf. If there is
	 * an error getting the iterator then null is returned.
	 * 
	 * @return an Iterator of Book objects, or null on error
	 */
	@Override
	public Iterator<Book> iterator() {
		try {
			RemoteMessage response = this.send(new BookshelfMessage(BookshelfMessage.MSG_ITERATOR, this.id), 5000);
			
			if((null == response) || !(response instanceof BookshelfMessage))
				return null;
			
			if(BookshelfMessage.MSG_ERROR == response.getMessageType())
				return null;
			
			Integer id = response.dequeParameter();
			if(null == id)
				return null;
			
			return new RemoteBookIterator(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}
	
	/**
	 * Creates a bookshelf containing a subset based on the
	 * comparable object given.
	 * 
	 * @param comparable the Comparable to use to determine whether
	 *        a book is in a subset
	 * 
	 * @return a Bookshelf containing a subset of Book objects
	 */
	@Override
	public Bookshelf subset(Comparable<Book> comparable) throws NullPointerException {
		if(null == comparable)
			throw new NullPointerException("comparable cannot be null");
		
		return new VirtualBookshelf(this, comparable);
	}

	/**
	 * Not implemented (yet).
	 */
	@Override
	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Not implemented (yet).
	 */
	@Override
	public Bookshelf intersect(Bookshelf shelf) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Not implemented (yet).
	 */
	@Override
	public Bookshelf difference(Bookshelf shelf) throws IllegalArgumentException {
		return null;
	}
	
	/**
	 * Not implemented (yet).
	 */
	@Override
	public boolean contains(Book book) throws IllegalArgumentException {
		return false;
	}
	
	/**
	 * Not implemented (yet).
	 */
	@Override
	public void insert(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		System.out.println("shelf");
	}

	/**
	 * Not implemented (yet).
	 */
	@Override
	public boolean remove(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean removeAll() {
		return false;
	}

}
