package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import operations.BookshelfOperations;

/**
 * The VirtualLibrary implements the Library interface and is used for storing
 * Bookshelf objects in memory during program execution. Unless specifically
 * saved, bookshelves in a VirtualLibrary will be lost when the program exits.
 * 
 * @author Andrew Alm
 */
public class VirtualLibrary implements Library {

    private List<Bookshelf> shelves;
    private Map<String, String> properties;

    /**
     * Creates a new empty VirtualLibrary. An empty VirtualLibrary has no
     * Bookshelves.
     */
    public VirtualLibrary() {
	this.shelves = new LinkedList<Bookshelf>();
	this.properties = new HashMap<String, String>();
    }

    /**
     * Adds the given shelf to the VirtualLibrary.
     * 
     * @param shelf
     *            the shelf to add to the library
     * 
     * @return this method always returns true
     * 
     * @throws NullPointerException
     *             if the shelf given is null.
     */
    @Override
    public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
	if (null == shelf)
	    throw new NullPointerException("shelf cannot be null");

	return this.shelves.add(shelf);
    }

    /**
     * Removes the given shelf from the VirtualLibrary, if it exists in the
     * library.
     * 
     * @param shelf
     *            the shelf to remove from the library
     * 
     * @return this method always returns true
     * 
     * @throws NullPointerException
     *             if the shelf given is null
     */
    @Override
    public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
	if (null == shelf)
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

    /**
     * This method is used to get a property of the given name from library. If
     * the property does not exist then null is returned.
     * 
     * @return the value of the property
     */
    @Override
    public String getProperty(String name) throws NullPointerException {
	if (null == name)
	    throw new NullPointerException("name cannot be null");

	return this.properties.get(name);
    }

    /**
     * Sets the named property to the given value, returning the old value for
     * the property. If the property does not exist, null is returned.
     * 
     * @param name
     *            the name of the property
     * @param value
     *            the value to set the property to
     * 
     * @return NullPointerException if the name of value given is null
     */
    @Override
    public String setProperty(String name, String value) {
	if (null == name || null == value)
	    throw new NullPointerException("name cannot be null");

	return this.properties.put(name, value);
    }

    /**
     * Returns an iterator containing the key-value pairs of all the properties.
     * 
     * @return an iterator of properties
     */
    @Override
    public Collection<Entry<String, String>> enumerateProperties() {
	return this.properties.entrySet();
    }

    /**
     * Gets the bookshelf of the given name from the library. If the library
     * does not contain a given bookshelf then null is returned.
     * 
     * @param name
     *            the name of the bookshelf
     * 
     * @return the Bookshelf object
     * 
     * @throws NullPointerException
     *             if the name given is null
     */
    @Override
    public Bookshelf getBookshelf(String name) throws NullPointerException {
	/* find bookshelf through iterator */
	for (Bookshelf shelf : this) {
	    if (!name.equals(shelf.getProperty("name")))
		continue;

	    return shelf;
	}

	return null;
    }

    /**
     * Gets an iterator of bookshelves with the names given in the Collection.
     * If a bookshelf name in the collection does not exist the Iterator will be
     * a small size than the collection.
     * 
     * @param names
     *            the name of the Bookshelf
     * 
     * @return an Iterator of Bookshelf objects
     * 
     * @throws NullPointerException
     *             if the names collection given
     *             isremoteLibrary1.getBookshelf("programming"); null
     */
    @Override
    public Collection<Bookshelf> getBookshelf(Collection<String> names)
	    throws NullPointerException {
	Collection<Bookshelf> shelves = new LinkedList<Bookshelf>();

	/* could also be done with a subset.. */
	for (Bookshelf shelf : this) {
	    if (names.contains(shelf.getProperty("name")))
		shelves.add(shelf);
	}

	return shelves;
    }

    /**
     * Gets the master shelf, a union of every bookshelf.
     * 
     * @return a Bookshelf.
     */
    @Override
    public Collection<String> getBookshelfNames() {
	Collection<String> names = new LinkedList<String>();

	for (Bookshelf shelf : this)
	    names.add(shelf.getProperty("name"));

	return names;
    }

}
