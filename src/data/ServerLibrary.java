package data;

import java.util.*;
import data.messages.*;
import network.*;
import network.messages.*;

public class ServerLibrary implements Library, ServerMessageReceiver {

	private Library parent;
	
	public ServerLibrary(Library parent) throws NullPointerException {
		if(null == parent)
			throw new NullPointerException("parent cannot be null");
		
		this.parent = parent;
	}
	
	@Override
	public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
		return this.parent.addBookshelf(shelf);
	}

	@Override
	public Bookshelf getMasterShelf() {
		return this.parent.getMasterShelf();
	}

	@Override
	public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
		return this.parent.removeBookshelf(shelf);
	}

	@Override
	public Iterator<Bookshelf> iterator() {
		return this.parent.iterator();
	}

	@Override
	public Message onMessageRecive(Message message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof LibraryMessage))
			throw new IllegalArgumentException("illegal message type");
		
		LibraryMessage libraryMessage = (LibraryMessage)message;
		Message response = null;
		
		switch(libraryMessage.getMessageType()) {
		case LibraryMessage.MSG_HELLO:
			response = new LibraryMessage(LibraryMessage.MSG_HELLO);
			break;
		case LibraryMessage.MSG_MASTER:
			break;
		}
		
		return response;		
	}

}
