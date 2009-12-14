package data;

import java.util.Map;
import java.util.Map.Entry;

import controller.ProgramProperties;
import database.QueryBuilder;

/**
 * The PersistentLibrary class extends the VirtualLibrary class and is used to
 * retrieve Bookshelf objects that are stored locally.
 * 
 * @author Andrew Alm
 */
public class PersistentLibrary extends VirtualLibrary {

	private QueryBuilder database;
		
	/**
	 * Creates a new PersistentLibrary object using the given 
	 * QueryBuilder object to access the database layer.
	 * 
	 * @param database the QueryBuilder to use
	 * 
	 * @throws NullPointerException if the database given is null
	 */
	public PersistentLibrary(QueryBuilder database) throws NullPointerException {
		if(null == database)
			throw new NullPointerException("database cannot be null");
		
		this.database = database;
		this.loadDatabase();
	}
	
	/**
	 * Loads bookshelves and properties from the database.
	 */
	@SuppressWarnings("unchecked") /* grr... */
	private void loadDatabase() {
		for(Bookshelf shelf : this.database.shelfList())
			this.addBookshelf(shelf);
		
		Object obj = ProgramProperties.getInstance().getProperty("data::library_properties");
		if(obj instanceof Map)
			return;
			
		Map<String,String> properties = (Map<String,String>)obj;
		for(Entry<String,String> property : properties.entrySet())
			this.setProperty(property.getKey(), property.getValue());
	}
	
	/**
	 * Saves a bookshelf to the persistent storage and adds the shelf
	 * to the library.
	 * 
	 * @param shelf the shelf to save to the library.
	 * 
	 * @return true if the shelf was successfully saved, otherwise
	 *         false
	 * 
	 * @throws NullPointerException if the shelf given is null
	 */
	public boolean saveBookshelf(VirtualBookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		if(1 != this.database.shelfStore(shelf))
			return false;
		
		return this.addBookshelf(shelf);
	}
	
	/**
	 * Deletes a bookshelf from the persistent storage and removes
	 * the shelf from the library.
	 * 
	 * @param shelf the shelf to delete from the library.
	 * 
	 * @return true if the shelf was deleted, otherwise false
	 * 
	 * @throws NullPointerException if the shelf given is null
	 */
	public boolean deleteBookshelf(VirtualBookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		if(1 != this.database.shelfRemove(shelf))
			return false;
		
		return this.removeBookshelf(shelf);
	}
	
	/**
	 * Determines whether the given VirtualBookshelf is stored in
	 * this persistent library.
	 * 
	 * @param shelf the shelf to check
	 * 
	 * @return true if the shelf is stored, otherwise false
	 * 
	 * @throws NullPointerException if the shelf given is null
	 */
	public boolean shelfStored(VirtualBookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("shelf cannot be null");
		
		for(Bookshelf storedShelf : this.database.shelfList()) {
			if(storedShelf == shelf)
				return true;
		}
		
		return false;
	}

}
