package butler;

import java.util.Map.Entry;

import javax.jdo.annotations.PersistenceCapable;
/**
 * Implementation of an Entry of String and Integer.
 * 
 * Used to map tags to an input neuron index.
 * 
 * @author sjpurol
 *
 */
@PersistenceCapable(detachable="true")
public class IDPair implements Entry<String, Integer> {

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
}
