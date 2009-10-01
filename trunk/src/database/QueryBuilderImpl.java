package database;

import java.util.ArrayList;

import data.Bookshelf;


/**
 * Class QueryBuilderImpl
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


  //
  // Accessor methods
  //

  //
  // Other methods
  //

  /**
   * Returns all bookshelfs with a book object with this specific title.
   * @return       ArrayList
   * @param        booktitle Book title. String type.
   */
  public ArrayList shelfSearch( String booktitle )
  {
	return null;
  }


  /**
   * Creates a new shelf. Takes both shelf name and the shelf object itself as
   * parameters.
   * @return       int
   * @param        shelfname
   * @param        shelf
   */
  public int shelfCreate( String shelfname, Bookshelf shelf )
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
