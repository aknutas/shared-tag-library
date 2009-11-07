package data.messages;

import network.ClientResponder;
import network.messages.Message;

public class Response implements ClientResponder {

	private Message message;
	
	public Response() {
		this.message = null;
	}
	
	public boolean messageRecieved() {
		return (null != this.message);
	}
	
	public Message getMessage() {
		return this.message;
	}
	
	@Override
	public void onMessage(Message message) {
		this.message = message;
	}

}
