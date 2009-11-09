package data;

import java.io.*;
import java.util.*;
import java.util.Map.*;
import data.messages.*;

public class PropertyIteratorResponder extends IteratorResponder<Entry<String, String>> {

	public PropertyIteratorResponder(Iterator<Entry<String, String>> iter) throws NullPointerException {
		super(iter);
	}

	@Override
	public Serializable serializeObject(Entry<String, String> object) throws NullPointerException, IllegalArgumentException {
		if(null == object)
			throw new NullPointerException("object cannot be null");
		
		return new RemoteEntry<String, String>(object.getKey(), object.getValue());
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		return null;
	}

}
