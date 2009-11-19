package data;

import java.util.Iterator;
import java.util.Map;

/**
 * The properties interface is used for classes that need to be able
 * to get and set property strings. This class is extended by the
 * Library, Bookshelf and Book interface.
 * 
 * @author Andrew Alm
 */
public interface Properties {

	/**
	 * Gets the value of the given property. If the given property does not
	 * exist then null is returned.
	 * 
	 * @param property the name of the property to get.
	 * 
	 * @return the value of the property
	 * 
	 * @throws NullPointerException if the property given is null
	 */
	public String getProperty(String name) throws NullPointerException;
	
	
	/**
	 * Sets the named property to the given value, returning the old value for
	 * the property. If the property does not exist, null is returned.
	 * 
	 * @param name the name of the property
	 * @param value the value to set the property to
	 * 
	 * @return NullPointerException if the name of value given is null
	 */
	public String setProperty(String name, String value) throws NullPointerException;
	
	/**
	 * Returns an iterator containing the key-value pairs of all the
	 * properties.
	 * 
	 * @return an iterator of properties
	 */
	public Iterator<Map.Entry<String, String>> enumerateProperties();
	
}
