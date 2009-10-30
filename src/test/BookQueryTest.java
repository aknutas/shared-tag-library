package test;

import java.util.*;
import java.util.Map.*;

import data.*;

public class BookQueryTest {

	public static void main(String []args) {
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
		
		Iterator<Book> bookIter = shelf.enumerate();
		while(bookIter.hasNext()) {
			Book book = bookIter.next();
			System.out.println("ADSFADSF");
			if(query.equals(book))
				System.out.println("Found: " + book.getProperty("title") + " by " + book.getProperty("author"));
		}
		

	}
	
}
