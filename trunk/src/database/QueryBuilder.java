package database;

import java.util.*;

import data.Bookshelf;
import network.Control;


/**
 * Interface QueryBuilder
 * This static class creates a helper object for managing database query input into
 * several different formats. Access methods created as needed.
 * 
 * @author Antti Knutas
 * 
 */
public interface QueryBuilder {

  //
  // Fields
  //

  
  //
  // Methods
  //

  /**
   * Returns all bookshelves with a book object with this specific title.
   * DOES NOT WORK YET
   * @return       List A list of shelves.
   * @param        booktitle Book title.
   */
  public List<Bookshelf> shelfSearchByBookName( String booktitle );
  
  /**
   * Returns all bookshelves with this specific property.
   * DOES NOT WORK YET
   * @return       List A list of shelves.
   * @param        property Property name.
   * @param        value Property value.
   */
  public List<Bookshelf> shelfSearchByProperty( String property, String value );
  
  /**
   * Returns all bookshelves.
   * @return       List A list of shelves.
   */
  public List<Bookshelf> shelfList(   );

  /**
   * Stores new shelf, or updates the database with the changes. Takes the persisted shelf object as a parameter.
   * @return       int Success status.
   * @param        shelf The shelf to be stored or updated.
   */
  public int shelfStore( Bookshelf shelf );


  /**
   * Searches for, and removes an instance of this bookshelf from the database.
   * Returns integer value of the success status.
   * @return       int
   * @param        shelf
   */
  public int shelfRemove( Bookshelf shelf );


}
