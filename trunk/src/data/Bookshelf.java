package data;

import java.util.Iterator;

/**
 * The Bookshelf interface stores a set of books. The Bookshelf is main
 * object used for searching through books.
 * 
 * @author Andrew Alm
 */
public interface Bookshelf {
	
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
	 * Returns a bookshelf containing a subset of books which match the given book.
	 * 
	 * @param book the book to model the new bookshelf after.
	 * 
	 * @return a new Bookshelf object.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public Bookshelf subset(Book book) throws IllegalArgumentException;

	/**
	 * to be removed
	 * @return none
	 */
	public String getName();
	
	/**
	 * to be removed
	 * @return none
	 */
	public String setName(String name) throws IllegalArgumentException;
}
