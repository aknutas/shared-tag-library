package data;

import java.util.Map.Entry;

public class IDPair implements Entry<String, Integer> {

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
	

}
