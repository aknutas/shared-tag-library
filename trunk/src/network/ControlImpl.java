package network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;

import network.messages.Message;

/**
 * Class ControlImpl This class allows for control and polling of the networking
 * threads.
 * 
 * @author Antti Knutas
 * 
 */
public class ControlImpl implements Control, ConnectionCallBack {

    private HashMap<Integer, CommThread> threadCollection;
    private HashMap<Integer, Set<ClientResponder>> connectionObjects;
    private long id;
    private int conncounter;
    private Random rd;
    private ServerResponder messageReceiver;
    private ConnectionListener cl;

    /**
     * Private default constructor prevents object creation without proper
     * parameters.
     */
    private ControlImpl() {
	rd = new Random();
	id = rd.nextLong();
	connectionObjects = new HashMap<Integer, Set<ClientResponder>>();
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
	    throws UnknownHostException, IOException, ConnectException {
	// Debug
	// System.out.println("Controller here. Starting to connect.");
	InetSocketAddress caddress = new InetSocketAddress(address,
		Definitions.PORT);
	Socket s = new Socket();
	s.connect(caddress, Definitions.TIMEOUT);
	ClientThread ct = new ClientThread(id, s, messageReceiver);
	ct.start();
	threadCollection.put(conncounter, ct);
	connectionObjects.put(conncounter, new HashSet<ClientResponder>());
	conncounter++;
	// Debug
	// System.out.println("Controller here. Connected to " + address
	// + " with thread " + (conncounter - 1));
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
	// Telling the connection thread to end, and removing it from
	// threadcollecion
	try {
	    threadCollection.get(connection).end();
	    threadCollection.remove(connection);
	} catch (Exception e) {
	    return false;
	}
	// Notifying registered objects that they have been disconnected, and
	// then removing the set from the notify collection
	try {
	    Set<ClientResponder> notifyset = connectionObjects.get(connection);
	    Iterator<ClientResponder> i = notifyset.iterator();
	    while (i.hasNext()) {
		i.next().onDisconnect(connection);
	    }
	    connectionObjects.remove(connection);
	} catch (Exception e) {
	    e.printStackTrace();
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
	// Debug
	/*
	 * System.out .println("Control here. Sent: " +
	 * message.getClass().getName() + " to connection " + connection);
	 */
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
	Entry<Integer, CommThread> entry;

	Set<Entry<Integer, CommThread>> entryset = threadCollection.entrySet();
	Iterator<Entry<Integer, CommThread>> i = entryset.iterator();

	// Debug
	/*
	 * System.out
	 * .println("Control here. Iterating through the threadCollection.");
	 */

	while (i.hasNext()) {
	    entry = i.next();
	    // Debug
	    // System.out.println("Iterating, queue: " + entry.getKey());
	    tempqueue = entry.getValue().getMsg();
	    if (tempqueue != null) {
		// Debug
		// System.out.println("Queue size: " + tempqueue.size());
		returnmap.put(entry.getKey(), tempqueue);
	    } /*
	       * else { System.out.println("Queue size: null"); }
	       */// Debug
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
	Entry<Integer, CommThread> entry;

	Set<Entry<Integer, CommThread>> entryset = threadCollection.entrySet();
	Iterator<Entry<Integer, CommThread>> i = entryset.iterator();

	while (i.hasNext()) {
	    entry = i.next();
	    returnmap.put(entry.getKey(), entry.getValue().getStatus());
	}

	if (returnmap.isEmpty())
	    return null;
	return returnmap;
    }

    /**
     * A request to start handling the given socket
     * 
     * @param socket
     *            A new socket.
     */
    public synchronized void gimmeThread(Socket socket) {
	CommThread ct = new ServerThread(id, socket, messageReceiver);
	ct.start();
	threadCollection.put(conncounter, ct);
	connectionObjects.put(conncounter, new HashSet<ClientResponder>());
	// Debug
	/*
	 * System.out.println("Controller here. Started listening to connection "
	 * + conncounter + " from " +
	 * socket.getInetAddress().getHostName().toString());
	 */
	conncounter++;
    }

    /**
     * A request to start shutdown routines regarding networking. This stops
     * connection listening, and starts to disconnect threads.
     */
    public synchronized void shutDown() {
	// Collectionlistener stops
	cl.shutdown();
	// Iterates through all the threads and tells them to stop
	Collection<CommThread> shutlist = threadCollection.values();
	Iterator<CommThread> i = shutlist.iterator();
	while (i.hasNext()) {
	    i.next().end();
	    i.remove();
	}
    }

    /**
     * The method is used to register objects that need to be notified when the
     * connection this specific ID gets shut down or stuff happens.
     * 
     * @param connId
     *            The connection id.
     * @param connUser
     *            The object to be registered for listening.
     * @return Returns the connection id. Or 0 in case of failure.
     */
    public synchronized int registerAnomalyListener(int connId,
	    ClientResponder connUser) {
	try {
	    Set<ClientResponder> notifyset = connectionObjects.get(connId);
	    notifyset.add(connUser);
	} catch (Exception e) {
	    connId = 0;
	}
	return connId;
    }
}
