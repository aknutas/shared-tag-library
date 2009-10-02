package database;

import java.util.ArrayList;
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


	//
	// Accessor methods
	//

	//
	// Other methods
	//

	/**
	 * Takes a string as a query, and returns an array of objects.
	 * @return       ArrayList
	 * @param        querystring
	 */
	public ArrayList query( String querystring )
	{
		return null;
	}


	/**
	 * Persists the objects in the list, according to the query parameters.
	 * @return       int
	 * @param        objects
	 * @param        query
	 */
	public int commit( ArrayList objects, String query )
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
			PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("META-INF/datanucleus.properties");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}


	/**
	 * @return       Database.AccessImpl
	 */
	public database.AccessImpl access(  )
	{
		if (instance==null)
		{
			instance = new AccessImpl();
		}
		return instance;
	}


}
