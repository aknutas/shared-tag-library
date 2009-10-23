package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class IDPairIterator implements Iterator<IDPair> {
	
	TreeSet<IDPair> set;
	IDPair current;

	public IDPairIterator(Set<IDPair> set){
		this.set.addAll(set);
		current = this.set.first();
	}
	
	@Override
	public boolean hasNext() {return set.size() > 1;}

	@Override
	public IDPair next() {
		
		return null;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
