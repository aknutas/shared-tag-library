package butler;

import java.util.*;

import org.joone.engine.Layer;

import data.*;
public class ButlerThread extends Thread {

	Butler buddy;
	public ButlerThread(Butler newBuddy){
		buddy = newBuddy;
	}
		
	public boolean trainButler(Book basis){
		
		Iterator<Map.Entry<String, Integer>> tags = basis.enumerateTags();
		Iterator<Map.Entry<String, String>> properties = basis.enumerateProperties();
		
		IDPairSet idPairs = new IDPairSet();	//tag | property -> neuron #
		InputPairSet inputPairs = new InputPairSet();	// neuron # -> weight
		
		int i = 0;
		
		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			idPairs.add(new IDPair(tag.getKey(),Integer.valueOf(i)));
			inputPairs.add(new InputPair(i, tag.getValue()));
			i++;
		}
		
		while (properties.hasNext()){
			Map.Entry<String, String> prop = properties.next();
			idPairs.add(new IDPair(prop.getValue(),Integer.valueOf(i)));
			inputPairs.add(new InputPair(i, 1));
			i++;
		}
		
		return false;
	}
}
