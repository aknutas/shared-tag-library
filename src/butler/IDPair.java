package butler;

import java.util.Map.Entry;
/**
 * Implementation of an Entry of String and Integer.
 * 
 * Used to map tags to an input neuron index.
 * 
 * @author sjpurol
 *
 */
public class IDPair implements Entry<String, Integer>, Comparable {

	String key;
	Integer value;
	
	/**
	 * Default constructor
	 * @param key the key to store
	 * @param value the value to store
	 */
	public IDPair(String key, Integer value){
		this.key = key;
		this.value = value;
	}
	
	/**
	 * returns the key contained in this
	 */
	@Override
	public String getKey() {return key;}

	/**
	 * returns the value contained in this
	 */
	@Override
	public Integer getValue() {return value;}

	/**
	 * sets the values contained in this and returns the old value
	 */
	@Override
	public Integer setValue(Integer newValue) {
		int oldValue = value.intValue();
		value = newValue;
		return oldValue;
	}
	
	/**
	 * if needed.
	 */
	@Override
	public int compareTo(Object o) {
		if (o instanceof IDPair){
			IDPair id = (IDPair)o;
			if ((this.key == id.key) && (this.value == id.value))
				return 0;
			else
				return this.key.compareTo(id.key);
			
		}
		else
			return -42;
	}
	

}
