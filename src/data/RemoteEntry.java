package data;

import java.io.Serializable;
import java.util.Map.Entry;

public class RemoteEntry<K, V> implements Entry<K, V>, Serializable {

	private static final long serialVersionUID = 1L;
	
	final K key;
	final V value;
	
	public RemoteEntry(K key, V value) throws NullPointerException {
		if((null == key) || (null == value))
			throw new NullPointerException("key or value cannot be null");
		
		this.key = key;
		this.value = value;
	}
	
	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}

	@Override
	public V setValue(V value) {
		return null;
	}

}
