package data;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import data.messages.*;

public class BookResponder extends RoutedResponder {

	private Book book;
	
	public BookResponder(Book book) throws NullPointerException {
		if(null == book)
			throw new NullPointerException("book cannot be null");
		
		this.book = book;
	}
	
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
	
	private BookMessage handleTagIterator(BookMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookMessage.MSG_TAG_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		/* create response */
		BookMessage response = new BookMessage(BookMessage.MSG_TAG_ITERATOR, this.getID());
		response.queueParameter((new TagIteratorResponder(this.book.enumerateTags())).getID());
		return response;
	}
	
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
			Iterator<Entry<String, Integer>> tagIter = book.enumerateTags();
			while(tagIter.hasNext()) {
				Entry<String, Integer> tag = tagIter.next();
				
				tags.put(tag.getKey(), tag.getValue());
			}
			
			for(Entry<String, String> property : book.enumerateProperties())
				properties.put(property.getKey(), property.getValue());
		}
		
		/* create response */
		message = new BookMessage(BookMessage.MSG_INITIALIZE, message.getID());
		message.queueParameter(tags);
		message.queueParameter(properties);
		
		return message;
	}
	
	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		return null;
	}

}
