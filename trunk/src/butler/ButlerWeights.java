package butler;

import org.joone.engine.Matrix;
import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(detachable="true")
public class ButlerWeights {

	Matrix inputs;
	Matrix hidden;
	Matrix outputs;
	Long timestamp;
	
	public ButlerWeights(Matrix in, Matrix hid, Matrix out){
		inputs = in;
		hidden = hid;
		outputs = out;
		Date time = new Date();
		timestamp = time.getTime();
		
	}
	
	public Matrix getInputWeights(){
		return inputs;
	}
	
	public Matrix getHiddenWeights(){
		return hidden;
	}
	
	public Matrix getOutputWeights(){
		return outputs;
	}
	
	public void setInputWeights(Matrix in){
		inputs = in;
	}
	
	public void setHiddenWeights(Matrix hid){
		hidden = hid;
	}
	
	public void setOutputWeights(Matrix out){
		outputs = out;
	}
	
	public Long getTimestamp(){
		return timestamp;
	}
}
