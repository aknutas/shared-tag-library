package butler;

import org.encog.matrix.Matrix;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(detachable="true")
public class ButlerWeights {

	Matrix inputs;
	Matrix outputs;
	Long timestamp;
	
	public ButlerWeights(Matrix[] vals){
		inputs = vals[0];
		outputs = vals[1];
		Date time = new Date();
		timestamp = time.getTime();
		
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
}
