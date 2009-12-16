package butler;

import java.util.HashMap;
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
	boolean initialized = false;

	/**
	 * used to store meta-data.
	 */
	protected Map<String, String> properties;

	/**
	 * Contains pairs of Strings and Integers mapping each tag to a specific input neuron.
	 */
	protected Map<String, Integer> idPairs;
	
	/**
	 * Takes an input book and returns the proper input values
	 * for the brain.
	 * 
	 * @param b the book to ready
	 * @return See description
	 */
	public double[] readyBook(Book b) {

		double[] inputValues = new double[this.numTags];

		for(Map.Entry<String, Integer> tag : b.enumerateTags()) {
			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());
			Integer current = idPairs.get(tag.getKey());
			if (current == null) {
				current = idPairs.get(OTHER);
				inputValues[current] += ( (double)tag.getValue() );/// maxTagMag);
				//inputValues[current.getValue()] += ( (double)tag.getValue() / maxTagMag);
			}
			else
				inputValues[current] = ( (double)tag.getValue() );/// maxTagMag);
			//inputValues[current.getValue()] = ( (double)tag.getValue() / maxTagMag);
		}
		
		double max[] = max(inputValues);
		for (int i = 0; i < inputValues.length; ++i)
			inputValues[i] = inputValues[i] / max[0];
		
		return inputValues;
	}
	
	/**
	 * Typically called by the HeadButler. Runs the input book through
	 * the brain and returns an entry containing the matching FlatShelf
	 * and the corresponding raw output value. If this butler was
	 * initialized with the empty library, returns null.
	 * 
	 * @param b the book to examine
	 */
	public Map.Entry<FlatShelf, Double> assemble(Book b) {
		
		double[] inputValues = readyBook(b);
		NeuralData book = new BasicNeuralData(inputValues);
		if (flatShelfs.size() == 0)
			return null;
				
		double[] answer = max(brain.compute(book).getData());
		
		//for (double d : answer)
		//	System.out.println(d);

		Map<FlatShelf, Double> temp = new HashMap<FlatShelf, Double>();
		temp.put(flatShelfs.get((int)answer[1]), answer[0]);
		return temp.entrySet().iterator().next();
	}
	
	/**
	 * Takes a double[] as input and returns a 2 element double[].
	 * ans
	 * @param input
	 * @return
	 */
	protected double[] max(double[] input) {
		
		double max = Double.MIN_VALUE;
		int index = -1;
		
		for (int i = 0; i < input.length; ++i) {
			max = Math.max(max, input[i]);
			if (max == input[i])
				index = i;
		}
		
		double[] answer = new double[2];
		answer[0] = max;
		answer[1] = index;
		
		return answer;
	}
	
	/**
	 * Returns the name property of the FlatShelf referenced
	 * by index.
	 * 
	 * @param index the index returned by compareTo(FlatShelf)
	 * @return a String containing the name of the FlatShelf
	 */
	public String identifyShelf(int index) {
		
		try {
			return flatShelfs.get(index).getProperty("name");
		}
		catch (Exception e) {
			return "unknown";
		}
	}
	
	/**
	 * Returns the number of shelfs this LibraryButler has learned.
	 */
	public int countShelfs() {return numShelfs;}
	
	/**
	 * Returns the magnitude (absolute value) of the largest tag.
	 */
	public int getMaxTagMag() {return maxTagMag;}

	/**
	 * Returns true if initialized.
	 */
	public boolean isInitialized() {return initialized;}

}
