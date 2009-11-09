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
	public boolean contains(Book book) throws IllegalArgumentException {
		RemoteMessage message = new BookshelfMessage(BookshelfMessage.MSG_CONTAINS, this.id);
		
		//RemoteMessage response = this.send(new , timeout)
		return false;
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
	public Iterator<Entry<String, String>> enumerateProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		System.out.println("shelf");
	}

	@Override
	public Bookshelf intersect(Bookshelf shelf) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String setProperty(String name, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookshelf subset(Comparable<Book> comparable) throws IllegalArgumentException {
		if(null == comparable)
			throw new NullPointerException("comparable cannot be null");
		
		return new VirtualBookshelf(this, comparable);
	}

	@Override
	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Iterator<Book> iterator() {
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

}
