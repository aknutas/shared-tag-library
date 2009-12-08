package butler;

import org.encog.matrix.Matrix;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(detachable="true")
public class ButlerWeights {

	private Matrix inputs;
	private Matrix outputs;
	private Long timestamp;
	private final int numTags;

	public ButlerWeights(Matrix input, int tags){
		//public ButlerWeights(Matrix input, Matrix output, int tags){
		inputs = input;
		//outputs = output;
		timestamp = new Date().getTime();
		numTags = tags;
		
	}
	
	public Matrix getInputWeights(){
		return inputs;
	}
		
	public Matrix getOutputWeights(){
		return outputs;
	}
	
	public void setInputWeights(Matrix in){
		inputs = in;
	}
	
	public void setOutputWeights(Matrix out){
		outputs = out;
	}
	
	public Long getTimestamp(){
		return timestamp;
	}
	
	public int getNumTags(){
		return numTags;
	}
}
