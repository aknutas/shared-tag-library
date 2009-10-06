package data;

import java.util.*;
//import javax.jdo.annotations.PersistenceCapable;

/**
 * The VirtualBook class implements the Book interface and represents a Book
 * which exists only in memory.
 *
 * @author AndrewAlm
 */
//@PersistenceCapable
public class VirtualBook implements Book {

	private String author;
	private String title;

	private Map<String, Integer> tags;

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
		this.title = title;
		this.author = author;
		this.tags = new HashMap<String, Integer>();
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
	 * Gets the author of the book.
	 *
	 * @return string containing the author's name
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Sets the author of the book to the given value.
	 *
	 * @param author string containing the author's name
	 *
	 * @return the previous value of author
	 *
	 * @throws IllegalArgumentException if the author is null
	 */
	public String setAuthor(String author) throws IllegalArgumentException {
		if(null == author)
			throw new IllegalArgumentException("author cannot be null");

		String prev = this.author;
		this.author = author;
		return prev;
	}

	/**
	 * Gets the title of the book.
	 *
	 * @return string containing the title of the book
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title of the book to the given value.
	 *
	 * @param title string containing the title of the book
	 *
	 * @return the previous value of title
	 *
	 * @throws IllegalArgumentException if the title is null
	 */
	public String setTitle(String title) throws IllegalArgumentException {
		if(null == title)
			throw new IllegalArgumentException("title cannot be null");

		String prev = this.title;
		this.title = title;
		return prev;
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

		assert b.getTitle().equals("Elements");
		assert b.getAuthor().equals("Euclid");

		b.setTitle("For Whom the Bell Tolls");
		b.setAuthor("Earnest Hemingway");

		assert b.getTitle().equals("For Whom the Bell Tolls");
		assert b.getAuthor().equals("Earnest Hemingway");
	}

}

