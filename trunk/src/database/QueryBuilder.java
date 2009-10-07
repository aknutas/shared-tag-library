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
   * @return       List A list of shelves.
   * @param        booktitle Book title.
   */
  public List<Bookshelf> shelfSearch( String booktitle );
  
  /**
   * Returns all bookshelves.
   * @return       List An array list of shelves.
   */
  public List<Bookshelf> shelfList(   );

  /**
   * Creates a new shelf. Takes the persisted shelf object as a parameter.
   * @return       int Success status.
   * @param        shelf
   */
  public int shelfCreate( Bookshelf shelf );


  /**
   * Searches for, and removes an instance of this bookshelf from the database.
   * Returns integer value of the success status.
   * @return       int
   * @param        shelf
   */
  public int shelfRemove( Bookshelf shelf );


}
