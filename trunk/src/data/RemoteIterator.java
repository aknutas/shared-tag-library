package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import network.Control;
import data.messages.IteratorMessage;
import data.messages.RemoteMessage;

/**
 * The RemoteIterator class extends RemoteObject and is used to allow iterators
 * of RemoteObjects to be used across a network.
 * 
 * @author Andrew Alm
 */
public abstract class RemoteIterator<T> extends RemoteObject implements
	Iterator<T> {

    private final int id;
    private boolean exhausted;
    private Queue<T> objectQueue;

    /**
     * Creates a new RemoteIterator object with the given id. Each
     * RemoteIterator is identified by a unique id the remote server generated.
     * 
     * @param network
     *            the network Control interface to use
     * @param connection
     *            the connection to use
     * @param id
     *            the iterator id to use
     * 
     * @throws NullPointerException
     *             if the network given is null
     * @throws RemoteObjectException
     *             if there is was an error creating this RemoteObject.
     */
    public RemoteIterator(Control network, int connection, int id)
	    throws NullPointerException, RemoteObjectException {
	super(connection, network);

	this.id = id;
	this.exhausted = false;
	this.objectQueue = new LinkedList<T>();

	/* populate remote iterator */
	if (0 > this.recieveMessages(20))
	    throw new RemoteObjectException();
    }

    /**
     * Gets the ID of the RemoteIterator object.
     * 
     * @return the id
     */
    public int getID() {
	return this.id;
    }

    /**
     * Used to convert an object parameter into a RemoteObject.
     * 
     * @param parameter
     *            the Object to attempt to convert
     * @return the RemoteObject, or null if there was a problem
     * 
     * @throws NullPointerException
     *             if the parameter given is null.
     * @throws IllegalArgumentException
     *             if parameter given is of the wrong type.
     */
    public abstract T createObject(IteratorMessage message)
	    throws NullPointerException, IllegalArgumentException;

    /**
     * Determines whether this remote iterator has any elements left to iterator
     * over.
     * 
     * @return true if next will not throw
     */
    @Override
    public boolean hasNext() {
	final RemoteIterator<T> self = this;

	if (this.exhausted)
	    return false;

	if (this.objectQueue.isEmpty())
	    this.exhausted = (1 > this.recieveMessages(10));

	/* we are running low on objects, get some more */
	if ((!this.exhausted && (20 > this.objectQueue.size()))) {
	    new Thread(new Runnable() {
		public void run() {
		    self.recieveMessages(10);
		}
	    }).start();
	}

	return (!this.exhausted);
    }

    /**
     * Returns the next RemoteObject available, or throws an exception.
     * 
     * @return the next available RemoteObject
     * 
     * @throws NoSuchElementException
     *             if there are no more RemoteObjects to return.
     */
    @Override
    public T next() {
	if (!this.hasNext())
	    throw new NoSuchElementException();

	if (this.objectQueue.isEmpty())
	    throw new NoSuchElementException();

	return this.objectQueue.poll();
    }

    /**
     * Not implemented (optional)
     */
    @Override
    public void remove() {
    }

    /**
     * This method is used to fetch the given number of RemoteObjects for the
     * RemoteIterator. If the number of objects fetched is less than the number
     * requested, then no more objects will be available.
     * 
     * @return the number of RemoteObjects read, or -1 to indicate an error
     *         occurred
     */
    private synchronized int recieveMessages(int count) {
	int i;

	if (this.exhausted)
	    return 0;

	/* send for more troops! */
	RemoteMessage message = new IteratorMessage(IteratorMessage.MSG_MORE,
		this.id);
	message.queueParameter(new Integer(count));
	message = this.send(message, 5000);
	if (null == message)
	    return -1;

	/* an iterator error indicates the iterator is empty */
	if (IteratorMessage.MSG_ERROR == message.getMessageType()) {
	    this.exhausted = true;
	    return 0;
	}

	/* drain message parameter queue */
	for (i = 0;; ++i) {
	    T object = this.createObject((IteratorMessage) message);
	    if (null == object)
		break;

	    this.objectQueue.offer(object);
	}

	return i;
    }

}
