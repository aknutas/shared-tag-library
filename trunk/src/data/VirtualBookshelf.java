package data;

import java.util.*;
import java.util.Map.*;
import javax.jdo.annotations.*;
import database.*;

/**
 * The VirtualBookshelf class implements the Bookshelf interface and is used
 * for in-memory, temporary Bookshelf objects. Most temporary Bookshelves will
 * be used for browsing/searching persistent bookshelves.
 * 
 * @author Andrew Alm
 */
@PersistenceCapable(detachable="true")
public class VirtualBookshelf implements Bookshelf {

	private Set<Book> bookshelf;
	private Set<Bookshelf> shelves;
	private Map<String, String> properties;

	/**
	 * Creates a new Bookshelf object which contains no books and has no
	 * properties.
	 * 
	 * @throws IllegalArgumentException if the name given is null
	 */
	public VirtualBookshelf() throws IllegalArgumentException {
		/* initialize containers */
		this.bookshelf = new HashSet<Book>();
		this.shelves = new HashSet<Bookshelf>();
		this.properties = new HashMap<String, String>();
		
		//(new QueryBuilderImpl()).shelfStore(this);
	}
	
	/**
	 * Creates a new Bookshelf object which contains no books.
	 * 
	 * @throws IllegalArgumentException if the name given is null
	 */
	public VirtualBookshelf(String name) throws IllegalArgumentException {
		this();
		
		if(null == name)
			throw new IllegalArgumentException("name given is null");
		
		this.setProperty("name", name);
	}

	/**
	 * Inserts a book onto the VirtualBookshelf, if the book already exists
	 * on the Bookshelf then this method does nothing.
	 * 
	 * @param book the book to add to the bookshelf.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public void insert(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");

		this.bookshelf.add(book);
	}

	/**
	 * Removes a book from the VirtualBookshelf, if and only if the book was
	 * added to this VirtualBookshelf (and not another bookshelf contained
	 * within). If the Bookshelf does not contain the given book, then this
	 * method does nothing.
	 * 
	 * @param book the book to remove from the bookshelf.
	 * 
	 * @return true if the book was removed, otherwise false.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public boolean remove(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");

		return false;
	}

	public boolean contains(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");
		
		return this.bookshelf.contains(book);
	}
	
	/**
	 * Determines if the virtual bookshelf is empty.
	 * 
	 * @return true if the book shelf is empty, otherwise false.
	 */
	public boolean empty() {
		for(Bookshelf shelf : this.shelves) {
			if(!shelf.empty())
				return false;
		}

		return this.bookshelf.isEmpty();
	}

	/**
	 * Determines the size of the bookshelf. A virtual bookshelf is the size of
	 * all the books it contains plus the size of all bookshelf objects it
	 * contains, the size may not reflect the exact number of books aviable.
	 * 
	 * @return the size of the shelf.
	 */
	public int size() {
		int size = 0;

		for(Bookshelf shelf : this.shelves)
			size += shelf.size();

		return (size + this.bookshelf.size());
	}

	/**
	 * Returns an Iterator of all the books on the bookshelf. This includes
	 * all eligible books on bookshelves contained within this virtual 
	 * bookshelf.
	 * 
	 * @return an Iterator of books
	 */
	public Iterator<Book> enumerate() {
		return new VirtualBookshelfIterator(this.shelves.iterator(), this.bookshelf.iterator());
	}

