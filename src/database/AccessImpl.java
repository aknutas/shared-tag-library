package database;

import javax.jdo.*;
import java.util.*;

/**
 * Class AccessImpl
 * 
 * @author Antti Knutas
 * 
 */
class AccessImpl implements Access {

	//
	// Fields
	//

	private database.AccessImpl instance;
	private PersistenceManager pm;
	private PersistenceManagerFactory pmf;

	//
	// Constructors
	//
	private AccessImpl () {
		//It says what it does, bud!
		initialize();
	};

	public void finalize ()
	{

	}

	//
	// Methods
	//

	/**
	 * Takes a string as a query, and returns the result as an array of objects.
	 * @return       List Result list of objects.
	 * @param        querystring Query, formatted by QueryBuilder.
	 */
	@SuppressWarnings("unchecked")
	public synchronized List query( String querystring )
	{
		Transaction tx=pm.currentTransaction();
		Query q=pm.newQuery("javax.jdo.query.JDOQL", querystring);
		List results = null;
		
        try
        {
        	//Beginning transaction
            tx.begin();
            results = (List)q.execute();
            tx.commit();
        }
        catch (Exception e)
        {
        System.out.println(e);	
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
            return results;
        }
	}


	/**
	 * Persists the objects in the list, according to the query parameters.
	 * @return       int Success status.
	 * @param        objects List of objects to be persisted.
	 */
	@SuppressWarnings("unchecked")
	public synchronized int commit( List objects )
	{
		int returnvalue = 1;
        Transaction tx=pm.currentTransaction();
        try
        {
        	//Beginning transaction
            tx.begin();
            pm.makePersistentAll(objects);
            tx.commit();
        }
        catch (Exception e)
        {
        returnvalue = 0;	
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
        return returnvalue;
	}


	/**
	 * Returns 1 in case of a successful initialization.
	 * @return       int
	 */
	private int initialize(  )
	{
		//Trying to create the persistance manager
		try {
			PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
			PersistenceManager pm = pmf.getPersistenceManager();
			pm.setDetachAllOnCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}


	/**
	 * Returns the singleton object reference.
	 * @return       Database.Access
	 */
	public synchronized database.AccessImpl access(  )
	{
		if (instance==null)
		{
			instance = new AccessImpl();
		}
		return instance;
	}


}
