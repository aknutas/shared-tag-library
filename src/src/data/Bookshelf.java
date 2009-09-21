package data;

import java.util.Iterator;

/**
 * The Bookshelf public interface represents a set of books.
 *
 * @author Andrew Alm
 */
public interface Bookshelf {

	/**
	 * Add a new book into the bookshelf.
	 *
	 * @param book the Book to add.
	 *
	 * @throws NullPointerException if the book given is null
	 */
	void addBook(Book book) throws NullPointerException;

	/**
	 * Remove a Book from the Bookshelf.
	 *
	 * @param book the Book to remove
	 *
	 * @throws NullPointerException if the book given is null
	 */
	void removeBook(Book book) throws NullPointerException;

	/**
	 * Determines whether the biven Book is on the Bookshelf.
	 *
	 * @param book the Book to search for
	 *
	 * @return true if the book is on the bookshelf, otherwise false
	 *
	 * @throws NullPointerException if the book given is null
	 */
	boolean contains(Book book) throws NullPointerException;

	/**
	 * Return an iterator to run through all books on the bookshelf,
	 * sorted by relevancy.
	 *
	 * @return an Iterator of all the Books on the Bookshelf
	 */
	Iterator<Book> getBooks();

	/**
	 * Returns a new Bookshelf containing all items on each bookshelf.
	 *
	 * @param shelf the Bookshelf to union this Bookshelf with
	 *
	 * @return a new Bookshelf.
	 *
	 * @throws NullPointerException if shelf is null
	 */
	Bookshelf union(Bookshelf shelf) throws NullPointerException;

	/**
	 * Creates a new bookshelf containing only books similar to the
	 * given book.
	 *
	 * @param book the model book.
	 *
	 * @return a new Bookshelf.
	 *
	 * @throws NullPointerException if the book is null
	 */
	Bookshelf subshelf(Book book) throws NullPointerException;

}

