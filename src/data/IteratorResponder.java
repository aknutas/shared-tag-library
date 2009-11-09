package data;

import java.io.*;
import java.util.*;
import data.messages.*;

public abstract class IteratorResponder<T> extends RoutedResponder {

	private final Iterator<T> iter;
	
	public IteratorResponder(Iterator<T> iter) throws NullPointerException {
		super();
		
		if(null == iter)
			throw new NullPointerException("iter cannot be null");
		
		this.iter = iter;
	}
	
	public abstract Serializable serializeObject(T object) throws NullPointerException, IllegalArgumentException;
	
	public RoutedMessage onRoutedMessage(RoutedMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof IteratorMessage))
			throw new IllegalArgumentException("illegal message type");
		
		if(this.getID() != message.getID())
			throw new IllegalArgumentException("illegal route");
		
		if(IteratorMessage.MSG_MORE == message.getMessageType())
			return this.handleMoreMessage((IteratorMessage)message);
		
		RoutedMessage error = new RoutedMessage(RemoteMessage.MSG_ERROR, this.getID());
		error.queueParameter("unknown message type");
		return error;
	}
	
	private IteratorMessage handleMoreMessage(IteratorMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(IteratorMessage.MSG_MORE != message.getMessageType())
			throw new IllegalArgumentException("illegal message type");
		
		Integer elementCount = message.dequeParameter();
		if(null == elementCount) {
			IteratorMessage error = new IteratorMessage(IteratorMessage.MSG_ERROR, this.getID());
			error.queueParameter("could not read element count");
			return error;
		}
		
		/* if the iterator is empty, give an error*/
		if(!iter.hasNext())
			return new IteratorMessage(IteratorMessage.MSG_ERROR, this.getID());
		
		IteratorMessage response = new IteratorMessage(IteratorMessage.MSG_MORE, this.getID());
		int i = elementCount.intValue();
		for(; iter.hasNext() && i > 0; --i)
			response.queueParameter(this.serializeObject(iter.next()));
		
		return response;
	}
	
}
