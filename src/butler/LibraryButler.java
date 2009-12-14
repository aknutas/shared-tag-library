package butler;

import java.util.Iterator;
import java.util.Map;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;

import data.Book;

/**
 * The LibraryButler abstract class is an implementation of LibraryButlerInterface. It contains a Self-Organizing
 * Map that is trained with a Collection of VirtualBookshelfs to classify books into these shelfs.
 * @author sjpurol
 *
 */
public abstract class LibraryButler implements LibraryButlerInterface {

	protected BasicNetwork brain;
	protected int numShelfs;
	protected int numTags;
	protected int maxTagMag;
	protected final static String OTHER = "__OTHER__";
	protected ButlerWeights currentWeights;
	protected Map<Integer, FlatShelf> flatShelfs;

	/**
	 * used to store meta-data.
	 */
	protected Map<String, String> properties;

	/**
	 * Contains pairs of Strings and Integers mapping each tag to a specific input neuron.
	 */
	protected IDPairSet idPairs;
	
	/**
	 * Takes an input book and returns the proper input values
	 * for the brain.
	 * 
	 * @param b the book to ready
	 * @return See description
	 */
	public double[] readyBook(Book b) {

		Iterator<Map.Entry<String, Integer>> tags = b.enumerateTags();
		double[] inputValues = new double[this.numTags];

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());
			IDPair current = idPairs.get(tag.getKey());
			if (current == null) {
				current = idPairs.get(OTHER);
				inputValues[current.getValue()] += ( (double)tag.getValue() );/// maxTagMag);
				//inputValues[current.getValue()] += ( (double)tag.getValue() / maxTagMag);
			}
			else
				inputValues[current.getValue()] = ( (double)tag.getValue() );/// maxTagMag);
			//inputValues[current.getValue()] = ( (double)tag.getValue() / maxTagMag);
		}
		return inputValues;
	}
	
	/**
	 * Typically called by the HeadButler. Runs the input book through
	 * the brain and returns the raw output vector.
	 * @param b the book to examine
	 */
	public double[] assemble(Book b) {
		
		double[] inputValues = readyBook(b);
		NeuralData book = new BasicNeuralData(inputValues);
		return brain.compute(book).getData();
	}
	
	/**
	 * Returns the name property of the FlatShelf referenced
	 * by index.
	 * 
	 * @param index the index returned by compareTo(FlatShelf)
	 * @return a String containing the name of the FlatShelf
	 */
	public String identifyShelf(int index) {return flatShelfs.get(index).getProperty("name");}
	
	/**
	 * Returns the number of shelfs this LibraryButler has learned.
	 */
	public int countShelfs() {return numShelfs;}
	
	/**
	 * Returns the magnitude (absolute value) of the largest tag.
	 */
	public int getMaxTagMag() {return maxTagMag;}

}
