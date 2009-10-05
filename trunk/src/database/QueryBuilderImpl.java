package database;

import java.util.ArrayList;

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
   * Returns all bookshelfs with a book object with this specific title.
   * @return       ArrayList An array list of shelves.
   * @param        booktitle Book title.
   */
  public ArrayList shelfSearch( String booktitle )
  {
	return null;
  }

  /**
   * Creates a new shelf. Takes the persisted shelf object as a parameter.
   * @return       int Success status.
   * @param        shelf The persisted shelf object.
   */
  public int shelfCreate( Bookshelf shelf )
  {
	return 0;
  }

  /**
   * Searches for, and removes an instance of this bookshelf from the database.
   * Returns boolean value of the success.
   * @return       boolean
   * @param        shelf
   */
  public boolean shelfRemove( Bookshelf shelf )
  {
	return false;
  }
  
}
