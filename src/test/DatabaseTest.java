package test;

import database.*;
import data.*;

import java.util.*;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

public class DatabaseTest {

	/**
	 * Note: The unit tests work only on a completely empty database. Clutter will show up as errors.
	 */
	public static void main(String[] args) {
		QueryBuilder qb = new QueryBuilderImpl();
		Book book;
		Bookshelf shelf;
		List<Bookshelf> returnlist;
		Bookshelf testshelf;

		shelf = new VirtualBookshelf("");
		shelf.setProperty("name", "Plop");
		book = new VirtualBook("Diiba", "Daaba");
		shelf.insert(book);

		/*
		qb.shelfStore(shelf);
		returnlist = qb.shelfSearchByBookName("Diiba");
		testshelf = (Bookshelf)returnlist.get(0);
		
		assert testshelf.getProperty("name").equals(shelf.getProperty("name"));
		assert returnlist.size()==1;
		*/
		
		for(int i=0; i<100; i++)
		{
			book = new VirtualBook(Integer.toString(i), Integer.toString(i));
			for(int j=0; j<5; j++)
			{
				book.tag(Integer.toString(j));
			}
			shelf.insert(book);
		}
		
		qb.shelfStore(shelf);
		returnlist = qb.shelfList();
		testshelf = (Bookshelf)returnlist.get(0);
		
		assert testshelf.getProperty("name").equals(shelf.getProperty("name"));
		assert testshelf.size()==101;
		assert (returnlist.size()==1);
		
		Iterator<Book> e = shelf.enumerate();
		
		while(e.hasNext())
		{
			System.out.println(e.next().getProperty("title"));
		}
		
		/*
		returnlist = qb.shelfSearchByBookName("Daaba");
		assert (returnlist.size()==0);
		*/
	}

}
