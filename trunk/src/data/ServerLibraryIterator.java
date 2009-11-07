package data;

import java.util.*;
import network.*;
import network.messages.Message;

public class ServerLibraryIterator implements Iterator<Bookshelf>, ServerResponder {

	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Bookshelf next() {
		return null;
	}

	@Override
	public void remove() {}
	
	@Override
	public Message onMessage(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
