package data;

import java.io.Serializable;
import java.util.Iterator;

import data.messages.RemoteMessage;

public class BookIteratorResponder extends IteratorResponder<Book> {

	public BookIteratorResponder(Iterator<Book> iter) throws NullPointerException {
		super(iter);
	}

	@Override
	public Serializable serializeObject(Book book) throws NullPointerException, IllegalArgumentException {
		if(null == book)
			throw new NullPointerException("shelf cannot be null");
		
		return new Integer((new BookResponder(book)).getID());
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
