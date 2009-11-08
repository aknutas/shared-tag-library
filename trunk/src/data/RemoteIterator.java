package data;

import java.util.*;

import data.messages.*;
import network.*;

/**
 * The RemoteIterator class extends RemoteObject and is used to allow iterators
 * of RemoteObjects to be used across a network.
 * 
 * @author Andrew Alm
 */
public abstract class RemoteIterator extends RemoteObject implements Iterator<RemoteObject> {

	private int id;
	private Queue<RemoteObject> objectQueue;
	
	/**
	 * Creates a new RemoteIterator object with the given id. Each
	 * RemoteIterator is identified by a unique id the remote server
	 * generated.
	 * 
	 * @param network the network Control interface to use
	 * @param connection the connection to use
	 * @param id the iterator id to use
	 * 
	 * @throws NullPointerException if the network given is null
	 * @throws RemoteObjectException if there is was an error
	 *         creating this RemoteObject.
	 */
	public RemoteIterator(Control network, int connection, int id) throws NullPointerException, RemoteObjectException {
		super(connection, network);
		
		this.id = id;
		this.objectQueue = new LinkedList<RemoteObject>();
		
		/* populate remote iterator */
		if(0 > this.recieveMessages(20))
			throw new RemoteObjectException();
	}
		
	/**
	 * Determines whether this remote iterator has any elements left
	 * to iterator over.
	 * 
	 * @return true if next will not throw 
	 */
	@Override
	public boolean hasNext() {
		final RemoteIterator self = this;
		
		if(this.objectQueue.isEmpty())
			return (1 > this.recieveMessages(10));
		
		/* we are running low on objects, get some more */
		if(20 > this.objectQueue.size()) {
			new Thread(new Runnable() {
				public void run() {
					self.recieveMessages(10);
				}
			}).start();
		}
		
		return true;
	}

	/**
	 * This method is used to fetch the given number of RemoteObjects
	 * for the RemoteIterator. If the number of objects fetched is
	 * less than the number requested, then no more objects will be
	 * available.
	 * 
	 * @return the number of RemoteObjects read, or -1 to indicate an
	 *         error occurred
	 */
	private int recieveMessages(int count) {
		int i;
		
		RemoteMessage message = new IteratorMessage(IteratorMessage.MSG_MORE, this.id);
		message.queueParameter(new Integer(count));
		
		/* send for more troops! */
		message = this.send(message, 5000);
		if(null == message)
			return -1;
		
		/* an iterator error indicates the iterator is empty */
		if(IteratorMessage.MSG_ERROR == message.getMessageType())
			return 0;
		
		/* empty message parameter queue */
		for(i = 0; ; ++i) {
			Object parameter = message.dequeParameter();
			if(null == parameter)
				break;
			
			RemoteObject object = this.createObject(parameter);
			if(null == object)
				break;
			
			this.objectQueue.offer(object);
		}
		
		return i;
	}
	
	/**
	 * Used to convert an object parameter into a RemoteObject.
	 * 
	 * @param parameter the Object to attempt to convert
	 * @return the RemoteObject, or null if there was a problem
	 * 
	 * @throws NullPointerException if the parameter given is null.
	 * @throws IllegalArgumentException if parameter given is of the 
	 *         wrong type.
	 */
	public abstract RemoteObject createObject(Object parameter) throws NullPointerException, IllegalArgumentException;
	
	/**
	 * Returns the next RemoteObject available, or throws an
	 * exception.
	 * 
	 * @return the next available RemoteObject
	 * 
	 * @throws NoSuchElementException if there are no more 
	 *         RemoteObjects to return.
	 */
	@Override
	public RemoteObject next() {
		if(!this.hasNext())
			throw new NoSuchElementException();
		
		if(this.objectQueue.isEmpty())
			throw new NoSuchElementException();
		
		return this.objectQueue.poll();
	}

	/**
	 * Not implemented (optional)
	 */
	@Override
	public void remove() {}
	
}
