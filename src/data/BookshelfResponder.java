package data;

import java.util.*;
import data.messages.*;
import network.*;
import network.messages.*;

public class BookshelfResponder extends RoutedResponder {

	private Bookshelf shelf;
	private int id;

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
		this.id = RoutedResponder.putResponder(this);
	}
	
	public int getID() {
		return this.id;
	}

	@Override
	public RoutedMessage onRoutedMessage(RoutedMessage message)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	public Message onMessage(Message message) throws NullPointerException, IllegalArgumentException {
//		if(null == message)
//			throw new NullPointerException("message cannot be null");
//		
//		if(message instanceof RemoteMessage) {
//			if(RemoteMessage.MSG_PING == ((RemoteMessage)message).getMessageType())
//				return new RemoteMessage(RemoteMessage.MSG_PING);
//		}
//		
//		if(!(message instanceof BookshelfMessage))
//			throw new IllegalArgumentException("illegal message type");
//		
//		switch(((BookshelfMessage)message).getMessageType()) {
//		case BookshelfMessage.MSG_HELLO:
//			return new BookshelfMessage(BookshelfMessage.MSG_HELLO, this.id);
//		case BookshelfMessage.MSG_PROPERTY:
//			String name = (String)((RemoteMessage)message).dequeParameter();
//			
//			BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_PROPERTY, this.id);
//			response.queueParameter(name);
//			response.queueParameter(this.shelf.getProperty(name));
//		}
//			
//		return null;
//	}
//	
//	/**
//	 * Static method to handle a BookshelfMessage. The BookshelfMessage  
//	 * @param message
//	 * @return
//	 * @throws NullPointerException
//	 */
//	public static BookshelfMessage onBookshelfMessage(BookshelfMessage message) throws NullPointerException {
//		if(message == null)
//			throw new NullPointerException("message cannot be null");
//		
//		//if()
//		
//		return null;
//	}
//	
//	/**
//	 * Puts the given BookshelfResponder into the responder map.
//	 * 
//	 * @param responder the responder to put into the responder map.
//	 * 
//	 * @return the id given to the responder.
//	 * 
//	 * @throws NullPointerException
//	 */
//	private static synchronized int putResponder(BookshelfResponder responder) throws NullPointerException {
//		if(null == responder)
//			throw new NullPointerException("responder cannot be null");
//		
//		int id = BookshelfResponder.nextShelf;
//		BookshelfResponder.responders.put(id, responder);
//		BookshelfResponder.nextShelf += 1;
//		
//		return id;
//	}

}
