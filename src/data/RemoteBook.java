package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import network.Control;
import data.messages.BookMessage;
import data.messages.RemoteMessage;

/**
 * The RemotBook class extends the RemoteObject class and implements the Book
 * interface. It is used to generate and handle message in order to interact
 * with a Book object over a network.
 *  
 * @author Andrew alm
 */
public class RemoteBook extends RemoteObject implements Book {

	private final int id;
	private Map<String, Integer> tags;
	private Map<String, String> properties;
	
	/**
	 * Creates a RemoteBook object given the network, connection, and
	 * id.
	 * 
	 * @param connection the connection id to use
	 * @param network the network Control object to use
	 * @param id the id to use in for the RoutedMessages
	 * 
	 * @throws NullPointerException if the network given is null
	 * @throws RemoteObjectException if the RemoteBook object cannot
	 *         be created
	 */
	public RemoteBook(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
		super(connection, network, 5000);
		
		this.tags = new HashMap<String, Integer>();
		this.properties = new HashMap<String, String>();
		this.id = id;
		
		this.getBookInfo();
	}
	
	/**
	 * This method sends a MSG_INITIALIZE message to the responder in
	 * order to get the tags and properties from a book and cache
	 * them locally.
	 * 
	 * @throws RemoteObjectException if there was an error receiving
	 *         the properties or tags
	 */
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
	
	/**
	 * The weight of the given tag is increased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag.
	 *
	 * @throws NullPointerException if the tag given is null
	 */
	@Override
	public int tag(String tag) throws NullPointerException {
		if(null == tag)
			throw new NullPointerException("tag cannot be null");
			
		int weight = this.weightMessage(BookMessage.MSG_TAG, tag);
		this.tags.put(tag, weight);		
		
		return weight;
	}

	/**
	 * The weight of the given tag is decreased.
	 *
	 * @param tag name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws NullPointerException if the tag given is null
	 */
	@Override
	public int untag(String tag) throws NullPointerException {
		if(null == tag)
			throw new NullPointerException("tag cannot be null");
		
		int weight = this.weightMessage(BookMessage.MSG_UNTAG, tag);
		this.tags.put(tag, weight);
		
		return weight;
	}

	/**
	 * Gets the weight of a given tag. If the tag has never been used 
	 * with this VirtualBook then the weight will be zero. (Note: A 
	 * return value of zero does not necessarily indicate that a tag
	 * has never been used with the book.)
	 *
	 * @param tag the name of the tag
	 *
	 * @return the weight of the tag
	 *
	 * @throws NullPointerException if the tag given is null
	 */
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
	
	/**
	 * Returns an iterator of all tags on a book and their associated
	 * weights.
	 * 
	 * @return an Iterator of map entries.
	 */
	@Override
	public Iterable<Entry<String, Integer>> enumerateTags() {
		return this.tags.entrySet();
	}
	
	/**
	 * Get the number of tags that are associated with this Book
	 * object.
	 * 
	 * @return the number of tags
	 */
	public int getTagCount() {
		return this.tags.size();
	}

	/**
	 * Gets the value of the given property. If the given property does not
	 * exist then null is returned.
	 * 
	 * @param property the name of the property to get.
	 * 
	 * @return the value of the property
	 * 
	 * @throws NullPointerException if the property given is null
	 */
	@Override
	public String getProperty(String name) throws NullPointerException {
		if(null == name)
			throw new NullPointerException("tag cannot be null");
			
		return this.properties.get(name);
	}

	/**
	 * Sets the named property to the given value, returning the old value for
	 * the property. If the property does not exist, null is returned.
	 * 
	 * @param name the name of the property
	 * @param value the value to set the property to
	 * 
	 * @return NullPointerException if the name of value given is null
	 */
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
	
	/**
	 * Returns an iterator containing the key-value pairs of all the
	 * properties.
	 * 
	 * @return an iterator of properties
	 */
	@Override
	public Collection<Entry<String, String>> enumerateProperties() {
		return this.properties.entrySet();
	}
	
	/**
	 * Returns a VirtualBook object of this Book interface object. If
	 * the book is already a VirtualBook then the Book is returned
	 * and no copy is made.
	 * 
	 * @return a VirtualBook
	 */
	public VirtualBook makeVirtual() {
		return new VirtualBook(this.tags, this.properties);
	}
}
