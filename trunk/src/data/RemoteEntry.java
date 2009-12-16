package data;

import java.io.Serializable;
import java.util.Map.Entry;

/**
 * The RemoteEntry class implements the Entry interface and the
 * Serializable interface. It is used clone other types of entry
 * objects which are not guaranteed to be Serializable.
 *
 * @param <K> key type
 * @param <V> value type
 * 
 * @author Andrew Alm
 */
public class RemoteEntry<K, V> implements Entry<K, V>, Serializable {

	private static final long serialVersionUID = 1L;
	
	final K key;
	final V value;
	
	/**
	 * Creates a new RemoteEntry from the given key and value. The
	 * key and value may be null.
	 *  
	 * @param key the key for the entry
	 * @param value the value for the entry
	 */
	public RemoteEntry(K key, V value)  {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * This method is used to the key value of the RemoteEntry.
	 * 
	 * @return the key value
	 */
	@Override
	public K getKey() {
		return this.key;
	}

	/**
	 * This method is used to the value of the RemoteEntry.
	 * 
	 * @return the value
	 */
	@Override
	public V getValue() {
		return this.value;
	}

	/**
	 * This method should never be called, since this Entry is 
	 * detached from its original data structure (and that data
	 * structure is on a remote program instance) the value cannot be
	 * set.
	 * 
	 * @param value not used
	 * 
	 * @return null
	 */
	@Override
	public V setValue(V value) {
		return null;
	}

}
