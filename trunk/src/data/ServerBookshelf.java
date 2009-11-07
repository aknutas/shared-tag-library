package data;

import data.messages.*;
import network.*;
import network.messages.*;

public class ServerBookshelf implements ServerResponder {

	private Bookshelf parent;
	private int id;

	public ServerBookshelf(Bookshelf parent, int id) throws NullPointerException {
		if(null == parent)
			throw new NullPointerException("parent cannot be null");
		
		this.parent = parent;
		this.id = id;
	}
	
	@Override
	public Message onMessageRecive(Message message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof BookshelfMessage))
			throw new IllegalArgumentException("illegal message type");
		
		switch(((BookshelfMessage)message).getMessageType()) {
		case BookshelfMessage.MSG_HELLO:
			return new BookshelfMessage(BookshelfMessage.MSG_HELLO, this.id);
		case BookshelfMessage.MSG_PROPERTY:
			String name = (String)((DataMessage)message).dequeParameter();
			
			BookshelfMessage response = new BookshelfMessage(BookshelfMessage.MSG_PROPERTY, this.id);
			response.queueParameter(name);
			response.queueParameter(this.parent.getProperty(name));
		}
			
		return null;
	}

}
