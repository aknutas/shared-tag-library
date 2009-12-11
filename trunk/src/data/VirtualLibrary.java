package data;

import java.util.*;
import java.util.Map.Entry;

import operations.*;

/**
 * The VirtualLibrary implements the Library interface and is used for storing
 * Bookshelf objects in memory during program execution.  Unless specifically
 * saved, bookshelves in a VirtualLibrary will be lost when the program exits.
 *
 * @author Andrew Alm
 */
public class VirtualLibrary implements Library {

	private List<Bookshelf> shelves;
	private Map<String,String> properties;
	
	/**
	 * Creates a new empty VirtualLibrary. An empty VirtualLibrary
	 * has no Bookshelves.
	 */
	public VirtualLibrary() {
		this.shelves = new LinkedList<Bookshelf>();
		this.properties = new HashMap<String, String>();
	}
	
	/**
	 * Adds the given shelf to the VirtualLibrary.
	 * 
	 * @param shelf the shelf to add to the library
	 * 
	 * @return this method always returns true
	 * 
	 * @throws NullPointerException if the shelf given is null.
	 */
	@Override
	public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		return this.shelves.add(shelf);
	}
	
	/**
	 * Removes the given shelf from the VirtualLibrary, if it exists
	 * in the library.
	 * 
	 * @param shelf the shelf to remove from the library
	 * 
	 * @return this method always returns true
	 * 
	 * @throws NullPointerException if the shelf given is null
	 */
	@Override
	public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		this.shelves.remove(shelf);
		return true;
	}

	/**
	 * Gets a bookshelf containing all books in the library.
	 * 
	 * @return a Bookshelf containing all books
	 */
	@Override
	public Bookshelf getMasterShelf() {
		return BookshelfOperations.union(this.shelves);
	}

	/**
	 * Gets an iterator of all the Bookshelves in the library.
	 * 
	 * @return an Iterator of Bookshelf objects
	 */
	@Override
	public Iterator<Bookshelf> iterator() {
		return this.shelves.iterator();
	}

	
	@Override
	public String getProperty(String name) throws NullPointerException {
		if(null == name)
			throw new NullPointerException("name cannot be null");
		
		return this.properties.get(name);
	}

	@Override
	public String setProperty(String name, String value) {
		if(null == name || null == value)
			throw new NullPointerException("name cannot be null");
		
		return this.properties.put(name, value);
	}
	
	@Override
	public Iterator<Entry<String, String>> enumerateProperties() {
		return this.properties.entrySet().iterator();
	}

	@Override
	public Bookshelf getBookshelf(String name) throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Bookshelf> getBookshelf(Collection<String> names)
			throws NullPointerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<String> getBookshelfNames() {
		// TODO Auto-generated method stub
		return null;
	}

}
