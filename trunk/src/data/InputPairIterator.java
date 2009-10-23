package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class InputPairIterator implements Iterator<InputPair> {
	
	TreeSet<InputPair> set;
	InputPair current;

	public InputPairIterator(Set<InputPair> set){
		this.set.addAll(set);
		current = this.set.first();
	}
	
	@Override
	public boolean hasNext() {return set.size() > 1;}

	@Override
	public InputPair next() {
		
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
