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

    /* statistics */
    private long messagesSent;
    private long messagesReceived;
    private long bytesSent;
    private long bytesReceived;

    public TestNetworkControl() {
	this.connectionID = 0;
	this.connections = new HashMap<Integer, ServerResponder>();

	/* initialize statistics */
	this.messagesSent = 0;
	this.bytesSent = 0;

	this.messagesReceived = 0;
	this.bytesReceived = 0;
    }

    public long getMessagesSent() {
	return this.messagesSent;
    }

    public long getMessagesReceived() {
	return this.messagesReceived;
    }

    public long getBytesSent() {
	return this.bytesSent;
    }

    public long getBytesReceived() {
	return this.bytesReceived;
    }

    @Override
    public int connect(String address) throws UnknownHostException, IOException {
	if (null == address)
	    throw new NullPointerException("address cannot be null");

	/* host lookup */
	Library library = this.getLibrary(address);
	if (null == library)
	    throw new UnknownHostException("library '" + address
		    + "' not found");

	/* found, create unique connection id */
	int connectionID = this.connectionID;
	this.connectionID += 1;

	/* initialize server object */
	this.connections.put(connectionID, new LibraryResponder(library));

	return connectionID;
    }

    @Override
    public boolean disconnect(int connection) {
	return (null != this.connections.remove(connections));
    }

    @Override
    public void sendLibraryMsg(final int connection, final Message message,
	    final ClientResponder receiver) throws NullPointerException,
	    IllegalArgumentException {
	if ((null == message) || (null == receiver))
	    throw new NullPointerException("message or receiver cannot be null");

	final ServerResponder server = this.connections.get(connection);
	if (null == server)
	    throw new IllegalArgumentException("unknown connection");

	final TestNetworkControl self = this;

	(new Thread(new Runnable() {
	    public void run() {
		Message response = server.onMessage(message);
		receiver.onMessage(response);

		self.recordReceive(message);
	    }
	})).start();

	this.recordSend(message);
    }

    @Override
    public void sendMsg(int connection, final Message message) {
	try {
	    if (null == message)
		throw new NullPointerException("message cannot be null");

	    final ServerResponder server = this.connections.get(connection);
	    if (null == server)
		throw new IllegalArgumentException("unknown connection");

	    (new Thread(new Runnable() {
		public void run() {
		    server.onMessage(message);
		}
	    })).start();

	    Thread.sleep(100);
	    this.recordSend(message);
	} catch (Exception ex) {

	}
    }

    private void recordSend(Message message) {
	try {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    (new ObjectOutputStream(bytes)).writeObject(message);
	    this.bytesSent += bytes.size();
	    this.messagesSent += 1;
	} catch (Exception ex) {
	    /* no stats... oh well */
	}
    }

    private void recordReceive(Message message) {
	try {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    (new ObjectOutputStream(bytes)).writeObject(message);
	    this.bytesReceived += bytes.size();
	    this.messagesReceived += 1;
	} catch (Exception ex) {
	    /* no stats... oh well */
	}
    }

    /**
     * Not implemented.
     */
    @Override
    public Map<Integer, Integer> getStatus() {
	return null;
    }

    /**
     * Not implemented.
     */
    @Override
    public Map<Integer, List<Message>> whatsUp() {
	return null;
    }

    /**
     * Not implemented.
     */
    @Override
    public void shutDown() {
	// TODO Auto-generated method stub

    }

    @Override
    public int registerAnomalyListener(int connId, ClientResponder connUser) {
	// TODO Auto-generated method stub
	return 0;
    }

}
