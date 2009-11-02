package butler;

import org.joone.io.MemoryInputSynapse;
import data.InputPair;

public class InputPairSynapse extends MemoryInputSynapse {
	private static final long serialVersionUID = 1077546057949331775L;
	private double[][] myInputArray;
	
	/**
	 * Creates a new instance of an InputPairSynapse.
	 */
	public InputPairSynapse(){
		
	}
	/**
	 * Places the value of the InputPair into the correct slot on the grid.
	 * @param input the InputPair
	 */
	public void setInputs(InputPair input){

		myInputArray[0][input.getKey()] = (double)input.getValue();
	}
	
	/**
	 * Required to set the values into the proper grid
	 * held by the parent class.
	 */
	public void setAllInput(){
		setInputArray(myInputArray);
	}
		
}
