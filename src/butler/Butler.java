package butler;

import java.util.Collection;
import data.Book;
import data.VirtualBookshelf;

public interface Butler extends Comparable<Book>{

	/**
	 * Initializes brain from basis
	 * @param basis books to return 1.0
	 * @throws IllegalArgumentException
	 */
	public void initialize(Collection<VirtualBookshelf> basis) throws IllegalArgumentException;

	/**
	 * Sets the weights within brain to those found in the input
	 * ButlerWeights object.
	 * @param weights the existing weights to copy.
	 */
	public void setMatrixes(ButlerWeights weights);

	public ButlerWeights getWeights();

	@Override
	/**
	 * Checks an input book against the trained network.
	 * Returns 1 for a match, -1 for a non-match, and 0 for unsure.
	 */
	public int compareTo(Book b);

	/**
	 * Exactly the same as compareTo(Book) except that it returns
	 * the value returned by the network.
	 * 
	 * Note: The value of the boolean does not matter.
	 * It is only there to distinguish between the two
	 * compareTo methods. 
	 * 
	 * @param b Book to examine
	 * @param bool 
	 * @return
	 */
	public double[] compareTo(Book b, boolean bool);

}


