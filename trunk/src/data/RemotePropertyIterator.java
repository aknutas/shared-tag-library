package data;

import java.util.Map.Entry;

import network.Control;
import data.messages.IteratorMessage;

public class RemotePropertyIterator extends RemoteIterator<Entry<String, String>> {

	public RemotePropertyIterator(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
		super(network, connection, id);
	}

	@Override
	public Entry<String, String> createObject(IteratorMessage message)throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		return message.dequeParameter();
	}
	
	

}
