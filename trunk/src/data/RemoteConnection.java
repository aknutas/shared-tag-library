package data;

import java.io.*;
import java.net.*;
import java.util.*;
import network.*;
import network.messages.*;

public class RemoteConnection implements Runnable {

	private final Socket socket;
	private final Communication communcation;
	private List<ClientResponder> listener;
		
	public RemoteConnection(Socket socket) throws NullPointerException {
		if(null == socket)
			throw new NullPointerException("socket cannot be null");
		
		this.socket = socket;
		this.communcation = new CommunicationImpl();
	}
	
	/**
	 * A blocking send message. This method sends a message and 
	 * blocks until a result is returned from the remote peer.
	 * 
	 * @param message the message object to send.
	 * 
	 * @return the reply Message object from the peer, or null if an
	 *         error occured.
	 * 
	 * @throws NullPointerException if the message given is null.
	 */
	public Message send(Message message) throws NullPointerException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		try {
			this.communcation.Send(this.socket, message);
		} catch(IOException ex) {
			return null;
		}
		
		return this.blockReceive();
	}
	
	/**
	 * Wait in loop for message receive.
	 * 
	 * @return
	 */
	private Message blockReceive() {
		Object replyObject = null;
		
		try {
			do {
				replyObject = this.communcation.Receive(this.socket);
			} while(null == replyObject);
		} catch(IOException ex) {
			return null;
		}
		
		if(!(replyObject instanceof Message))
			return null;
		
		return (Message)replyObject;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
