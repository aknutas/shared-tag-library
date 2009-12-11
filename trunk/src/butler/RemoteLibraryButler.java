package butler;

import java.util.HashMap;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.pattern.SOMPattern;

import data.Book;

public class RemoteLibraryButler extends LibraryButler {
	
	public RemoteLibraryButler(ButlerWeights weights, String name) {

		properties = new HashMap<String, String>();
		properties.put("name", name);
		currentWeights = weights;
		numTags = weights.getNumTags();
		flatShelfs = weights.getShelfs();
		numShelfs = flatShelfs.size();
		idPairs = weights.getIDPairs();
		
		// initialize the brain with a random set of weights.
		SOMPattern pattern = new SOMPattern();
		pattern.setInputNeurons(numTags);
		pattern.setOutputNeurons(numShelfs);
		brain = pattern.generate();
		
		// import weights from ButlerWeights object to brain.
		brain.getStructure().getSynapses().iterator().next().setMatrix(currentWeights.getWeights());
		brain.getStructure().finalizeStructure();
	}
	
	double[] assemble(Book b) {
		
		double[] inputValues = readyBook(b);
		NeuralData book = new BasicNeuralData(inputValues);
		return brain.compute(book).getData();
	}

	@Override
	/**
	 * Checks an input book against the trained network.
	 * Returns the index of the shelf that best matches.
	 * If no shelf matches, returns -1.
	 * 
	 * @param b The book to check.
	 */
	public int compareTo(Book b) {
		
		double[] inputValues = readyBook(b);

		NeuralData book = new BasicNeuralData(inputValues);
		final NeuralData output = brain.compute(book);

		double best = Double.MIN_VALUE;

		for (int i = 0; i < numShelfs; ++i)
			best = Math.max(best, output.getData(i));

		int index = -1;
		for (int i = 0; i < numShelfs; ++i)
			if (best == output.getData(i))
				index = i;
				
		System.out.println(b.getProperty("title") + ": " + identifyShelf(index) + "(" + index + ")");

		return index;
	}

}
