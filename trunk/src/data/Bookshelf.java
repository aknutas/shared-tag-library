package data;

import java.util.Iterator;

public interface Bookshelf {
	public void insert(Book book) throws IllegalArgumentException;
	public void remove(Book book) throws IllegalArgumentException;
	public boolean empty();
	public int size();
	public Iterator<Book> enumerate();

	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException;
	public Bookshelf intersect(Bookshelf shelf) throws IllegalArgumentException;
	public Bookshelf difference(Bookshelf shelf) throws IllegalArgumentException;
	public Bookshelf subset(Bookshelf shelf, Book book) throws IllegalArgumentException;

	public String getName();
	public String setName(String name) throws IllegalArgumentException;
}
