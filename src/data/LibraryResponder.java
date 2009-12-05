package data;

import java.util.*;
import data.messages.*;
import network.*;
import network.messages.*;

/**
 * The LibraryResponder class extends the RemoteResponder abstract class and is
 * used to respond to all messages originating from this LibraryResponder
 * object. It uses a Library object to generate appropriate responses.
 * 
 * @author Andrew Alm
 */
public class LibraryResponder extends RemoteResponder {

	private Library library;
	
	/**
	 * Creates a new LibraryResponder for the given Library object.
	 * 
	 * @param library the library to use for responses
	 * 
	 * @throws NullPointerException if the library given is null
	 */
	public LibraryResponder(Library library) throws NullPointerException {
		if(null == library)
			throw new NullPointerException("library cannot be null");
		
		this.library = library;
	}

	/**
	 * This method is called when a message is received from a
	 * RemoteObject. It generates an appropriate RemoteMessage
	 * response to the message given.
	 * 
	 * @param message the RemoteMessage to respond to
	 * 
	 * @return a RemoteMessage response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message given cannot
	 *         be processed
	 */
	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		/* remote bookshelves and iterators */
		if(message instanceof RoutedMessage)
			return RoutedResponder.routeMessage((RoutedMessage)message);
		
		/* not a remote message this object should have been given */
		if(!(message instanceof LibraryMessage))
			throw new IllegalArgumentException("illegal message type");
		
		/* send response, or error if message type is illegal */
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
	
	/**
	 * This method is used to handle a MSG_MASTER LibraryMessage. It
	 * responds by creating a master bookshelf responder and
	 * responding with the bookshelf id as the only parameter.
	 * 
	 * @param message the MSG_MASTER to respond to
	 * 
	 * @return a LibraryMessage response
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the message type is not a
	 *         MSG_MASTER
	 */
	private LibraryMessage handleMasterRequest(LibraryMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(LibraryMessage.MSG_MASTER != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		LibraryMessage response = new LibraryMessage(LibraryMessage.MSG_MASTER);
		
		/* get master bookshelf */
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
	
	/**
	 * Responds to an MSG_ITERATOR type LibraryMessage. This will
	 * respond with a LibraryMessage containing the id of the
	 * IteratorResponder this method creates.
	 * 
	 * @param message the MSG_ITERATOR message to respond to
	 * 
	 * @return the response message
	 * 
	 * @throws NullPointerException if the message given is null
	 * @throws IllegalArgumentException if the LibraryMessage type is
	 *         not MSG_ITERATOR
	 */
	private LibraryMessage handleIteratorRequest(LibraryMessage message) throws NullPointerException, IllegalArgumentException {
		LibraryMessage response = new LibraryMessage(LibraryMessage.MSG_ITERATOR);
		
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(LibraryMessage.MSG_ITERATOR != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		BookshelfIteratorResponder responder = new BookshelfIteratorResponder(this.library.iterator());
		response.queueParameter(responder.getID());
		return response;
	}

}