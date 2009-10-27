package data;

import java.util.Map.Entry;
/**
 * Implementation of Entry<Integer, Integer>
 * 
 * @author Steve
 *
 */
public class InputPair implements Entry<Integer, Integer> {

	Integer key;
	Integer value;
	
	public InputPair(Integer key, Integer value){
		this.key = key;
		this.value = value;
	}
	@Override
	public Integer getKey() {return key;}

	@Override
	public Integer getValue() {return value;}

	@Override
	public Integer setValue(Integer newValue) {
		int oldValue = value.intValue();
		value = newValue;
		return oldValue;
	}
	

}
