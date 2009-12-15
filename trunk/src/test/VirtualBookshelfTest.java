package test;

import java.util.Iterator;

import data.Book;
import data.Bookshelf;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;
import data.PersistentLibrary;
import database.QueryBuilderImpl;

public class VirtualBookshelfTest {

	/* test case */
	public static void main(String []args) {
		VirtualLibrary lib = new PersistentLibrary(new QueryBuilderImpl());
		VirtualBookshelf a = new VirtualBookshelf("a");
		VirtualBookshelf b = new VirtualBookshelf("b");
		lib.addBookshelf(a);
		lib.addBookshelf(b);
		
		
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
		
		
		Bookshelf shelf = lib.getBookshelf("a");
		shelf.insert(new VirtualBook("test", "test"));
		((PersistentLibrary)lib).saveBookshelf(new VirtualBookshelf("c"));
		
		for(Bookshelf s : lib) 
			System.out.println(s.getProperty("name"));
		
		
		
		
		Iterator<Book> it = a.enumerate();
		int i = 0;
		
		for(Book book = it.next(); it.hasNext(); book = it.next()) {
			System.out.println(book.getProperty("author") + " - " + book.getProperty("title"));
			i += 1;
		}
		System.out.println("asdfasfd");
		assert(i == a.size());
		
		it = b.enumerate();
		i = 0;
		
		for(Book book = it.next(); it.hasNext(); book = it.next()) {
			System.out.println(book.getProperty("author") + " - " + book.getProperty("title"));
			i += 1;
		}
		
		assert(i == b.size());
		
		Bookshelf c = a.union(b);
		it = c.iterator();
		System.out.println(it.hasNext());
		i = 0;
		
		for(Book book = it.next(); it.hasNext(); book = it.next()) {
			System.out.println(book.getProperty("author") + " - " + book.getProperty("title"));
			i += 1;
		}
	}
	
}
