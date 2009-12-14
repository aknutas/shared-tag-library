package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

import data.messages.RemoteMessage;

public class TagIteratorResponder extends IteratorResponder<Entry<String, Integer>> {

	public TagIteratorResponder(Iterator<Entry<String, Integer>> iter) throws NullPointerException {
		super(iter);
	}

	@Override
	public Serializable serializeObject(Entry<String, Integer> object) throws NullPointerException, IllegalArgumentException {
		if(null == object)
			throw new NullPointerException("object cannot be null");
		
		return new RemoteEntry<String, Integer>(object.getKey(), object.getValue());
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		return null;
	}

	
	
}
