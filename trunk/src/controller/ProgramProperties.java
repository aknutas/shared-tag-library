package controller;

import java.io.*;
import java.util.*;

import javax.jdo.annotations.PersistenceCapable;

import scripts.DefaultProperties;

import database.QueryBuilder;
import database.QueryBuilderImpl;

/**
 * The ProgramProperties class is used to load and store all program properties
 * which may be changed by the user. To ensure no name conflicts prepend the
 * package name followed '::' to each property the package requires. For if the
 * network layer requires a property called 'timeout' then 'network::timeout'
 * should be used. If a property is to be used by more than one package then it
 * should be put in the 'global' namespace.
 * 
 * Below is a list of properties the program stores (please add):
 * 
 * 	1. butler package
 * 	2. controller package
 * 		controller::connections  the map of connection names to alias
 * 	3. data package
 * 		data::timeout		the default timeout of a remote object
 *		data::iter_block	the fetch block size for remote iterators
 *	4. database package
 *	5. main package
 *	6. operations package
 *	7. scripts package
 *	8. global
 *	9. network package
 *		network::timeout	Connection timeout
 *		network::so_timeout	Timeout when transferring data
 *		network::port		Listening port
 * 
 * @author Andrew Alm
 */

@PersistenceCapable(detachable = "true")
public class ProgramProperties {

    private Map<String, Object> properties = new HashMap<String, Object>();
    private static ProgramProperties instance;

    /**
     * Returns the singleton object reference. Use this to access the object.
     * 
     * @return ProgramProperties Singleton object reference
     */
    public synchronized static ProgramProperties getInstance() {
	// Checking to see if we can load up the instance from the database.
	if (instance == null) {
	    QueryBuilder qb = new QueryBuilderImpl();
	    instance = qb.getProperties();
	}
	// Don't put "else if" here. qb.getProperties can return null.
	if (instance == null)
	    instance = new ProgramProperties();
	return instance;
    }

    /**
     * The default constructor. If the class cannot be loaded from database in
     * the getInstance method, this will populate the class with the default
     * settings.
     */
    protected ProgramProperties() {
	DefaultProperties dp = new scripts.DefaultProperties(this);
	dp.generate();
    };

    /**
     * This method is used to to store all properties to the database backend.
     */
    public synchronized void saveProperties() {
	QueryBuilder qb = new QueryBuilderImpl();
	qb.storeProperties(instance);
    }

    /**
     * Sets the given property name to the given value, returning the previous
     * value, or null if no previous value. If the value given is null then the
     * Property will be removed.
     * 
     * @param name
     *            the name of the property to set
     * @param value
     *            the value to set the property to
     * 
     * @return the previous value of the property
     */
    public synchronized Object setProperty(String name, Serializable value)
	    throws NullPointerException {
	if (null == name)
	    throw new NullPointerException("name cannot be null");

	Object obj = null;
	if (null == value)
	    obj = properties.remove(name);
	else
	    obj = properties.put(name, value);

	return obj;
    }

    /**
     * Gets the property of the given name from the property map. If the
     * property does not exist in the property map then null is returned.
     * 
     * @param name
     *            the name of the property
     * 
     * @return the Object from the property map, or null if not found
     * 
     * @throws NullPointerException
     *             if the name given is null
     */
    public synchronized Object getProperty(String name)
	    throws NullPointerException {
	if (name == null)
	    throw new NullPointerException("name cannot be null");

	// Debug
	// System.out.println("Returned property " + name + " value " +
	// properties.get(name));

	return properties.get(name);
    }

    /**
     * This method is used to serialize an object into a byte array to store in
     * the database.
     * 
     * @param object
     *            the object to convert to a byte array
     * 
     * @return a byte array representing the object, or null on error
     * 
     * @throws NullPointerException
     *             if the object given is null
     */
    @SuppressWarnings("unused")
    @Deprecated
    private byte[] serialize(Serializable object) throws NullPointerException {
	if (object == null)
	    throw new NullPointerException("object cannot be null");

	try {
	    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	    (new ObjectOutputStream(byteStream)).writeObject(object);
	    return byteStream.toByteArray();
	} catch (IOException ex) {
	    return null;
	}
    }

    /**
     * This method is used to convert a byte array into an Object.
     * 
     * @param buffer
     *            the byte array to convert to an object
     * 
     * @return the deserialized object, or null on error
     */
    @SuppressWarnings("unused")
    @Deprecated
    private Object deserialize(byte[] buffer) {
	if (null == buffer)
	    throw new NullPointerException("buffer cannot be null");

	try {
	    return (new ObjectInputStream(new ByteArrayInputStream(buffer)))
		    .readObject();
	} catch (Exception ex) {
	    return null;
	}
    }

    /*
     * unit test (moved to JUnit 4 unit test section in ProgramPropertiesTest)
     * public static void main(String []args) {
     * ProgramProperties.setProperty("data::timeout", new Integer(5000));
     * ProgramProperties.setProperty("data::iter_block", new Integer(20));
     * 
     * assert 20 == ((Integer)ProgramProperties.setProperty("data::iter_block",
     * new Integer(10))); assert 5000 ==
     * ((Integer)ProgramProperties.getProperty("data::timeout")).intValue();
     * assert 10 ==
     * ((Integer)ProgramProperties.getProperty("data::iter")).intValue(); assert
     * null == ProgramProperties.getProperty("foobar::none");
     * 
     * System.out.println("success"); }
     */

}
