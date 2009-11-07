package network;

import java.io.IOException;
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
    ServerMessageReceiver messageReceiver;
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
    public ControlImpl(ServerMessageReceiver messageReceiver) {
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
	Socket s = new Socket(address, Definitions.PORT);
	s.setSoTimeout(Definitions.TIMEOUT);
	ClientThread ct = new ClientThread(id, s, messageReceiver);
	ct.run();
	threadCollection.put(conncounter, ct);

	conncounter++;
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
	    ClientMessageReceiver receiver) {
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
    public void sendMsg(int connection, Message message) {
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

	Set<Integer> keyset = threadCollection.keySet();
	Iterator<Integer> i = keyset.iterator();

	while (i.hasNext()) {
	    tempqueue = threadCollection.get(i.next()).getMsg();
	    if (tempqueue != null)
		returnmap.put((Integer) i.next(), tempqueue);
	}

	return returnmap;
    }

    /**
     * A query of thread statuses. (connection, data transfer, etc.)
     * 
     * @return An integer-integer map of statuses. First integer is connection
     *         id and the second is connection status.
     */
    public Map<Integer, Integer> getStatus() {
	return null;

    }

    /**
     * A request to start handle the given socket
     * 
     * @param socket
     *            A new socket.
     */
    public synchronized void gimmeThread(Socket socket) {
	CommThread ct = new ServerThread(id, socket, messageReceiver);
	threadCollection.put(conncounter, ct);
	conncounter++;
    }

}
