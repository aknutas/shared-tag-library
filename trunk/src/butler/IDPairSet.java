package butler;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
/**
 * An implementation of a Set of IDPair.
 * 
 * @author sjpurol
 *
 */
public class IDPairSet implements Set<IDPair> {

	TreeSet<IDPair> set;

	/**
	 * Creates a new instance of an IDPairSet.
	 */
	public IDPairSet(){
		set = new TreeSet<IDPair>();
	}

	@Override
	/**
	 * This method adds an IDPair to the set.
	 * 
	 *  @returns true if unique item added. false if item already exists.
	 */
	public boolean add(IDPair e) {

		try {
			for (IDPair i : set)
				if (i.getKey() == e.getKey())
					return false;

			return set.add(e);
		}
		catch (ConcurrentModificationException ex) {
			return false;
		}
	}

	/**
	 * If needed.
	 */
	public boolean addAll(Collection<? extends IDPair> c) {return set.addAll(c);}


	@Override
	/**
	 * If needed.
	 */
	public void clear() {set.clear();}

	/**
	 * Returns true if the set contains an IDPair
	 * with a key matching name.
	 * @param name Key to match
	 */
	public boolean contains(String name) {

		for (IDPair i : set)
			if (i.getKey() == name)
				return true;

		return false;
	}

	/**
	 * Returns an IDPair matching key name.
	 * @param name the key to match
	 */
	public IDPair get(String name) {

		if (contains(name))
			for (IDPair i : set)
				if (i.getKey().equalsIgnoreCase(name))
					return i;

		return null;
	}

	/**
	 * Returns the num-th IDPair.
	 * @param num the index of the IDPair
	 */
	public IDPair get(Integer num){

		for (IDPair i : set)
			if (i.getValue().equals(num))
				return i;
		return null;
	}

	@Override
	/**
	 * if needed
	 */
	public boolean containsAll(Collection<?> c) {return set.containsAll(c);}

	@Override
	/**
	 * Returns true if set is empty.
	 */
	public boolean isEmpty() {return set.isEmpty();}

	@Override
	/**
	 * Returns an iterator over the set.
	 */
	public Iterator<IDPair> iterator() {return set.iterator();}

	@Override
	/**
	 * if needed.
	 */
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
	/**
	 * if needed.
	 */
	public boolean removeAll(Collection<?> c) {return set.removeAll(c);}

	@Override
	/**
	 * if needed.
	 */
	public boolean retainAll(Collection<?> c) {return set.retainAll(c);}

	@Override
	/**
	 * Returns the number of IDPairs in the set.
	 */
	public int size() {return set.size();}

	@Override
	/**
	 * if needed.
	 */
	public Object[] toArray() {return set.toArray();}

	@Override
	/**
	 * if needed.
	 */
	public <T> T[] toArray(T[] a) {return set.toArray(a);}
	@Override
	/**
	 * if needed.
	 */
	public boolean contains(Object o) {return set.contains(o);}

}
