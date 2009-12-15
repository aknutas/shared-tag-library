package data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import data.messages.BookMessage;
import data.messages.BookshelfMessage;
import data.messages.RemoteMessage;
import data.messages.RoutedMessage;

/**
 * The BookResponder class extends the RoutedResponder and is used to
 * Respond to RemoteMessages from RemoteBook objects over a network.
 * It generates its response based on the Book object it contains.
 * 
 * @author Andrew Alm
 */
public class BookResponder extends RoutedResponder {

	private final Book book;
	
	/**
	 * Creates a new BookResponder using the given Book object as the
	 * response generator.
	 * 
	 * @param book the book to use
	 * 
	 * @throws NullPointerException if the book given is null.
	 */
	public BookResponder(Book book) throws NullPointerException {
		if(null == book)
			throw new NullPointerException("book cannot be null");
		
		this.book = book;
	}
	
	/**
	 * This method is used to handle a RoutedMessage of the type
	 * BookMessage. It handles all messages a book may send.
	 * 
	 * @param message the RoutedMessage to respond to
	 * 
	 * @returns a RoutedMessage response message to send
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         an instance of BookMessage
	 */
	@Override
	public RoutedMessage onRoutedMessage(RoutedMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof BookMessage))
			throw new IllegalArgumentException("illegal message type");
		
		switch(message.getMessageType()) {
		case BookMessage.MSG_INITIALIZE:
			return this.handleInitializeMessage((BookMessage)message);
		case BookMessage.MSG_TAG:
			return this.handleTagMessage((BookMessage)message);
		case BookMessage.MSG_UNTAG:
			return this.handleUntagMessage((BookMessage)message);
		case BookMessage.MSG_WEIGHT:
			return this.handleWeightMessage((BookMessage)message);
		case BookMessage.MSG_TAG_ITERATOR:
			return this.handleTagIterator((BookMessage)message);
		case BookMessage.MSG_GET:
			return this.handleGetMessage((BookMessage)message);
		case BookMessage.MSG_SET:
			return this.handleSetMessage((BookMessage)message);
		case BookMessage.MSG_PROPERTY_ITERATOR:
			return this.handlePropertyIterator((BookMessage)message);
		default:
			RoutedMessage error = new BookshelfMessage(BookshelfMessage.MSG_ERROR, this.getID());
			error.queueParameter("unknown message type");
			return error;
		}
	}
	
	/**
	 * This method is used to respond to a MSG_INITIALIZE call and is
	 * used to send the tag and property maps of the book object this
	 * responder represents.  This is used as a fast alternative to
	 * the tag and property iterators for the Book object, instead of
	 * sending each tag and property individual in an iterator all
	 * tags and properties are sent in bulk (more data, less 
	 * packets).
	 * 
	 * @param message the message to respond to
	 * 
	 * @return the response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_INITIALIZE
	 */
	private BookMessage handleInitializeMessage(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_INITIALIZE != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		/* create maps */
		HashMap<String, Integer> tags = new HashMap<String, Integer>();
		HashMap<String, String> properties = new HashMap<String, String>();
		
		/* initialize tags and properties map */
		if(book instanceof VirtualBook) {
			tags = (HashMap<String, Integer>)((VirtualBook)book).tags;
			properties = (HashMap<String, String>)((VirtualBook)book).properties;
		} else { /* slow fail safe fallback */
			for(Entry<String, Integer> tag : book.enumerateTags())
				tags.put(tag.getKey(), tag.getValue());
			
			for(Entry<String, String> property : book.enumerateProperties())
				properties.put(property.getKey(), property.getValue());
		}
		
		/* create response */
		message = new BookMessage(BookMessage.MSG_INITIALIZE, message.getID());
		message.queueParameter(tags);
		message.queueParameter(properties);
		
		return message;
	}

	/**
	 * This method is used to respond to a BookMessage.MSG_TAG type
	 * message. This updates the tag value by adding one to the
	 * previous value of the tag, returning the new tag weight.
	 * 
	 * @param message the message to respond to
	 * 
	 * @return the response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_TAG
	 */
	private BookMessage handleTagMessage(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_TAG != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String tag = message.dequeParameter();
		if(null == tag)
			return new BookMessage(BookMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_TAG, this.getID());
		response.queueParameter(new Integer(this.book.tag(tag)));
		return response;
	}
	
	/**
	 * This method is used to respond to a BookMessage.MSG_UNTAG type
	 * message. This updates the tag value by subtracting one from 
	 * the value of the tag, returning the new tag weight.
	 * 
	 * @param message the message to respond to
	 * 
	 * @return the response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_UNTAG
	 */
	private BookMessage handleUntagMessage(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_UNTAG != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String tag = message.dequeParameter();
		if(null == tag)
			return new BookMessage(BookMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_UNTAG, this.getID());
		response.queueParameter(new Integer(this.book.untag(tag)));
		return response;
	}
	
	/**
	 * This message is used to get the weight of a tag, returning
	 * a message with the given value.
	 * 
	 * @param message the message to respond to
	 * 
	 * @return the response message 
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_WEIGHT 
	 */
	private BookMessage handleWeightMessage(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_WEIGHT != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String tag = message.dequeParameter();
		if(null == tag)
			return new BookMessage(BookMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_WEIGHT, this.getID());
		response.queueParameter(new Integer(this.book.weight(tag)));
		return response;
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	private BookMessage handleSetMessage(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_GET != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String name = message.dequeParameter();
		String value = message.dequeParameter();
		if((null == name) || (null == value))
			return new BookMessage(BookMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_GET, this.getID());
		response.queueParameter(this.book.setProperty(name, value));
		return response;
	}
	
	/**
	 * This method is used respond to MSG_GET messages. It returns a
	 * message containing the value of the property of the given
	 * name. It is no longer used (instead MSG_INITIALZE gives this
	 * information to the RemoteBook object).
	 * 
	 * @param message the message to respond to
	 * 
	 * @return the response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_GET
	 */
	@Deprecated
	private BookMessage handleGetMessage(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_GET != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String name = message.dequeParameter();
		if(null == name)
			return new BookMessage(BookMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_GET, this.getID());
		response.queueParameter(this.book.getProperty(name));
		return response;
	}
	
	/**
	 * This message is used to respond to requests for the property 
	 * iterator. It is not used by the RemoteBook but here just in
	 * case.
	 * 
	 * @param message the message to respond to
	 * 
	 * @return a response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_PROPERTY_ITERATOR 
	 */
	@Deprecated
	private BookMessage handlePropertyIterator(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_PROPERTY_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_PROPERTY_ITERATOR, this.getID());
		response.queueParameter((new PropertyIteratorResponder(this.book.enumerateProperties().iterator())).getID());
		return response;
	}
	
	/**
	 * This message is used to respond to requests for the tag 
	 * iterator. It is not used by the RemoteBook but here just in
	 * case.
	 * 
	 * @param message the message to respond to
	 * 
	 * @return a response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given is not
	 *         of the type BookMessage.MSG_TAG_ITERATOR 
	 */
	@Deprecated
	private BookMessage handleTagIterator(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_TAG_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_TAG_ITERATOR, this.getID());
		response.queueParameter((new TagIteratorResponder(this.book.enumerateTags().iterator())).getID());
		return response;
	}
	
	/**
	 * Not used.
	 * 
	 * @param messsage the message to respond to
	 * 
	 * @returns nothing, always throws
	 * 
	 * @throws IllegalArgumentException if this function is called
	 */
	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		throw new IllegalArgumentException();
	}

}
