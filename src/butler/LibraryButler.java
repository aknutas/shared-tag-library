package butler;

import java.util.Iterator;
import java.util.Map;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.networks.BasicNetwork;

import data.Book;

/**
 * The LibraryButler abstract class is an extension of Butler. It contains a Self-Organizing
 * Map that is trained with a Collection of VirtualBookshelfs to classify books into these shelfs.
 * @author sjpurol
 *
 */
public abstract class LibraryButler extends Butler {

	protected BasicNetwork brain;
	
	protected int numShelfs;
	protected int numTags;
	
	protected final static String OTHER = "__OTHER__";
	
	protected ButlerWeights currentWeights;

	protected Map<Integer, FlatShelf> flatShelfs;
	
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
	double[] readyBook(Book b) {

		Iterator<Map.Entry<String, Integer>> tags = b.enumerateTags();
		double[] inputValues = new double[this.numTags];

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());
			IDPair current = idPairs.get(tag.getKey());
			if (current == null) {
				current = idPairs.get(OTHER);
				inputValues[current.getValue()] += tag.getValue();
			}
			else
				inputValues[current.getValue()] = tag.getValue();
		}
		return inputValues;
	}
	
	/**
	 * Typically called by the HeadButler. Runs the input book through
	 * the brain and returns the raw output vector.
	 * @param b the book to examine
	 */
	double[] assemble(Book b) {
		
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
	int countShelfs() {return numShelfs;}

}
