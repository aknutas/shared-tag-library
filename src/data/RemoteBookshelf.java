package data;

import java.util.*;
import java.util.Map.*;
import data.messages.*;
import network.*;

public class RemoteBookshelf extends RemoteObject implements Bookshelf {

	private final int id;
	
	public RemoteBookshelf(Control network, int connection, int id)throws NullPointerException, RemoteObjectException {
		super(connection, network, 5000);
		
		this.id = id;
	}

	@Override
	public boolean empty() {
		return (0 == this.size());
	}
	
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

	@Override
	public String getProperty(String name) throws IllegalArgumentException {
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
	
	@Override
	public String setProperty(String name, String value) {
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
	
	@Override
	public Bookshelf subset(Comparable<Book> comparable) throws IllegalArgumentException {
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

}
