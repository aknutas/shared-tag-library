package data;

import java.io.*;
import java.util.*;
import data.messages.*;

public class IteratorResponder extends RoutedResponder {

	private Iterator<Serializable> it;
	
	public RoutedMessage onRoutedMessage(RoutedMessage message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof IteratorMessage))
			throw new IllegalArgumentException("illegal message type");
		
		if(IteratorMessage.MSG_MORE != message.getMessageType())
			return this.handleMoreMessage((IteratorMessage)message);
		
		RoutedMessage error = new RoutedMessage(RemoteMessage.MSG_ERROR, message.getID());
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
			IteratorMessage error = new IteratorMessage(IteratorMessage.MSG_ERROR, message.getID());
			error.queueParameter("could not read element count");
			return error;
		}
		
		IteratorMessage response = new IteratorMessage(IteratorMessage.MSG_MORE, message.getID());
		int i = elementCount.intValue();
		for(; it.hasNext() && i > 0; --i)
			response.queueParameter((Serializable)it.next());
		
		return response;
	}
	
}
