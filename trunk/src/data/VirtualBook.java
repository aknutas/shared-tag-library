package data;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import javax.jdo.annotations.*;

/**
 * The VirtualBook class implements the Book interface and represents a Book
 * which exists only in memory.
 *
 * @author AndrewAlm
 */
@PersistenceCapable(detachable="true")
public class VirtualBook implements Book, Serializable {

	private static final long serialVersionUID = 1L;

	private Map<String, Integer> tags;
	private Map<String, String> properties;

	/**
	 * Creates a new VirtualBook with the given author and title.
	 *
	 * @param title the title of the book
	 * @param author the author of the book
	 *
	 * @throws IllegalArgumentException if the author or title given are
	 *         null.
	 */
	public VirtualBook(String title, String author) throws IllegalArgumentException {
		if(null == author || null == title)
			throw new IllegalArgumentException("author and title cannot be null");

		/* initialize variables */		
		this.tags = new HashMap<String, Integer>();
		this.properties = new HashMap<String, String>();
		
		this.setProperty("title", title);
		this.setProperty("author", author);
	}

	/**
	 * The weight of the given tag is increased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag.
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int tag(String tag) throws IllegalArgumentException {
		if(null == tag)
			throw new IllegalArgumentException("tag cannot be null");

		Integer weight = this.tags.get(tag);
		if(null == weight)
			weight = new Integer(0);

		weight = new Integer(weight.intValue() + 1);
		this.tags.put(tag, weight);
		
		return weight.intValue();
	}

	/**
	 * The weight of the given tag is decreased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int untag(String tag) throws IllegalArgumentException {
		if(null == tag)
			throw new IllegalArgumentException("tag cannot be null");

		Integer weight = this.tags.get(tag);
		if(null == weight)
			weight = new Integer(0);

		/* this allows for negative weights, i dont have a problem with this */
		weight = new Integer(weight.intValue() - 1);
		this.tags.put(tag, weight);

		return weight.intValue();
	}

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
	 * @throws IllegalArgumentException if the tag given is null
	 */
	public int weight(String tag) throws IllegalArgumentException {
		if(null == tag)
			throw new IllegalArgumentException("tag cannot be null");

		Integer weight = this.tags.get(tag);
		if(null == weight)
			weight = new Integer(0);

		return weight;
	}

	/**
	 * Returns an iterator containing all the of a book and their
	 * given weights.
	 * 
	 * @return an Iterator of tags and weights.
	 */
	public Iterator<Map.Entry<String, Integer>> enumerateTags() {
		return this.tags.entrySet().iterator();
	}
	
	/**
	 * Gets the value of the given property. If the given property does not
	 * exist then null is returned.
	 * 
	 * @param name the name of the property to get.
	 * 
	 * @return the value of the property
	 * 
	 * @throws IllegalArgumentException if the property given is null
	 */
	public String getProperty(String name) throws IllegalArgumentException {
		if(null == name)
			throw new IllegalArgumentException("name cannot be null");
		
		return this.properties.get(name);
	}
	
	/**
	 * Sets the named property to the given value, returning the old value for
	 * the property. If the Book did not have the property, null returned.
	 * 
	 * @param name the name of the property
	 * @param value the value to set the property to
	 * 
	 * @return the old property value
	 * 
	 * @throws IllegalArgumentException if the name of value given is null
	 */
	public String setProperty(String name, String value) {
		if(null == name || null == value)
			throw new IllegalArgumentException("name or value cannot be null");
		
		this.tag(value);
		
		return this.properties.put(name, value);
	}

	/**
	 * Returns an iterator containing the key-value pairs of all the properties
	 * the book has.
	 * 
	 * @return an iterator of properties
	 */
	public Iterator<Entry<String, String>> enumerateProperties() {
		return this.properties.entrySet().iterator();
	}

	/* unit tests */
	public static void main(String []args) {
		Book b = new VirtualBook("Elements", "Euclid");

		assert 1 == b.tag("foo");
		assert 1 == b.weight("foo");

		assert -1 == b.untag("bar");
		assert -1 == b.weight("bar");

		for(int i = 0; i < 50; ++i)
			b.tag("foobar");

		assert 50 == b.weight("foobar");

		for(int i = 0; i < 25; ++i)
			b.untag("foobar");

		assert 25 == b.weight("foobar");
		
		assert b.getProperty("title").equals("Elements");
		assert b.getProperty("author").equals("Euclid");

	}

}

