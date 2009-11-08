package data;

import java.util.Map.Entry;
/**
 * Implementation of Entry<Integer, Integer>
 * 
 * @author Steve
 *
 */
public class InputPair implements Entry<Integer, Double>, Comparable {

	Integer key;
	Double value;
	
	public InputPair(Integer key, Double value){
		this.key = key;
		this.value = value;
	}
	@Override
	public Integer getKey() {return key;}

	@Override
	public Double getValue() {return value;}

	@Override
	public Double setValue(Double newValue) {
		double oldValue = value.doubleValue();
		value = newValue;
		return oldValue;
	}
	@Override
	public int compareTo(Object o) {
		if (o instanceof InputPair){
			InputPair id = (InputPair)o;
			if ((this.key == id.key) && (this.value == id.value))
				return 0;
			else
				return this.key.compareTo(id.key);
			
		}
		else
			return -42;
	}
	

}
