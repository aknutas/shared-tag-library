package data;

import network.*;
import network.messages.Message;
import data.messages.*;

public abstract class RemoteObject implements ClientResponder {

	boolean alive;
	private int connection;
	private Control network;
	
	private class Response implements ClientResponder {

		private RemoteMessage message;
		
		public boolean isReady() {
			return (null != message);
		}
		
		public RemoteMessage getMessage() {
			return this.message;
		}
		
		@Override
		public void onMessage(Message message) {
			if(!(message instanceof RemoteMessage))
				throw new IllegalArgumentException("illegal message type");
			
			this.message = (RemoteMessage)message;
		}
		
	}
	
	public RemoteObject(int connection, Control network) throws NullPointerException {
		if(null == network)
			throw new NullPointerException("network cannot be null");
		
		this.connection = connection;
		this.network = network;
		this.alive = false;
		
		this.network.sendLibraryMsg(this.connection, new RemoteMessage(RemoteMessage.MSG_HELLO), this);
	}
	
	public RemoteMessage send(RemoteMessage message) throws NullPointerException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		Response response = new Response();
		
		this.network.sendLibraryMsg(this.connection, message, response);
		while(!response.isReady());
		
		return response.getMessage();
	}
	
	public void onMessage(Message message) throws NullPointerException, IllegalArgumentException {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		if(!(message instanceof RemoteMessage))
			throw new IllegalArgumentException("illegal message type");
		
		//if((message.getMessageType())
	}
	
}
