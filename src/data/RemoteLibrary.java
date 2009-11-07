package data;

import java.util.*;
import data.messages.*;
import network.*;
import network.messages.*;



/**
 * The RemoteLibrary class implements the Library interface and is used to
 * represent a remote library connection on the client side.
 * 
 * @author Andrew Alm
 */
public class RemoteLibrary implements Library, ClientMessageReceiver {

	private int connection;
	private Control network;

	/**
	 * Creates a new RemoteLibrary object with the given connection
	 * and network.
	 * 
	 * @param connection the connection to use
	 * @param network the network control to use
	 * 
	 * @throws NullPointerException if the network given is null
	 */
	public RemoteLibrary(int connection, Control network) throws NullPointerException {
		if(null == network)
			throw new NullPointerException("network cannot be null");
		
		this.connection = connection;
		this.network = network;
		
		this.network.sendLibraryMsg(this.connection, new LibraryMessage(LibraryMessage.MSG_HELLO), this);
	}
		
	
	/**
	 * Operation not permitted.
	 */
	@Override
	public boolean addBookshelf(Bookshelf shelf) throws NullPointerException {
		return false;
	}
	
	/**
	 * Operation not permitted.
	 */
	@Override
	public boolean removeBookshelf(Bookshelf shelf) throws NullPointerException {
		return false;
	}

	/**
	 * Operation not finished.
	 */
	@Override
	public Bookshelf getMasterShelf() {
		return null;
	}

	/**
	 * Operation not finished.
	 */
	@Override
	public Iterator<Bookshelf> iterator() {
		return null;
	}

	@Override
	public void onMessageRecive(Message message) throws IllegalArgumentException {
		if(!(message instanceof LibraryMessage))
			throw new IllegalArgumentException("message given is null");
		
		switch(((LibraryMessage)message).getMessageType()) {
		case LibraryMessage.MSG_HELLO:
			System.out.println("recieved hello");
			break;
		}
	}

}
