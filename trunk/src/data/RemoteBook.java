package data;

import java.util.*;
import java.util.Map.*;
import data.messages.*;

import network.Control;

public class RemoteBook extends RemoteObject implements Book {

	private final int id;
	
	public RemoteBook(int connection, Control network, int id) throws NullPointerException, RemoteObjectException {
		super(connection, network, 5000);
		
		this.id = id;
	}
	
	@Override
	public int tag(String tag) throws NullPointerException {
		return this.weightMessage(BookMessage.MSG_TAG, tag);
	}

	@Override
	public int untag(String tag) throws NullPointerException {
		return this.weightMessage(BookMessage.MSG_UNTAG, tag);
	}

	@Override
	public int weight(String tag) throws NullPointerException {
		return this.weightMessage(BookMessage.MSG_WEIGHT, tag);
	}
	
	/**
	 * This method is used to send a Generic 'weight' message. Every
	 * weight message send a tag and receives a weight as a response.
	 * 
	 * @param messageType the message type to use
	 * @param tag the tag to perform this weight message on
	 * 
	 * @return the weight returned (0 on error)
	 * 
	 * @throws NullPointerException if the tag is null
	 * @throws IllegalArugmentException if the message type is
	 *         illegal
	 */
	private int weightMessage(int messageType, String tag) throws NullPointerException, IllegalArgumentException {
		if(null == tag)
			throw new NullPointerException("tag cannot be null");
		
		if(BookMessage.MSG_TAG != messageType && BookMessage.MSG_UNTAG != messageType && BookMessage.MSG_WEIGHT != messageType)
			throw new IllegalArgumentException("illegal message type");
		
		/* send message */
		BookMessage message = new BookMessage(messageType, this.id);
		message.queueParameter(tag);
		RemoteMessage response = this.send(message, 5000);
		if(null == response)
			return 0;
		
		if(!(response instanceof BookMessage))
			return 0;
		
		if(messageType != response.getMessageType())
			return 0;
		
		Integer weight = response.dequeParameter();
		if(null == weight)
			return 0;
		
		return weight.intValue();
	}
	
	@Override
	public Iterator<Entry<String, Integer>> enumerateTags() {
		try {
			BookMessage message = new BookMessage(BookMessage.MSG_TAG_ITERATOR, this.id);
			RemoteMessage response = this.send(message, 5000);
			if(null == response)
				return null;
			
			if(!(response instanceof BookMessage))
				return null;
			
			if(BookMessage.MSG_TAG_ITERATOR != response.getMessageType())
				return null;
			
			Integer id = response.dequeParameter();
			if(null == id)
				return null;
			
			return new RemoteTagIterator(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}
	
	public int getTagCount() {
		return 0;
	}

	
	@Override
	public String getProperty(String name) throws NullPointerException {
		if(null == name)
			throw new NullPointerException("name cannot be null");
		
		/* send message */
		BookMessage message = new BookMessage(BookMessage.MSG_GET, this.id);
		message.queueParameter(name);
		RemoteMessage response = this.send(message, 5000);
		if(null == response)
			return null;
		
		if(!(response instanceof BookMessage))
			return null;
		
		if(BookMessage.MSG_GET != response.getMessageType())
			return null;
		
		return response.dequeParameter();
	}

	@Override
	public String setProperty(String name, String value) throws NullPointerException {
		if((null == name) || (null == value))
			throw new NullPointerException("name or value cannot be null");
		
		/* send message */
		BookMessage message = new BookMessage(BookMessage.MSG_SET, this.id);
		message.queueParameter(name);
		message.queueParameter(value);
		RemoteMessage response = this.send(message, 5000);
		if(null == response)
			return null;
		
		if(!(response instanceof BookMessage))
			return null;
		
		if(BookMessage.MSG_SET != response.getMessageType())
			return null;
		
		return response.dequeParameter();
	}
	
	@Override
	public Iterator<Entry<String, String>> enumerateProperties() {
		try {
			BookMessage message = new BookMessage(BookMessage.MSG_PROPERTY_ITERATOR, this.id);
			RemoteMessage response = this.send(message, 5000);
			if(null == response)
				return null;
			
			if(!(response instanceof BookMessage))
				return null;
			
			if(BookMessage.MSG_PROPERTY_ITERATOR != response.getMessageType())
				return null;
			
			Integer id = response.dequeParameter();
			if(null == id)
				return null;
			
			return new RemotePropertyIterator(this.network, this.connection, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}	
}
