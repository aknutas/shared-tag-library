package data;

import java.util.*;

/**
 * The Book interface represents a single book. Each book has an author, title,
 * and description. A book can have 0 or more tags associated with it, each tag
 * is weighted by adding and removing tags.  A book can also have properties
 * attached to it to represent name-value pairs of data, such as an author
 * property containing the author's name.
 *
 * @author Andrew Alm
 */
public interface Book {

	/**
	 * The weight of the given tag is increased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag.
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int tag(String tag) throws IllegalArgumentException;

	/**
	 * The weight of the given tag is decreased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int untag(String tag) throws IllegalArgumentException;

	/**
	 * Gets the weight of a given tag. If the tag has never been used 
	 * with this VirtualBook then the weight will be zero. (Note: A 
	 * return value of zero does not necessicarily indicate that a tag
	 * has never been used with the book.)
	 *
	 * @param tag the name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int weight(String tag) throws IllegalArgumentException;

	/**
	 * Returns an iterator of all tags on a book and their associated
	 * weights.
	 * 
	 * @return an Iterator of map entries.
	 */
	public Iterator<Map.Entry<String, Integer>> enumerateTags();
	
	/**
	 * Gets the value of the given property. If the given property does not
	 * exist then null is returned.
	 * 
	 * @param property the name of the property to get.
	 * 
	 * @return the value of the property
	 * 
	 * @throws IllegalArgumentException if the property given is null
	 */
	public String getProperty(String name) throws IllegalArgumentException;
	
	
	/**
	 * Sets the named property to the given value, returning the old value for
	 * the property. If the Book did not have the property, null returned.
	 * 
	 * @param name the name of the property
	 * @param value the value to set the property to
	 * 
	 * @return IllegalArgumentException if the name of value given is null
	 */
	public String setProperty(String name, String value);
	
	/**
	 * Returns an iterator containing the key-value pairs of all the properties
	 * the book has.
	 * 
	 * @return an iterator of properties
	 */
	public Iterator<Map.Entry<String, String>> enumerateProperties();

}

