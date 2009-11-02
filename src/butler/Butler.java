package butler;

import java.util.Iterator;
import java.util.Map;

import operations.LibraryOperations;

import org.joone.engine.*;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.io.MemoryInputSynapse;

import data.Book;
import data.Bookshelf;
import data.IDPair;
import data.IDPairSet;
import data.InputPair;
import data.InputPairSet;
import data.Library;
import controller.Controller;
import scripts.ScriptGenerator;

public class Butler extends Thread implements NeuralNetListener {

	private Controller controller;  //reference to the current controller.
	private Layer INPUT;
	private Layer OUTPUT;
	private Layer HIDDEN;
	private boolean isTrained;
	private IDPairSet idPairs;

	public Butler(String name, Controller control){
		super(name);
		controller = control;
	}

	public void firstTrainingData(Library basis) throws IllegalArgumentException{

		if (null == basis)
			throw new IllegalArgumentException("basis cannot be null.");

		Iterator<Map.Entry<String, Integer>> tags = LibraryOperations.getTags(basis);
		Iterator<Map.Entry<String, String>> properties = LibraryOperations.getProperties(basis);

		idPairs = new IDPairSet();	//tag | property -> neuron #
		InputPairSet inputPairs = new InputPairSet();	// neuron # -> weight

		int i = 0;

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			idPairs.add(new IDPair(tag.getKey(),i));
			inputPairs.add(new InputPair(i, tag.getValue()));
			
			if (idPairs.contains(tag.getKey()))
			
			i++;
		}

		while (properties.hasNext()){
			Map.Entry<String, String> prop = properties.next();
			idPairs.add(new IDPair(prop.getValue(),i));
			inputPairs.add(new InputPair(i, 1));
			i++;
		}

		INPUT = new SigmoidLayer();
		HIDDEN = new SigmoidLayer();
		OUTPUT = new SigmoidLayer();
		
		INPUT.setLayerName("input");
		HIDDEN.setLayerName("hidden");
		OUTPUT.setLayerName("output");
		
		INPUT.setRows(i);
		HIDDEN.setRows(i+1);
		OUTPUT.setRows(1);
		
		FullSynapse synapse_IH = new FullSynapse();
		FullSynapse synapse_HO = new FullSynapse();
		
		synapse_IH.setName("IH");
		synapse_HO.setName("HO");
		
		INPUT.addOutputSynapse(synapse_IH);
		HIDDEN.addInputSynapse(synapse_IH);
		HIDDEN.addOutputSynapse(synapse_HO);
		OUTPUT.addInputSynapse(synapse_HO);
		
		Monitor monitor = new Monitor();
		
		monitor.setLearningRate(0.8);
		monitor.setMomentum(0.3);
		
		INPUT.setMonitor(monitor);
		HIDDEN.setMonitor(monitor);
		OUTPUT.setMonitor(monitor);
		
		monitor.addNeuralNetListener(this);
		
		InputPairSynapse inputSynapse = new InputPairSynapse();
		INPUT.addInputSynapse(inputSynapse);
		
		Iterator<InputPair> inputs = inputPairs.iterator();
		while (inputs.hasNext()){
			InputPair tmp = inputs.next();
			inputSynapse.setInputs(tmp);
		}
		
		TeachingSynapse trainer = new TeachingSynapse();
		trainer.setMonitor(monitor);
		
		double[][] expectedOutputs = {{1.0}};
		
		MemoryInputSynapse expected = new MemoryInputSynapse();
		expected.setInputArray(expectedOutputs);
		
		trainer.setDesired(expected);
		OUTPUT.addOutputSynapse(trainer);
		
		/*
		 * Starting background threads for each layer.
		 */
		
		INPUT.start();
		HIDDEN.start();
		OUTPUT.start();
		
		monitor.setTotCicles(20000);
		monitor.setLearning(true);
		
		monitor.Go();
		isTrained = true;
	}

	@Override
	public void cicleTerminated(NeuralNetEvent e) {
		
		Monitor mon = (Monitor)e.getSource();
		long cyc = mon.getCurrentCicle();
		long cycK = cyc / 1000;		// for debugging. print out every 1000 cycles.
		
		if ( (cycK * 1000) == cyc)
			System.out.println("Cycle " + cyc + " Error remaining: " + mon.getGlobalError());
	}

	/**
	 * @unused
	 */
	@Override
	public void errorChanged(NeuralNetEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @unused
	 */
	@Override
	public void netStarted(NeuralNetEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @unused
	 */
	@Override
	public void netStopped(NeuralNetEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @unused
	 */
	@Override
	public void netStoppedError(NeuralNetEvent e, String error) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args){
		ScriptGenerator sg = new ScriptGenerator("sampleBooks.txt");
		Controller dummy = new Controller();
		Butler buddy = new Butler("Buddy", dummy);
		buddy.firstTrainingData(sg.p.lib);
		
		
	}

}
