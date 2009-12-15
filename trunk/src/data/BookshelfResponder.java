package data;

import data.messages.BookshelfMessage;
import data.messages.RemoteMessage;
import data.messages.RoutedMessage;

public class BookshelfResponder extends RoutedResponder {

	private Bookshelf shelf;

	/**
	 * Creates a new BookshelfResponder object using the given
	 * bookshelf to fulfill requests.
	 * 
	 * @param shelf the Bookshelf
	 * 
	 * @throws NullPointerException
	 */
	public BookshelfResponder(Bookshelf shelf) throws NullPointerException {
		if(null == shelf)
			throw new NullPointerException("parent cannot be null");
		
		this.shelf = shelf;
	}

	@Override
	public RoutedMessage onRoutedMessage(RoutedMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof BookshelfMessage))
			throw new IllegalArgumentException("illegal message type");
		
		switch(message.getMessageType()) {
		case BookshelfMessage.MSG_SIZE:
			return this.handleSizeMessage((BookshelfMessage)message);
		case BookshelfMessage.MSG_GET:
			return this.handleGetMessage((BookshelfMessage)message);
		case BookshelfMessage.MSG_SET:
			return this.handleSetMessage((BookshelfMessage)message);
		case BookshelfMessage.MSG_PROPERTY_ITERATOR:
			return this.handlePropertyIterator((BookshelfMessage)message);
		case BookshelfMessage.MSG_ITERATOR:
			return this.handleIteratorRequest((BookshelfMessage)message);
		default:
			RoutedMessage error = new BookshelfMessage(BookshelfMessage.MSG_ERROR, this.getID());
			error.queueParameter("unknown message type");
			return error;
		}
	}
	
	private BookshelfMessage handleSizeMessage(BookshelfMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookshelfMessage.MSG_SIZE != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		/* create response */
		BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_SIZE, this.getID());
		response.queueParameter(new Integer(this.shelf.size()));
		return response;
	}
	
	private BookshelfMessage handleGetMessage(BookshelfMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookshelfMessage.MSG_GET != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String name = message.dequeParameter();
		if(null == name)
			return new BookshelfMessage(BookshelfMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_GET, this.getID());
		response.queueParameter(this.shelf.getProperty(name));
		return response;
	}
	
	private BookshelfMessage handleSetMessage(BookshelfMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookshelfMessage.MSG_SET != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		String name = message.dequeParameter();
		String value = message.dequeParameter();
		if((null == name) || (null == value))
			return new BookshelfMessage(BookshelfMessage.MSG_ERROR, this.getID());
				
		/* create response */
		BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_SET, this.getID());
		response.queueParameter(this.shelf.setProperty(name, value));
		return response;
	}
	
	private BookshelfMessage handlePropertyIterator(BookshelfMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookshelfMessage.MSG_PROPERTY_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		/* create response */
		BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_PROPERTY_ITERATOR, this.getID());
		response.queueParameter((new PropertyIteratorResponder(this.shelf.enumerateProperties().iterator())).getID());
		return response;
	}
	
	private BookshelfMessage handleIteratorRequest(BookshelfMessage message) throws NullPointerException, IllegalArgumentException {
		BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_ITERATOR, this.getID());
		
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(BookshelfMessage.MSG_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		BookIteratorResponder responder = new BookIteratorResponder(this.shelf.iterator());
		response.queueParameter(responder.getID());
		return response;
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message)	throws NullPointerException, IllegalArgumentException {
		return null;
	}

}
