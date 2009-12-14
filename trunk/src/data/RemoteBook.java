package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import network.Control;
import data.messages.BookMessage;
import data.messages.RemoteMessage;

public class RemoteBook extends RemoteObject implements Book {

	private final int id;
	private Map<String, Integer> tags;
	private Map<String, String> properties;
	
	public RemoteBook(int connection, Control network, int id) throws NullPointerException, RemoteObjectException {
		super(connection, network, 5000);
		
		this.tags = new HashMap<String, Integer>();
		this.properties = new HashMap<String, String>();
		this.id = id;
		
		this.getBookInfo();
	}
	
	@SuppressWarnings("unchecked")
	private void getBookInfo() throws RemoteObjectException {
		RemoteMessage message = new BookMessage(BookMessage.MSG_INITIALIZE, this.id);
		message = this.send((RemoteMessage)message, 5000);
		
		if(!(message instanceof BookMessage))
			throw new RemoteObjectException();
		
		Map<String, Integer> tags = (Map<String, Integer>)message.dequeParameter();
		Map<String, String> properties = (Map<String, String>)message.dequeParameter();
		
		if(null == tags || null == properties)
			throw new RemoteObjectException();
		
		this.tags = tags;
		this.properties = properties;
	}
	
	@Override
	public int tag(String tag) throws NullPointerException {
		if(null == tag)
			throw new NullPointerException("tag cannot be null");
			
		int weight = this.weightMessage(BookMessage.MSG_TAG, tag);
		this.tags.put(tag, weight);		
		
		return weight;
	}

	@Override
	public int untag(String tag) throws NullPointerException {
		if(null == tag)
			throw new NullPointerException("tag cannot be null");
		
		int weight = this.weightMessage(BookMessage.MSG_UNTAG, tag);
		this.tags.put(tag, weight);
		
		return weight;
	}

	@Override
	public int weight(String tag) throws NullPointerException {
		if(null == tag)
			throw new NullPointerException("tag cannot be null");
		
		return this.tags.get(tag);
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
		return this.tags.entrySet().iterator();
	}
	
	public int getTagCount() {
		return this.tags.size();
	}

	
	@Override
	public String getProperty(String name) throws NullPointerException {
		if(null == name)
			throw new NullPointerException("tag cannot be null");
			
		return this.properties.get(name);
	}

	@Override
	public String setProperty(String name, String value) throws NullPointerException {
		if((null == name) || (null == value))
			throw new NullPointerException("name or value cannot be null");
		
		/* update local */
		this.properties.put(name, value);
		
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
		
		/* this is the most up-to-date previous value */
		return response.dequeParameter();
	}
	
	@Override
	public Collection<Entry<String, String>> enumerateProperties() {
		return this.properties.entrySet();
	}	
}
