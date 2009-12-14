package data;

import java.util.Map.Entry;

import network.Control;
import data.messages.IteratorMessage;

public class RemoteTagIterator extends RemoteIterator<Entry<String, Integer>> {

	public RemoteTagIterator(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
		super(network, connection, id);
	}
	
	@Override
	public Entry<String, Integer> createObject(IteratorMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		return message.dequeParameter();
	}
	
}
