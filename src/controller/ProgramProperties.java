package controller;

import java.io.*;
import java.util.*;

/**
 * The ProgramProperties class is used to load and store all program
 * properties which may be changed by the user. To ensure no name
 * conflicts prepend the package name followed '::' to each property
 * the package requires. For if the network layer requires a property
 * called 'timeout' then 'network::timeout' should be used. If a
 * property is to be used by more than one package then it should be
 * put in the 'global' namespace.
 * 
 * Below is a list of properties the program stores (please add):
 * 
 * 	1. butler package
 * 	2. controller package
 * 	3. data package
 * 		data::timeout		the default timeout of a remote object
 *		data::iter_block	the fetch block size for remote iterators
 *	4. database package
 *	5. main package
 *	6. operations package
 *	7. scripts package
 *	8. global
 * 
 * @author Andrew Alm
 */
public final class ProgramProperties {

	private static final Map<String, Serializable> properties = new HashMap<String, Serializable>();
	
	/**
	 * This method is used to load properties from the database. If a
	 * property already set is in the database it will be 
	 * overwritten.
	 */
	public static synchronized void loadProperties() {
		/* do something with query builder here */
	}
	
	/**
	 * This method is used to to store all properties to the database
	 * backend. 
	 */
	public static synchronized void saveProperties() {
		/* do something with query builder here */
	}
	
	/**
	 * Sets the given property name to the given value, returning the
	 * previous value, or null if no previous value. If the value
	 * given is null then the Property will be removed.
	 * 
	 * @param name the name of the property to set
	 * @param value the value to set the property to
	 * 
	 * @return the previous value of the property
	 */
	public static synchronized Object setProperty(String name, Serializable value) throws NullPointerException {
		if(null == name)
			throw new NullPointerException("name cannot be null");
		
		Object obj = null;
		if(null == value)
			obj = ProgramProperties.properties.remove(name);
		else
			obj = ProgramProperties.properties.put(name, value);
		
		return obj;
	}
	
	/**
	 * Gets the property of the given name from the property map. If
	 * the property does not exist in the property map then null is
	 * returned.
	 * 
	 * @param name the name of the property
	 * 
	 * @return the Object from the property map, or null if not found
	 * 
	 * @throws NullPointerException if the name given is null
	 */
	public static synchronized Object getProperty(String name) throws NullPointerException {
		if(name == null)
			throw new NullPointerException("name cannot be null");
		
		return ProgramProperties.properties.get(name);
	}
	
	/* unit test */
	public static void main(String []args) {
		ProgramProperties.setProperty("data::timeout", new Integer(5000));
		ProgramProperties.setProperty("data::iter_block", new Integer(20));
		
		assert 20 == ((Integer)ProgramProperties.setProperty("data::iter_block", new Integer(10)));
		assert 5000 == ((Integer)ProgramProperties.getProperty("data::timeout")).intValue();
		assert 10 == ((Integer)ProgramProperties.getProperty("data::iter")).intValue();
		assert null == ProgramProperties.getProperty("foobar::none");
		
		System.out.println("success");
	}
	
}
