package database;

import java.util.*;

import data.Bookshelf;

/**
 * Class QueryBuilderImpl QueryBuilder implementation.
 * 
 * @author Antti Knutas
 * 
 */
public final class QueryBuilderImpl implements QueryBuilder {

    public QueryBuilderImpl() {
    };

    /**
     * Returns all bookshelves with a book object with this specific title.
     * 
     * @return List A list of shelves.
     * @param String Book title.
     */
    @SuppressWarnings("unchecked")
    public List<Bookshelf> shelfSearchByBookName(String booktitle) {
	Access db = AccessImpl.getInstance();
	String querystring = "SELECT FROM data.VirtualBookshelf WHERE (this.bookshelf.contains(book) && book.properties.containsEntry('title', '" + booktitle + "')) VARIABLES data.VirtualBook book";
	List<Bookshelf> returnlist = db.query(querystring);

	return returnlist;
    }

    /**
     * Returns all bookshelves with this specific property.
     * 
     * @return List A list of shelves.
     * @param booktitle Book title.
     */
    @SuppressWarnings("unchecked")
    public List<Bookshelf> shelfSearchByProperty( String property, String value )
    { 
	Access db = AccessImpl.getInstance();
	String querystring = "SELECT FROM data.VirtualBookshelf WHERE properties.containsEntry('" + property + "', '" + value + "')";
	List<Bookshelf> returnlist = db.query(querystring);
	return returnlist;
    }

    /**
     * Returns all bookshelves.
     * 
     * @return Array A list of shelves.
     */
    @SuppressWarnings("unchecked")
    public List<Bookshelf> shelfList() {
	Access db = AccessImpl.getInstance();
	String querystring = "SELECT FROM data.VirtualBookshelf";

	List<Bookshelf> returnlist = (List<Bookshelf>) db.query(querystring);

	return returnlist;
    }

    /**
     * Stores new shelf, or updates the database with the changes. Takes the
     * persisted shelf object as a parameter.
     * 
     * @return int Success status.
     * @param shelf The shelf to be stored or updated.   
     */
    public int shelfStore(Bookshelf shelf) {
	Access db = AccessImpl.getInstance();
	int returnvalue = db.commitOne(shelf);

	return returnvalue;
    }

    /**
     * Searches for, and removes an instance of this bookshelf from the
     * database. Returns the success status.
     * 
     * @return boolean
     * @param shelf
     */
    public int shelfRemove(Bookshelf shelf) {
	Access db = AccessImpl.getInstance();
	int returnvalue = db.removeOne(shelf);

	return returnvalue;
    }

}
