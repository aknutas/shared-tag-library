package scripts;

import controller.*;

/**
 * This class is used to set the default properties for the application.  This
 * class contains all properties the program uses (and their author's suggested
 * default values).
 * 
 * @author Andrew Alm
 */
public class DefaultProperties {
    
    	ProgramProperties props;
    
    	public DefaultProperties()
    	{
    	    props = ProgramProperties.getInstance();
    	}
    	
    	public DefaultProperties(ProgramProperties props)
    	{
    	    this.props = props;
    	}

	/**
	 * This method is used to set all the default properties for the
	 * ProgramProperties class and then save to the database. This
	 * method will overwrite and previous values.
	 */
	public void generate() {
		/* butler package properties */
		// TODO: add butler package properties
		
		/* controller package properties */
		// TODO: add controller package properties
		
		/* data package properties */
		props.setProperty("data::timeout", new Integer(5000)); /* controls message timeout */
		props.setProperty("data::iter_chunk", new Integer(20)); /* default iterator message chunk size */
		
		/* main package properties */
		// TODO: add main package properties
		
		/* network package properties */
		//Listening port
		props.setProperty("network::port", new Integer(24600));
		//Connection timeout, in seconds
		props.setProperty("network::timeout", new Integer(4000));
		//Network timeout, in milliseconds
		props.setProperty("network::so_timeout", new Integer(35));
		
		/* operations package properties */
		// TODO: add operations package properties
		
		/* global properties */
		props.setProperty("global::version", new Integer(1)); /* version (example) */
		
	}
	
	public void store()
	{
		/* save to database */
		props.saveProperties();
	}
	
	public static void main(String args[]) {
	    	DefaultProperties propset = new DefaultProperties();
		propset.generate();
		propset.store();
	}
	
}
