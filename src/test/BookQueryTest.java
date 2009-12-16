package test;

import data.Book;
import data.BookQuery;
import data.Bookshelf;
import data.VirtualBook;
import data.VirtualBookshelf;

public class BookQueryTest {

    public static void main(String[] args) {
	Bookshelf shelf = new VirtualBookshelf();
	BookQuery query = new BookQuery();

	query.match(".*Euclid.*");
	query.match("Alighier", true);

	shelf.insert(new VirtualBook("Elements", "Euclid"));
	shelf.insert(new VirtualBook("Odyssey", "Homer"));
	shelf.insert(new VirtualBook("Illiad", "Homer"));
	shelf.insert(new VirtualBook("Divine Comedy", "Alighier"));
	shelf.insert(new VirtualBook("A Book About Euclid", "Aristotle"));
	shelf.insert(new VirtualBook("Homer Euclid", "Alighier"));

	for (Book book : shelf) {
	    if (query.equals(book))
		System.out.println("1Found: " + book.getProperty("title")
			+ " by " + book.getProperty("author"));
	}

	System.out.println("sdaf");
	Bookshelf shelf2 = shelf.subset(query);
	for (Book book : shelf2)
	    System.out.println("2Found: " + book.getProperty("title") + " by "
		    + book.getProperty("author"));
    }

	//@Test
	public void testQuery() {
		
	}
	
}
