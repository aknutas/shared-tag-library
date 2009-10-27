package data;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class IDPairSet implements Set<IDPair> {

	TreeSet<IDPair> set;
	
	@Override
	/**
	 * This method adds an IDPair to the set. If the IDPair already
	 * exists in the set, the existing IDPair's value is added to the
	 * new value and stored in the existing IDPair. 
	 */
	public boolean add(IDPair e) {
		
		try {
			for (IDPair i : set)
				if (i.getKey() == e.getKey()) {
					i.setValue(i.getValue() + e.getValue());
					return false;
				}
			
			return set.add(e);
		}
		catch (ConcurrentModificationException ex) {
			return false;
		}
	}
	
	public boolean addAll(Collection<? extends IDPair> c) {return set.addAll(c);}

	@Override
	public void clear() {set.clear();}

	public boolean contains(String name) {

		for (IDPair i : set)
			if (i.getKey() == name)
				return true;
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {return set.containsAll(c);}

	@Override
	public boolean isEmpty() {return set.isEmpty();}

	@Override
	public Iterator<IDPair> iterator() {return set.iterator();}

	@Override
	public boolean remove(Object o) {
		
		try {
			set.remove(o);
			return true;
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {return set.removeAll(c);}

	@Override
	public boolean retainAll(Collection<?> c) {return set.retainAll(c);}

	@Override
	public int size() {return set.size();}

	@Override
	public Object[] toArray() {return set.toArray();}

	@Override
	public <T> T[] toArray(T[] a) {return set.toArray(a);}
	@Override
	public boolean contains(Object o) {return set.contains(o);}

}
