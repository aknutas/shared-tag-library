package network;

import network.messages.NetworkMessage;

/**
 * The Connection class is a dummy class to demonstrate the required
 * functionality of the data structures.
 * 
 * @author Andrew Alm
 */
public abstract class Connection {

	public NetworkMessage sendMessage(NetworkMessage msg) {
		return msg;
	}
	
}
