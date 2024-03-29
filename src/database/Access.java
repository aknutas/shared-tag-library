package database;

import java.util.Collection;
import java.util.List;

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
public interface Access {

    /**
     * Orders the persistence manager to close connections and finish. Call
     * right before program halt.
     */
    public void shutdown();

    /**
     * Takes a string as a query, and returns the result as an array of objects.
     * 
     * @return List Result list of objects.
     * @param querystring
     *            Query, formatted by QueryBuilder.
     */
    @SuppressWarnings("unchecked")
    public List query(String querystring);

    /**
     * Persists a single object.
     * 
     * @return int Success status.
     * @param object
     *            The persisted object.
     */
    public int commitOne(Object object);

    /**
     * Removes a single object from the persistence graph.
     * 
     * @return int Success status.
     * @param object
     *            The persisted object.
     */
    public int removeOne(Object object);

    /**
     * Persists the objects in the list.
     * 
     * @return int Success status.
     * @param objects
     *            List of objects to be persisted.
     */
    @SuppressWarnings("unchecked")
    public int commitCollection(Collection objects);

}
