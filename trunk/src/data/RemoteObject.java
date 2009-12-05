package data;

import java.util.*;
import java.util.concurrent.*;
import network.*;
import network.messages.Message;
import data.messages.*;


/**
 * The RemoteObject class implements the ClientResponder interface and is used
 * as the base object for all Remote (client side) Libraries, Bookshelves, and
 * Books. It provides simple communication facilities (blocking send, ping) for
 * all objects. 
 * 
 * @author Andrew Alm
 */
public abstract class RemoteObject implements ClientResponder {
		
	final protected int connection;
	final protected Control network;
	
	/* used to provide simple blocking */
	private class Response implements ClientResponder {

		private final Semaphore lock;
		private RemoteMessage message;
		private long time;
		
		public Response() {
			this.message = null;
			this.lock = new Semaphore(0);
		}
				
		public RemoteMessage block(long timeout) {
			boolean acquired;
			
			try {
				this.time = System.currentTimeMillis();
				
				if(0 == timeout)
					acquired = this.lock.tryAcquire();
				else
					acquired = this.lock.tryAcquire(timeout, TimeUnit.MILLISECONDS);
				
				this.time = System.currentTimeMillis() - this.time;
				
				return (true == acquired) ? this.message : null;
			}
			catch(InterruptedException ex) {
				this.time = 0;
				return null;
			}
		}
		
		public long getResponseTime() {
			return this.time;
		}
		
		public void onMessage(Message message) {
			if(!(message instanceof RemoteMessage))
				throw new IllegalArgumentException("illegal message type");
			
			this.message = (RemoteMessage)message;
			this.lock.release();
		}

		@Override
		public void onDisconnect() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLoop() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Creates a new RemoteObject from the given connection and
	 * network Control object.
	 * 
	 * @param connection the connection id
	 * @param network the network Control object
	 * 
	 * @throws NullPointerException if the network control object is
	 *         null
	 * @throws RemoteObjectException if the RemoteObject cannot be
	 *         created (timeout = 5000 ms)
	 */
	public RemoteObject(int connection, Control network) throws NullPointerException, RemoteObjectException {
		this(connection, network, 5000);
	}
	
	/**
	 * Creates a new RemoteObject from the given connection and
	 * network Control object. In order for a remote object to be
	 * created, the connection must respond to a ping.
	 * 
	 * @param connection the connection id
	 * @param network the network Control object
	 * 
	 * @throws NullPointerException if the network control object is
	 *         null
	 * @throws RemoteObjectException if the RemoteObject cannot be
	 *         created
	 */
	public RemoteObject(int connection, Control network, long timeout) throws NullPointerException, RemoteObjectException {
		if(null == network)
			throw new NullPointerException("network cannot be null");
		
		this.connection = connection;
		this.network = network;
		
		if(!this.ping(timeout))
			throw new RemoteObjectException();
	}
	
	/**
	 * Gets the connection ID of the RemoteObject.
	 * 
	 * @return the connection ID
	 */
	public int getConnectionID() {
		return this.connection;
	}
	
	/**
	 * Sends the given message over the connection this RemoteObject
	 * contains, returning the response message. This method will 
	 * block until there is a response.
	 * 
	 * @param message the message to send
	 * 
	 * @return the response message
	 * 
	 * @throws NullPointerException if the message given is null
	 */
	public RemoteMessage send(RemoteMessage message) throws NullPointerException {
		return this.send(message, 0);
	}
	
	/**
	 * Sends the given message over the connection this RemoteObject
	 * contains, returning the response message. This method will 
	 * block until there is a response, or the timeout is reached.
	 * 
	 * @param message the message to send
	 * @param timeout the maximum amount of time to wait for a
	 *        response, or 0 to wait indefinitely
	 *        
	 * @return the response message, or null if the operation timed
	 *         out
	 * 
	 * @throws NullPointerException if the message given is null
	 */
	public RemoteMessage send(RemoteMessage message, long timeout) throws NullPointerException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
	
		if(message instanceof TrackedMessage)
			((TrackedMessage)message).messageSent();
		
		Response response = new Response();
		this.network.sendLibraryMsg(this.connection, message, response);
		RemoteMessage responseMessage = response.block(timeout);
		
		if(responseMessage instanceof TrackedMessage)
			((TrackedMessage)message).messageReceived();
		
		return responseMessage;
	}
	
	/**
	 * Pings the remote connection to determine if the connection is
	 * still alive.
	 * 
	 * @return true if there was a response, false otherwise
	 */
	public boolean ping(long timeout) {
		Response response = new Response();
		
		this.network.sendLibraryMsg(this.connection, new RemoteMessage(RemoteMessage.MSG_PING), response);
		if(null == response.block(timeout))
			return false;

		return true;
	}
	
	/**
	 * This method is fired when a message is received.  The remote object only 
	 */
	public void onMessage(Message message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof RemoteMessage))
			throw new IllegalArgumentException("illegal message type");
		
		if(RemoteMessage.MSG_PING == ((RemoteMessage)message).getMessageType())
			return;
	}
	
	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoop() {
		// TODO Auto-generated method stub	
	}
	
}
