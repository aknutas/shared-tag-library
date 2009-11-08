package data;

import java.util.Map.Entry;
/**
 * Implementation of Entry<String, Integer>
 * 
 * @author Steve
 *
 */
public class IDPair implements Entry<String, Integer>, Comparable {

	String key;
	Integer value;
	
	public IDPair(String key, Integer value){
		this.key = key;
		this.value = value;
	}
	@Override
	public String getKey() {return key;}

	@Override
	public Integer getValue() {return value;}

	@Override
	public Integer setValue(Integer newValue) {
		int oldValue = value.intValue();
		value = newValue;
		return oldValue;
	}
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
