package data;

import java.util.*;
import data.messages.*;
import network.*;
import network.messages.*;

public class ServerLibrary implements ServerResponder {

	private int shelfId;
	private Library parent;
	private Map<Integer, ServerResponder> shelfResponders;
	
	private ServerLibrary() {
		this.shelfResponders = new HashMap<Integer, ServerResponder>();
		this.shelfId = 0;
	}
	
	public ServerLibrary(Library parent) throws NullPointerException {
		this();
		
		if(null == parent)
			throw new NullPointerException("parent cannot be null");
		
		this.parent = parent;
	}
	
	@Override
	public Message onMessage(Message message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(message instanceof BookshelfMessage) {
			BookshelfMessage bookshelfMessage = (BookshelfMessage)message;
			
			ServerResponder responder = this.shelfResponders.get(bookshelfMessage.getId());
			if(null == responder)
				throw new IllegalArgumentException("unknown bookshelf id");
			
			return responder.onMessage(message);
		}
		
		if(!(message instanceof LibraryMessage))
			throw new IllegalArgumentException("illegal message type");
		
		LibraryMessage libraryMessage = (LibraryMessage)message;
		RemoteMessage response = null;
		
		switch(libraryMessage.getMessageType()) {
		case LibraryMessage.MSG_HELLO:
			response = new LibraryMessage(LibraryMessage.MSG_HELLO);
			break;
		case LibraryMessage.MSG_MASTER:
			ServerBookshelf masterShelf = new ServerBookshelf(this.parent.getMasterShelf(), this.shelfId);
			this.shelfResponders.put(this.shelfId, masterShelf);
			
			response = new LibraryMessage(LibraryMessage.MSG_MASTER);
			response.queueParameter(this.shelfId);
			
			this.shelfId += 1;
			break;
		}
		
		return response;		
	}

}
