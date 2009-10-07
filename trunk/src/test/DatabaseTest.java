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
	 * @param args
	 */
	public static void main(String[] args) {
		QueryBuilder qb = new QueryBuilderImpl();
		Book book;
		Bookshelf shelf;

		shelf = new VirtualBookshelf();
		shelf.setName("Plop");
		book = new VirtualBook("Diiba", "Daaba");
		shelf.insert(book);

		/*
		PersistenceManager pm;
		PersistenceManagerFactory pmf;
		pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
		pm = pmf.getPersistenceManager();
		pm.getFetchPlan().setMaxFetchDepth(-1);
		
		Transaction tx=pm.currentTransaction();
		try
		{
			//Beginning transaction
			tx.begin();
			pm.makePersistent(shelf);
			tx.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
		*/
		
		qb.shelfCreate(shelf);
		qb.shelfSearch("Diiba");
	}

}
