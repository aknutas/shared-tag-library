package data;

import java.io.*;
import java.util.Map.*;

public class RemoteEntry<K, V> implements Entry<K, V>, Serializable {

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
