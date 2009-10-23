package data;

import java.util.Iterator;
import java.util.Map.Entry;
import network.*;

public class RemoteBookshelf implements Bookshelf {

	private Connection connection;
	private Bookshelf shelf;
	
	public RemoteBookshelf(Connection connection, Bookshelf shelf) {
		this.connection = connection;
		this.shelf = shelf;
	}
	
	public RemoteBookshelf(Connection connection) {
		this(connection, null);
	}
	
	@Override
	public boolean contains(Book book) throws IllegalArgumentException {
		if(null != shelf)
			return this.shelf.contains(book);
		else
			return false; //socket.send(
	}

	@Override
	public Bookshelf difference(Bookshelf shelf)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean empty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterator<Book> enumerate() {
		// TODO Auto-generated method stub
		return null;
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
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bookshelf subset(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
