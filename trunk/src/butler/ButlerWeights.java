package butler;

import org.encog.matrix.Matrix;
import java.util.Date;
import java.util.Map;

import javax.jdo.annotations.PersistenceCapable;

/**
 * The ButlerWeights class allows a trained butler
 * to be stored in the database and retrieved and recreated
 * later.
 * 
 * @author sjpurol
 *
 */
@PersistenceCapable(detachable="true")
public class ButlerWeights {

	private Matrix inputs;
	private Long timestamp;
	private final int numTags;
	private Map<Integer, FlatShelf> shelfs;
	private IDPairSet idPairs;

	/**
	 * Default constructor
	 * @param input the matrix of weights to be stored
	 * @param tags the number of input neurons
	 * @param fshelfs the map identifying the output neurons
	 * @param ids the set identifying the input neurons
	 */
	
	ButlerWeights(Matrix input, int tags, Map<Integer, FlatShelf> fshelfs, IDPairSet ids){
		inputs = input;
		timestamp = new Date().getTime();
		numTags = tags;
		shelfs = fshelfs;
		idPairs = ids;
		
	}
	
	/**
	 * Returns the map of shelfs.
	 */
	public Map<Integer, FlatShelf> getShelfs() {return shelfs;}
	
	/**
	 * Returns the set of IDPairs
	 */
	public IDPairSet getIDPairs() {return idPairs;}
	
	/**
	 * Returns the matrix
	 */
	public Matrix getWeights(){return inputs;}
	
	/**
	 * Returns the timestamp
	 */
	public Long getTimestamp(){return timestamp;}
	
	/**
	 * Returns the number of tags
	 */
	public int getNumTags(){return numTags;}
}
