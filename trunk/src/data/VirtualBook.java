package data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jdo.annotations.PersistenceCapable;

/**
 * The VirtualBook class implements the Book interface and represents a Book
 * which exists only in memory.
 *
 * @author AndrewAlm
 */
@PersistenceCapable(detachable="true")
public class VirtualBook implements Book, Serializable {

	private static final long serialVersionUID = 1L;

	protected Map<String, Integer> tags;
	protected Map<String, String> properties;

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
	 * Creates a VirtualBook object from the given Map of tags and 
	 * properties. This is a protected method and is used only inside
	 * this package.
	 * 
	 * @param tags the tags Map to use
	 * @param properties the properties Map to use
	 * 
	 * @throws NullPointerException if the tags or properties given 
	 *         are null.
	 */
	protected VirtualBook(Map<String, Integer> tags, Map<String, String> properties) throws NullPointerException {
		if(null == tags || null == properties)
			throw new NullPointerException("tags or properties cannot be null");
		
		this.tags = tags;
		this.properties = properties;
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
	public Iterable<Map.Entry<String, Integer>> enumerateTags() {
		return this.tags.entrySet();
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
		
		String oldValue = this.properties.put(name, value);

		/* update tags */
		this.tag(value);
		if(null != oldValue)
			this.untag(oldValue);
		
		return oldValue;
	}
	
	/**
	 * Get the number of tags that are associated with this Book
	 * object.
	 * 
	 * @return the number of tags
	 */
	public int getTagCount() {
		return this.tags.size();
	}

	/**
	 * Returns an iterator containing the key-value pairs of all the properties
	 * the book has.
	 * 
	 * @return an iterator of properties
	 */
	public Collection<Entry<String, String>> enumerateProperties() {
		return this.properties.entrySet();
	}
	
	/**
	 * An overridden equals method. This is used to compare two book
	 * objects to each other.
	 * 
	 * @param object the Object to check the equality of
	 */
	@Override
	public boolean equals(Object object) throws NullPointerException {
		if(null == object || !(object instanceof Book))
			return false;
		
		Book book = (Book)object;
		for(Entry<String, String> property : book.enumerateProperties()) {
			if(!this.properties.containsKey(property.getKey()))
				return false;
			
			if(!property.getValue().equals(this.properties.get(property.getKey())))
				return false;
		}
		
		return true;
	}

	/**
	 * Returns a VirtualBook object of this Book interface object. If
	 * the book is already a VirtualBook then the Book is returned
	 * and no copy is made.
	 * 
	 * @return a VirtualBook
	 */
	@Override
	public VirtualBook makeVirtual() {
		return this;
	}

}

