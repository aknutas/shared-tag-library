package network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

import network.messages.Message;

/**
 * Class ControlImpl
 * 
 * @author Antti Knutas
 * 
 */
public class ControlImpl implements Control, ConnectionCallBack {

    private HashMap<Integer, CommThread> threadCollection;
    private long id;
    private int conncounter;
    Random rd;
    ServerResponder messageReceiver;
    ConnectionListener cl;

    /**
     * Private default constructor prevents object creation without proper
     * parameters.
     */
    private ControlImpl() {
	rd = new Random();
	id = rd.nextLong();
	threadCollection = new HashMap<Integer, CommThread>();
	conncounter = 0;
	cl = new ConnectionListener(this);
    };

    /**
     * Use this constructor OR ELSE
     * 
     * @param messageReceiver
     *            A class that answers remote queries. Usually the Library class
     *            in this context.
     */
    public ControlImpl(ServerResponder messageReceiver) {
	this(); // Using the default constructor
	this.messageReceiver = messageReceiver;
	cl.start();
    };

    /**
     * A command for the network interface to connect to a server address.
     * Return value is 0 for failure, or a number for connection identifier.
     * 
     * @return int
     * @param address
     *            Connection IP
     * @throws IOException
     * @throws UnknownHostException
     */
    public synchronized int connect(String address)
	    throws UnknownHostException, IOException {
	//Debug
	System.out.println("Controller here. Starting to connect.");
	InetSocketAddress caddress = new InetSocketAddress(address, Definitions.PORT);
	Socket s = new Socket();
	s.setSoTimeout(Definitions.TIMEOUT);
	s.connect(caddress, Definitions.PORT);
	ClientThread ct = new ClientThread(id, s, messageReceiver);
	ct.start();
	threadCollection.put(conncounter, ct);
	conncounter++;
	//Debug
	System.out.println("Controller here. Connected to " + address + " with thread " + (conncounter-1));
	return (conncounter - 1);
    }

    /**
     * A command to disconnect a certain client or server connection.
     * 
     * @return int
     * @param connection
     *            The connection ID to be disconnected.
     */
    public synchronized boolean disconnect(int connection) {
	try {
	    threadCollection.get(connection).end();
	    threadCollection.remove(connection);
	} catch (Exception e) {
	    return false;
	}
	return true;
    }

    /**
     * A command to send a data object to a remote library.
     * 
     * @param connection
     *            The connection ID.
     * @param message
     *            The message to be sent.
     * @param receiver
     *            The message listener which should receive the reply.
     */
    public synchronized void sendLibraryMsg(int connection, Message message,
	    ClientResponder receiver) {
	message.setComID(id);
	message.setMsgID(rd.nextLong());
	threadCollection.get(connection).sendMsgGetReply(message, receiver);
    }

    /**
     * A command to send a data object to the specified connection, with no
     * direct reply expected.
     * 
     * @param connection
     *            The connection ID.
     */
    public synchronized void sendMsg(int connection, Message message) {
	message.setComID(id);
	threadCollection.get(connection).sendMsg(message);
    }

    /**
     * A query of incoming messages. (chats, disconnections, etc.)
     * 
     * @return A map that contains a list of all incoming messages. Sorted by
     *         connection id.
     */
    public synchronized Map<Integer, List<Message>> whatsUp() {
	Map<Integer, List<Message>> returnmap = new HashMap<Integer, List<Message>>();
	List<Message> tempqueue;
	Integer key;

	//TODO Iterate with entrysets instead (full iterator faster than .get() seeks)
	Set<Integer> keyset = threadCollection.keySet();
	Iterator<Integer> i = keyset.iterator();

	// Debug
	System.out
		.println("Control here. Iterating through the threadCollection.");

	while (i.hasNext()) {
	    key = i.next();
	    // Debug
	    System.out.println("Iterating, queue: " + key);
	    tempqueue = threadCollection.get(key).getMsg();
	    // Debug
	    if (tempqueue != null) {
		System.out.println("Queue size: " + tempqueue.size());
		returnmap.put(key, tempqueue);
	    } else {
		System.out.println("Queue size: null");
	    }
	}

	if (returnmap.isEmpty())
	    return null;
	return returnmap;
    }

    /**
     * A query of thread statuses. (connection, data transfer, etc.)
     * 
     * @return An integer-integer map of statuses. First integer is connection
     *         id and the second is connection status.
     */
    public synchronized Map<Integer, Integer> getStatus() {
	Map<Integer, Integer> returnmap = new HashMap<Integer, Integer>();
	
	Integer key;
	Integer status;

	//TODO Iterate with entrysets instead (full iterator faster than .get() seeks)
	Set<Integer> keyset = threadCollection.keySet();
	Iterator<Integer> i = keyset.iterator();

	while (i.hasNext()) {
	    key = i.next();
	    status = threadCollection.get(key).getStatus();
	    returnmap.put(key, status);
	}
	
	if (returnmap.isEmpty())
	    return null;
	return returnmap;
    }

    /**
     * A request to start handle the given socket
     * 
     * @param socket
     *            A new socket.
     */
    public synchronized void gimmeThread(Socket socket) {
	CommThread ct = new ServerThread(id, socket, messageReceiver);
	ct.start();
	threadCollection.put(conncounter, ct);
	//Debug
	System.out.println("Controller here. Started listening to connection " + conncounter);
	conncounter++;
    }

}
