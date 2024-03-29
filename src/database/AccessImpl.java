package database;

import javax.jdo.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Interface Access The singleton object that both manages and queues access for
 * the database.
 * 
 * It is synchronized for relatively safe multithreading.
 * 
 * Instance of this is protected with AccessImpl, use AccessImpl.getInstance()
 * to access.
 * 
 * @author Antti Knutas
 * 
 */
public class AccessImpl implements Access {

    private static database.Access instance;
    private PersistenceManager pm;
    private PersistenceManagerFactory pmf;

    protected AccessImpl() {
	initialize();
    };

    /**
     * Shuts down all database connections and removes the local reference to
     * singleton instance.
     */
    public void shutdown() {
	pm.close();
	pmf.close();
	try {
	    DriverManager.getConnection("jdbc:derby:;shutdown=true");
	} catch (SQLException e) {
	    // Error code 50000 means proper shutdown. That's a good exception,
	    // mmm'kay?
	    if (e.getErrorCode() != 50000)
		e.printStackTrace();
	}
	pm = null;
	pmf = null;
    }

    /**
     * Takes a string as a query, and returns the result as an array of objects.
     * 
     * @return List Result list of objects.
     * @param querystring
     *            Query, formatted by QueryBuilder.
     */
    @SuppressWarnings("unchecked")
    public synchronized List query(String querystring) {
	Transaction tx = pm.currentTransaction();
	Query q = pm.newQuery(querystring);
	List queryresults = null;

	try {
	    // Beginning transaction
	    tx.begin();
	    queryresults = (List) q.execute();
	    tx.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    if (tx.isActive()) {
		tx.rollback();
	    }
	}
	ArrayList results = null;
	if (queryresults == null)
	    results = new ArrayList();
	else
	    results = new ArrayList(queryresults);
	q.closeAll();

	return results;
    }

    /**
     * Persists a single object.
     * 
     * @return int Success status.
     * @param object
     *            The persisted object.
     */
    public synchronized int commitOne(Object object) {
	int returnvalue = 1;
	Transaction tx = pm.currentTransaction();
	try {
	    // Beginning transaction
	    tx.begin();
	    pm.makePersistent(object);
	    tx.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    returnvalue = 0;
	} finally {
	    if (tx.isActive()) {
		tx.rollback();
	    }
	}
	return returnvalue;
    }

    /**
     * Persists the objects in the list.
     * 
     * @return int Success status.
     * @param objects
     *            List of objects to be persisted.
     */
    @SuppressWarnings("unchecked")
    public synchronized int commitCollection(Collection objects) {
	int returnvalue = 1;
	Transaction tx = pm.currentTransaction();
	try {
	    // Beginning transaction
	    tx.begin();
	    pm.makePersistentAll(objects);
	    tx.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    returnvalue = 0;
	} finally {
	    if (tx.isActive()) {
		tx.rollback();
	    }
	}
	return returnvalue;
    }

    /**
     * Returns 1 in case of a successful initialization.
     * 
     * @return int Something remarkable
     */
    private synchronized int initialize() {
	// Trying to create the persistence manager
	try {
	    pmf = JDOHelper
		    .getPersistenceManagerFactory("datanucleus.properties");
	    pm = pmf.getPersistenceManager();
	    pm.getFetchPlan().setMaxFetchDepth(-1);
	    pm.setDetachAllOnCommit(true);
	} catch (Exception e) {
	    e.printStackTrace();
	    return 0;
	}
	return 1;
    }

    /**
     * Returns the singleton object reference.
     * 
     * @return Database.Access Singleton object reference
     */
    public synchronized static database.Access getInstance() {
	if (instance == null) {
	    instance = new AccessImpl();
	}
	return instance;
    }

    /**
     * Removes a single object from the persistence graph.
     * 
     * @return int Success status.
     * @param object
     *            The persisted object.
     */
    public synchronized int removeOne(Object object) {
	int returnvalue = 1;
	Transaction tx = pm.currentTransaction();
	try {
	    // Beginning transaction
	    tx.begin();
	    pm.deletePersistent(object);
	    tx.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    returnvalue = 0;
	} finally {
	    if (tx.isActive()) {
		tx.rollback();
	    }
	}
	return returnvalue;
    }

}
