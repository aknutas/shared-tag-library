import java.util.*;

public class VirtualBookshelf implements Bookshelf {

	private String name;
	private Set<Book> bookshelf;
	private Set<Bookshelf> shelves;

	public VirtualBookshelf(String name) throws IllegalArgumentException {
		if(null == name)
			throw new IllegalArgumentException("name cannot be null");
		
		this.bookshelf = new HashSet<Book>();
		this.shelves = new HashSet<Bookshelf>();
	}

	public void insert(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");

		this.bookshelf.add(book);
	}

	public void remove(Book book) throws IllegalArgumentException {
		if(null == book)
			throw new IllegalArgumentException("book cannot be null");

		this.bookshelf.add(book);
	}

	public boolean empty() {
		for(Bookshelf shelf : this.shelves) {
			if(!shelf.empty())
				return false;
		}

		return this.bookshelf.isEmpty();
	}

	public int size() {
		int size = 0;

		for(Bookshelf shelf : this.shelves)
			size += shelf.size();

		return (size + this.bookshelf.size());
	}

	public Iterator<Book> enumerate() {
		// need an iterator that traverses other iterators
		return null;
	}

	public String getName() {
		return this.name;
	}

	public String setName(String name) throws IllegalArgumentException {
		String temp = this.name;
		this.name = name;
		return name;
	}

	public Bookshelf union(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		if(shelf instanceof VirtualBookshelf) {
			// merge
		}

		// use virtual bookshelf to combine non-similar types
		return null;
	}

	public Bookshelf intersect(Bookshelf shelf) {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		if(shelf instanceof VirtualBookshelf) {
			// merge
		}

		return null;
	}

	public Bookshelf difference(Bookshelf shelf) {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		return null;
	}

	public Bookshelf subset(Bookshelf shelf, Book book) throws IllegalArgumentException {
		return null;

	}

	public void addBookshelf(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");

		this.shelves.add(shelf);
	}

	public void removeBookshelf(Bookshelf shelf) throws IllegalArgumentException {
		if(null == shelf)
			throw new IllegalArgumentException("shelf cannot be null");
		
		this.shelves.remove(shelf);
	}

}

