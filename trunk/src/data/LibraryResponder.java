package data;

import java.util.*;
import data.messages.*;
import network.*;
import network.messages.*;

public class LibraryResponder extends RemoteResponder {

	private Library library;
	
	public LibraryResponder(Library library) throws NullPointerException {
		if(null == library)
			throw new NullPointerException("parent cannot be null");
		
		this.library = library;
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(message instanceof RoutedMessage)
			return RoutedResponder.routeMessage((RoutedMessage)message);
		
		if(!(message instanceof LibraryMessage))
			throw new IllegalArgumentException("illegal message type" + message.getClass().toString());
		
		switch(((RemoteMessage)message).getMessageType()) {
		case LibraryMessage.MSG_MASTER:
			return this.handleMasterRequest((LibraryMessage)message);
		case LibraryMessage.MSG_ITERATOR:
			return this.handleIteratorRequest((LibraryMessage)message);
		default:
			RemoteMessage errorMessage = new LibraryMessage(LibraryMessage.MSG_ERROR);
			errorMessage.queueParameter("illegal message type");
			return errorMessage;
		}
	}
	
	private LibraryMessage handleMasterRequest(LibraryMessage message) throws NullPointerException, IllegalArgumentException {
		LibraryMessage response = new LibraryMessage(LibraryMessage.MSG_MASTER);
		
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(LibraryMessage.MSG_MASTER != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		Bookshelf masterShelf = this.library.getMasterShelf();
		if(null == masterShelf) {
			response = new LibraryMessage(LibraryMessage.MSG_ERROR);
			response.queueParameter("internal error");
			return response;
		}
		
		/* create response, and responder object */
		response.queueParameter((new BookshelfResponder(masterShelf)).getID());
		return response;
	}
	
	private LibraryMessage handleIteratorRequest(LibraryMessage message) throws NullPointerException, IllegalArgumentException {
		LibraryMessage response = new LibraryMessage(LibraryMessage.MSG_ITERATOR);
		
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(LibraryMessage.MSG_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		LibraryIteratorResponder responder = new LibraryIteratorResponder(this.library.iterator());
		response.queueParameter(responder.getID());
		return response;
	}

}
