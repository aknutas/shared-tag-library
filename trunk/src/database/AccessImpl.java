package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;


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
	public synchronized List query( String querystring )
	{
		return null;
	}


	/**
	 * Persists the objects in the list, according to the query parameters.
	 * @return       int Success status.
	 * @param        objects List of objects to be persisted.
	 * @param        query The save query, if necessary.
	 */
	public synchronized int commit( List objects, String query )
	{
		return 0;
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
