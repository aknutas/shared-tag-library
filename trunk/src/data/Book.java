package data;

import java.util.Iterator;
import java.util.Map;

/**
 * The Book interface represents a single book. Each book has an author, title,
 * and description. A book can have 0 or more tags associated with it, each tag
 * is weighted by adding and removing tags.  A book can also have properties
 * attached to it to represent name-value pairs of data, such as an author
 * property containing the author's name.
 *
 * @author Andrew Alm
 */
public interface Book extends Properties {

	/**
	 * The weight of the given tag is increased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag.
	 *
	 * @throws NullPointerException if the tag given is null
	 */
	public int tag(String tag) throws NullPointerException;

	/**
	 * The weight of the given tag is decreased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws NullPointerException if the tag given is null
	 */
	public int untag(String tag) throws NullPointerException;

	/**
	 * Gets the weight of a given tag. If the tag has never been used 
	 * with this VirtualBook then the weight will be zero. (Note: A 
	 * return value of zero does not necessarily indicate that a tag
	 * has never been used with the book.)
	 *
	 * @param tag the name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws NullPointerException if the tag given is null
	 */
	public int weight(String tag) throws NullPointerException;

	/**
	 * Get the number of tags that are associated with this Book
	 * object.
	 * 
	 * @return the number of tags
	 */
	public int getTagCount();
	
	/**
	 * Returns an iterator of all tags on a book and their associated
	 * weights.
	 * 
	 * @return an Iterator of map entries.
	 */
	public Iterable<Map.Entry<String, Integer>> enumerateTags();
	
	
	/**
	 * Returns a VirtualBook object of this Book interface object. If
	 * the book is already a VirtualBook then the Book is returned
	 * and no copy is made.
	 * 
	 * @return a VirtualBook
	 */
	public VirtualBook makeVirtual();

}

