package butler;

import java.util.Map;

import data.Book;

/**
 * The LibraryButlerInterface allows VirtualLibraryButlers and RemoteLibraryButlers to be
 * within the same collection.
 * @author sjpurol
 *
 */
public interface LibraryButlerInterface{

	/**
	 * Returns the name property of the FlatShelf referenced
	 * by index.
	 * 
	 * @param index the index returned by compareTo(FlatShelf)
	 * @return a String containing the name of the FlatShelf
	 */
	public String identifyShelf(int index);
	
	/**
	 * Returns the number of shelfs this LibraryButler has learned.
	 */
	 int countShelfs();
	
	/**
	 * Takes an input book and returns the proper input values
	 * for the brain.
	 * 
	 * @param b the book to ready
	 * @return See description
	 */
	double[] readyBook(Book b);
	
	/**
	 * Typically called by the HeadButler. Runs the input book through
	 * the brain and returns the raw output vector.
	 * @param b the book to examine
	 */
	Map.Entry<FlatShelf, Double> assemble(Book b);
	
	/**
	 * Returns the property that name maps to
	 * @param name the key whose value to return
	 */
	String getProperty(String name);

	/**
	 * Returns the current ButlerWeights object.
	 */
	ButlerWeights getWeights();
}
