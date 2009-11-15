package database;

import java.util.*;

import controller.ProgramProperties;

import butler.ButlerWeights;

import data.Bookshelf;
import network.Control;

/**
 * Interface QueryBuilder This static class creates a helper object for managing
 * database query input into several different formats. Access methods created
 * as needed.
 * 
 * @author Antti Knutas
 * 
 */
public interface QueryBuilder {

    /**
     * Returns all bookshelves with a book object with this specific title.
     * 
     * @return List A list of shelves.
     * @param booktitle Book title.
     */
    public List<Bookshelf> shelfSearchByBookName(String booktitle);

    /**
     * Returns all bookshelves with this specific property set to this value.
     * 
     * @return List A list of shelves.
     * @param property Property name.
     * @param value Value.
     */
    public List<Bookshelf> shelfSearchByProperty(String property, String value);

    /**
     * Returns all bookshelves.
     * 
     * @return List A list of shelves.
     */
    public List<Bookshelf> shelfList();

    /**
     * Stores new shelf, or updates the database with the changes. Takes the
     * persisted shelf object as a parameter.
     * 
     * @return int Success status.
     * @param shelf The shelf to be stored or updated.
     *            
     */
    public int shelfStore(Bookshelf shelf);
    
    /**
     * Stores new shelf, or updates the database with the changes. Takes a collection
     * of persisted shelf objects as a parameters.
     * 
     * @return int Success status.
     * @param shelf The shelf to be stored or updated.
     *            
     */
    public int shelfStore(Collection<Bookshelf> shelves);

    /**
     * Searches for, and removes an instance of this bookshelf from the
     * database. Returns integer value of the success status.
     * 
     * @return int
     * @param shelf
     */
    public int shelfRemove(Bookshelf shelf);
    
    /**
     * Checks if the database has a stored butlerweights object, and returns a list with them.
     * 
     * @return int
     */
    public List<ButlerWeights> getButlerWeights();
    
    /**
     * Stores a butlerweights object into the database.
     *  
     * @param butler The butler to be stored.
     * @return int
     */
    public int storeButler(ButlerWeights butler);
    
    /**
     * Stores ProgramProperties
     * @return int Storing status.
     */
    public int storeProperties(ProgramProperties properties);
    
    /**
     * Gets the stored ProgramProperties from the database.
     * @return ProgramProperties
     */
    public ProgramProperties getProperties();
    
}
