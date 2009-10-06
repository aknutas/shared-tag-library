package database;

import java.util.*;

import data.Bookshelf;


/**
 * Class QueryBuilderImpl
 * QueryBuilder implementation.
 * 
 * @author Antti Knutas
 * 
 */
public final class QueryBuilderImpl implements QueryBuilder {

  //
  // Fields
  //

  
  //
  // Constructors
  //
  public QueryBuilderImpl () { };
  
  //
  // Methods
  //

  /**
   * Returns all bookshelves with a book object with this specific title.
   * @return       ArrayList An array list of shelves.
   * @param        booktitle Book title.
   */
  @SuppressWarnings("unchecked") 
public List<Bookshelf> shelfSearch( String booktitle )
  {
	  Access db = AccessImpl.getInstance();
	  String querystring = "SELECT FROM data.Bookshelf WHERE title = " + booktitle;
	
	  List<Bookshelf> returnlist = (List<Bookshelf>)db.query(querystring);
	  
	  return returnlist;
  }
  
  /**
   * Returns all bookshelves.
   * @return       ArrayList An array list of shelves.
   */
  @SuppressWarnings("unchecked")
public List<Bookshelf> shelfList(   )
  {
	  Access db = AccessImpl.getInstance();
	  String querystring = "SELECT UNIQUE FROM data.Bookshelf";
	
	  List<Bookshelf> returnlist = (List<Bookshelf>)db.query(querystring);
	  
	  return returnlist;
  }

  /**
   * Creates a new shelf. Takes the persisted shelf object as a parameter.
   * @return       int Success status.
   * @param        shelf The persisted shelf object.
   */
  public int shelfCreate( Bookshelf shelf )
  {
	Access db = AccessImpl.getInstance();
	int returnvalue = db.commitOne(shelf);
	
	return returnvalue;
  }

  /**
   * Searches for, and removes an instance of this bookshelf from the database.
   * Returns the success status.
   * @return       boolean
   * @param        shelf
   */
  public int shelfRemove( Bookshelf shelf )
  {
	Access db = AccessImpl.getInstance();
	int returnvalue = db.removeOne(shelf);
	
	return returnvalue;
  }
  
}
