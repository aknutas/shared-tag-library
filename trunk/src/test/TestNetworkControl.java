package test;

import java.io.*;
import java.net.*;
import java.util.*;
import data.*;
import network.*;
import network.messages.*;



/**
 * The TestNetworkControl class implements the network Control interface and is
 * used to create test network scenarios.
 * 
 * @author Andrew Alm
 */
public class TestNetworkControl extends TestNetwork implements Control {
	
	private int connectionID;
	private Map<Integer, ServerResponder> connections;
	
	public TestNetworkControl() {
		this.connectionID = 0;
		this.connections = new HashMap<Integer, ServerResponder>();
	}
	
	@Override
	public int connect(String address) throws UnknownHostException, IOException {
		if(null == address)
			throw new NullPointerException("address cannot be null");
		
		/* host lookup */
		Library library = this.getLibrary(address);
		if(null == library)
			throw new UnknownHostException("library '" + address + "' not found");
		
		/* found, create unique connection id */
		int connectionID = this.connectionID;
		this.connectionID += 1;
		
		/* initialize server object */
		this.connections.put(connectionID, new ServerLibrary(library));
				
		return connectionID;
	}

	@Override
	public boolean disconnect(int connection) {
		return (null != this.connections.remove(connections));
	}

	

	@Override
	public void sendLibraryMsg(final int connection, final Message message, final ClientResponder receiver) throws NullPointerException, IllegalArgumentException {
		if((null == message) || (null == receiver))
			throw new NullPointerException("message or receiver cannot be null");
		
		final ServerResponder server = this.connections.get(connection);
		if(null == server)
			throw new IllegalArgumentException("unknown connection");
		
		(new Thread(new Runnable() {
			public void run() {
				Message response = server.onMessageRecive(message);
				receiver.onMessageRecive(response);
			}
		})).start();
	}

	@Override
	public void sendMsg(int connection, final Message message) {
		if(null == message)
			throw new NullPointerException("message cannot be null");
		
		final ServerResponder server = this.connections.get(connection);
		if(null == server)
			throw new IllegalArgumentException("unknown connection");
		
		(new Thread(new Runnable() {
			public void run() {
				server.onMessageRecive(message);
			}
		})).start();
	}

	/**
	 * Not implemented.
	 */
	@Override
	public Map<Integer, Integer> getStatus() { return null; }
	
	/**
	 * Not implemented.
	 */
	@Override
	public Map<Integer, List<Message>> whatsUp() { return null; }
	
}
