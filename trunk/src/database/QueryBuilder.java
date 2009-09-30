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
  public ArrayList shelfSearch( String booktitle );


  /**
   * Creates a new shelf. Takes both shelf name and the shelf object itself as
   * parameters.
   * @return       int
   * @param        shelfname
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
