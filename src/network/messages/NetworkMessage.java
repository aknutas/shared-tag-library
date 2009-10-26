package network.messages;

import network.*;

import java.io.*;
import java.util.*;

public class NetworkMessage {
	
	/* Bookshelf functions */
	public static final int LIB_CLOSE_REQUEST = 0;
	public static final int LIB_CLOSE_RESPONSE = 1;
	public static final int LIB_MASTER_REQUEST = 2;
	public static final int LIB_MASTER_RESPONSE = 3;
	public static final int LIB_ITERATE_REQUEST = 4;
	public static final int LIB_ITERATE_RESPONSE = 5;
	
	/* Bookshelf functions */
	public static final int SHELF_CLOSE = 4;
	public static final int SHELF_SIZE = 5;
	
	/* Message type */
	private final int type;
	
	/* Network stuff */
	private Connection connection;
	
	public NetworkMessage(Connection connection, int type) {
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void putData(Serializable data) {
	}
	
	/* send message to other side, with given parameters */
	public void send() {
	}
	
	
	/* recv message from other side */
	public Object recv() {
		return null;
	}
	
}
