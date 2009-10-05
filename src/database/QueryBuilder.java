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
   * Returns all bookshelfs with a book object with this specific title.
   * @return       ArrayList An array list of shelves.
   * @param        booktitle Book title.
   */
  public ArrayList shelfSearch( String booktitle );


  /**
   * Creates a new shelf. Takes the persisted shelf object as a parameter.
   * @return       int Success status.
   * @param        shelf
   */
  public int shelfCreate( String shelfname, Bookshelf shelf );


  /**
   * Searches for, and removes an instance of this bookshelf from the database.
   * Returns boolean value of the success.
   * @return       boolean
   * @param        shelf
   */
  public boolean shelfRemove( Bookshelf shelf );


}
