package data;

import network.Control;
import data.messages.IteratorMessage;

public class RemoteBookIterator extends RemoteIterator<Book> {

	public RemoteBookIterator(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
		super(network, connection, id);
	}

	@Override
	public Book createObject(IteratorMessage message) throws NullPointerException, IllegalArgumentException {
		try {
			if(null == message)
				throw new NullPointerException("message cannot be null");
			
			if(IteratorMessage.MSG_MORE != message.getMessageType())
				throw new IllegalArgumentException("illegal message type");
			
			Integer id = message.dequeParameter();
			if(null == id)
				return null;
			
			return new RemoteBook(this.connection, this.network, id.intValue());
		}
		catch(RemoteObjectException ex) {
			return null;
		}
	}

}
