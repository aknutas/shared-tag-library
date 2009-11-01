package data;

import java.util.*;

/**
 * The Bookshelf interface stores a set of books. The Bookshelf is main
 * object used for searching through books.
 * 
 * @author Andrew Alm
 */
public interface Bookshelf extends Iterable<Book> {
	
	/**
	 * Adds a book to the Bookshelf, if the Bookshelf already contains the book
	 * then this method does nothing.
	 * 
	 * @param book the Book to add to the Bookshelf.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public void insert(Book book) throws IllegalArgumentException;
	
	/**
	 * Removes a book from the Bookshelf, if the Bookshelf does not contain the
	 * book then this mtehod does nothing.
	 * 
	 * @param book the Book to remove from the Bookshelf.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public boolean remove(Book book) throws IllegalArgumentException;
	
	/**
	 * Determines whether the bookshelf contains the given book.
	 * 
	 * @param book the book to check for
	 * 
	 * @return true if the the bookshelf has the book, otherwise false
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public boolean contains(Book book) throws IllegalArgumentException;
	
	/**
	 * Determines whether the Bookshelf is empty. An empty bookshelf contains no
	 * books and has a size of 0.
	 * 
	 * @return true if the Bookshelf is empty, otherwise false.
	 */
	public boolean empty();
	
	/**
	 * Determines the size of the Bookshelf. A bookshelf's size is equal to the
	 * number of book it conatins.
	 * 
	 * @return the number of books contained in the Bookshelf.
	 */
	public int size();
	
	/**
	 * Retreives an iterator containing all books on the Bookshelf.
	 * 
	 * @return an iterator.
	 */
	public Iterator<Book> enumerate();

	/**
	 * Joins two Bookshelfs together into a new bookshelf. This method does not
	 * change any old bookshelves.
	 * 
	 * @param shelf the shelf to union with.
	 * 
	 * @return a new Bookshelf object.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException;
	
	/**
	 * Returns a bookshelf containing only that are contained in each Bookshelf
	 * object.
	 * 
	 * @param shelf the shelf to intersect with.
	 * 
	 * @return a new Bookshelf obejct.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf intersect(Bookshelf shelf) throws IllegalArgumentException;
	
	/**
	 * Returns a bookshelf containly found only on this Bookshelf.
	 * 
	 * @param shelf the shelf to take the difference with.
	 * 
	 * @return a new Bookshelf object.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf difference(Bookshelf shelf) throws IllegalArgumentException;
	
	/**
	 * Returns a bookshelf containing a subset of Book objects which
	 * match the given Book Comparable.
	 * 
	 * @param book the book to model the new bookshelf after.
	 * 
	 * @return a new Bookshelf object.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public Bookshelf subset(Comparable<Book> comparable) throws IllegalArgumentException;

	/**
	 * Gets the value of the given property. If the given property does not
	 * exist then null is returned.
	 * 
	 * @param property the name of the property to get.
	 * 
	 * @return the value of the property
	 * 
	 * @throws IllegalArgumentException if the property given is null
	 */
	public String getProperty(String name) throws IllegalArgumentException;
	
	
	/**
	 * Sets the named property to the given value, returning the old value for
	 * the property. If the Book did not have the property, null returned.
	 * 
	 * @param name the name of the property
	 * @param value the value to set the property to
	 * 
	 * @return IllegalArgumentException if the name of value given is null
	 */
	public String setProperty(String name, String value);
	
	/**
	 * Returns an iterator containing the key-value pairs of all the properties
	 * the book has.
	 * 
	 * @return an iterator of properties
	 */
	public Iterator<Map.Entry<String, String>> enumerateProperties();
}
