package butler;

import java.util.Map;

import data.Book;

/**
 * The Butler abstract class contains the bare essentials of any Butler.
 * 
 * @author sjpurol
 *
 */
public abstract class Butler implements Comparable<Book>{

	/**
	 * used to store meta-data.
	 */
	protected Map<String, String> properties;

	/**
	 * Determines the correct bookshelf for the given book.
	 */
	@Override
	public abstract int compareTo(Book b);
	
	/**
	 * Returns the value of the given key. If the key does not exist in the map, returns null.
	 * @param string the key to find.
	 */
	public String getProperty(String string) {return properties.get(string);}
}