	/**
	 * Joins two Bookshelves.
	 * 
	 * @param shelf the bookshelf to wed.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		VirtualBookshelf newShelf = new VirtualBookshelf("");
		this.shallowCopyInto(newShelf);

		if(shelf instanceof VirtualBookshelf) 
			((VirtualBookshelf)shelf).shallowCopyInto(newShelf);
		else
			newShelf.addBookshelf(shelf);
		
		return newShelf;
	}

	/**
	 * Computes the intersection of two bookshelf objects and returns a new
	 * Bookshelf.
	 * 
	 * @param shelf the bookshelf to intersect with.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf intersect(Bookshelf shelf) {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		if(shelf instanceof VirtualBookshelf) {
			// merge
		}

		return null;
	}

	/**
	 * Computes the difference of two bookshelfs returning a new bookshelf
	 * containing the result.
	 * 
	 * @param shelf the bookshelf to difference with.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	public Bookshelf difference(Bookshelf shelf) {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		return null;
	}

	/**
	 * Computes the subset of books contained in a Bookshelf that match part of
	 * the given book.
	 * 
	 * @param book the model book.
	 * 
	 * @throws IllegalArgumentException if the book given is null.
	 */
	public Bookshelf subset(Book book) throws IllegalArgumentException {
		return null;
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
	
	/**
	 * Adds a bookshelf to this virtual bookshelf, if the shelf already exists
	 * in this VirtualBookshelf, then this method does nothing.
	 *
	 * @param shelf the shelf to add.
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	protected void addBookshelf(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		this.shelves.add(shelf);
	}

	/**
	 * Removes a bookshelf from this virtual bookshelf, if the VirtualBookshelf
	 * already contains the shelf then this method does nothing.
	 * 
	 * @param shelf the shelf to remove from the VirtualBookshelf
	 * 
	 * @throws IllegalArgumentException if the shelf given is null.
	 */
	protected void removeBookshelf(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");
		
		this.shelves.remove(shelf);
	}
	
	/**
	 * Returns an iterator of all the bookshelves contained in this virtual
	 * bookshelf.
	 *
	 * @return an iterator of books
	 */
	protected Iterator<Bookshelf> enumerateBookshelves() {
		return this.shelves.iterator();
	}
	
	/**
	 * Performs a shallow copy of itself into the given VirtualBookshelf.
	 * 
	 * @return a new Bookshelf identical to this.
	 */
	protected VirtualBookshelf shallowCopyInto(VirtualBookshelf toShelf) throws IllegalArgumentException {
		if(null == toShelf)
			throw new IllegalArgumentException("toShelf cannot be null");

		for(Bookshelf shelf : this.shelves)
			toShelf.addBookshelf(shelf);
		
		for(Book book : this.bookshelf)
			toShelf.insert(book);
		
		return toShelf;
	}

	/* test cases */
	public static void main(String []args) {
		VirtualBookshelf a = new VirtualBookshelf("a");
		VirtualBookshelf b = new VirtualBookshelf("b");
		
		a.insert(new VirtualBook("a", "b"));
		a.insert(new VirtualBook("c", "d"));
		a.insert(new VirtualBook("e", "f"));
		a.insert(new VirtualBook("g", "h"));
		a.insert(new VirtualBook("i", "j"));
		a.insert(new VirtualBook("k", "l"));
		
		b.insert(new VirtualBook("m", "n"));
		b.insert(new VirtualBook("o", "p"));
		b.insert(new VirtualBook("q", "r"));
		b.insert(new VirtualBook("s", "t"));
		b.insert(new VirtualBook("u", "v"));
		b.insert(new VirtualBook("w", "x"));
		
		Iterator<Book> it = a.enumerate();
		int i = 0;
		
		for(Book book = it.next(); it.hasNext(); book = it.next()) {
			System.out.println(book.getProperty("author") + " - " + book.getProperty("title"));
			i += 1;
		}
		
		assert(i == a.size());
		
		it = b.enumerate();
		i = 0;
		
		for(Book book = it.next(); it.hasNext(); book = it.next()) {
			System.out.println(book.getProperty("author") + " - " + book.getProperty("title"));
			i += 1;
		}
		
		assert(i == b.size());
		
		Bookshelf c = a.union(b);
		it = c.enumerate();
		System.out.println(it.hasNext());
		i = 0;
		
		for(Book book = it.next(); it.hasNext(); book = it.next()) {
			System.out.println(book.getProperty("author") + " - " + book.getProperty("title"));
			i += 1;
		}
	}
}