package butler;

import org.joone.net.SimpleNeuralNet;

public class Butler extends SimpleNeuralNet implements Runnable {
	
	private static final long serialVersionUID = 5147551660822819190L;

	private Object controller;  //reference to the current controller.
	
	public Butler(String name, Object control){
		super(name);
		controller = control;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
