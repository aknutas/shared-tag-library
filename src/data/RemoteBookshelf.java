package data;

import java.util.*;
import java.util.Map.*;

import data.messages.*;
import network.*;
import network.messages.*;

public class RemoteBookshelf implements Bookshelf, ClientResponder {

	private Control network;
	private int connection;
	private int id;
	
	public RemoteBookshelf(Control network, int connection, int id) {
		this.network = network;
		this.connection = connection;
		this.id = id;
		
		this.network.sendLibraryMsg(this.connection, new BookshelfMessage(BookshelfMessage.MSG_HELLO, this.id), this);
	}

	@Override
	public boolean contains(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return false;
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

	@Override		// TODO Auto-generated method stub

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
		return null;		// TODO Auto-generated method stub

	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bookshelf subset(Comparable<Book> comparable)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void onMessage(Message message) {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(message instanceof RemoteMessage) {
			return;
		}
		
		if(!(message instanceof BookshelfMessage))
			throw new IllegalArgumentException("illegal message type");
		
		switch(((BookshelfMessage)message).getMessageType()) {
		case BookshelfMessage.MSG_HELLO:
			System.out.println("Bookshelf hello");
		}
	}

	@Override
	public Bookshelf subset(Book book) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Book> enumerate() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
