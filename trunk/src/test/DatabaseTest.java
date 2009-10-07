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

		shelf = new VirtualBookshelf();
		shelf.setName("Plop");
		book = new VirtualBook("Diiba", "Daaba");
		shelf.insert(book);
		
		qb.shelfCreate(shelf);
		List returnlist = qb.shelfSearch("Diiba");
		Bookshelf testshelf = (Bookshelf)returnlist.get(0);
		
		assert testshelf.getName().equals(shelf.getName());
		assert returnlist.size()==1;
		
		for(int i=0; i<100; i++)
		{
			book = new VirtualBook(Integer.toString(i), Integer.toString(i));
			for(int j=0; j<5; j++)
			{
				book.tag(Integer.toString(j));
			}
			shelf.insert(book);
		}
		
		qb.shelfCreate(shelf);
		returnlist = qb.shelfList();
		testshelf = (Bookshelf)returnlist.get(0);
		
		assert testshelf.getName().equals(shelf.getName());
		assert testshelf.size()==101;
		assert (returnlist.size()==1);
		
		returnlist = qb.shelfSearch("Daaba");
		assert (returnlist.size()==0);
	}

}
