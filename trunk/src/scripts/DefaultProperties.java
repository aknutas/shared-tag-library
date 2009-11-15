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

	/**
	 * This method is used to set all the default properties for the
	 * ProgramProperties class and then save to the database. This
	 * method will overwrite and previous values.
	 */
	public static void generate() {
		/* butler package properties */
		// TODO: add butler package properties
		
		/* controller package properties */
		// TODO: add controller package properties
		
		/* data package properties */
		ProgramProperties.setProperty("data::timeout", new Integer(5000)); /* controls message timeout */
		ProgramProperties.setProperty("data::iter_chunk", new Integer(20)); /* default iterator message chunk size */
		
		/* main package properties */
		// TODO: add main package properties
		
		/* network package properties */
		// TODO: add network package properties
		
		/* operations package properties */
		// TODO: add operations package properties
		
		/* global properties */
		ProgramProperties.setProperty("global::version", new Integer(1)); /* version (example) */
		
		/* save to database */
		ProgramProperties.saveProperties();
	}
	
	public static void main(String args[]) {
		DefaultProperties.generate();
	}
	
}
