package data;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * An implementation of Set<InputPair>.
 * @author Steve
 *
 */
public class InputPairSet implements Set<InputPair> {

	TreeSet<InputPair> set;
	
	/**
	 * Attempts to add an InputPair to the set.
	 * If the key already exists, the corresponding
	 * value is added to the input value and re-stored.
	 * 
	 * @param InputPair e The InputPair to add.
	 */
	public boolean add(InputPair e) {
		
		for (InputPair i : set)
			if (i.getKey() == e.getKey()) {
				i.setValue(i.getValue() + e.getValue());
				return true;
			}
		
		return set.add(e);
	}

	/**
	 * if needed.
	 */
	public boolean addAll(Collection<? extends InputPair> c) {return set.addAll(c);}

	@Override
	/**
	 * if needed.
	 */
	public void clear() {set.clear();}

	/**
	 * Returns true if the set contains an InputPair with
	 * an Key matching id.
	 */
	public boolean contains(Integer id) {

		for (InputPair i : set)
			if (i.getKey() == id)
				return true;
		
		return false;
	}

	@Override
	/**
	 * if needed.
	 */
	public boolean containsAll(Collection<?> c) {return set.containsAll(c);}

	@Override
	/**
	 * if needed.
	 */
	public boolean isEmpty() {return set.isEmpty();}

	@Override
	/**
	 * if needed.
	 */
	public Iterator<InputPair> iterator() {return set.iterator();}

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
	 * if needed.
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
