package network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import network.messages.Message;

/**
 * Interface Control This class allows for control and polling of the networking
 * threads.
 * 
 * @author Antti Knutas
 * 
 */
public interface Control {

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
    public int connect(String address) throws UnknownHostException,
	    ConnectException, IOException;

    /**
     * A command to disconnect a certain client or server connection.
     * 
     * @return boolean Success status.
     * @param connection
     *            The connection ID to be disconnected.
     */
    public boolean disconnect(int connection);

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
    public void sendLibraryMsg(int connection, Message message,
	    ClientResponder receiver);

    /**
     * A command to send a data object to the specified connection, with no
     * direct reply expected.
     * 
     * @param connection
     *            The connection ID.
     * @param message
     *            The message to be sent.
     */
    public void sendMsg(int connection, Message message);

    /**
     * A query of incoming messages. (chats, disconnections, etc.)
     * 
     * @return A map that contains a list of all incoming messages. Sorted by
     *         connection id.
     */
    public Map<Integer, List<Message>> whatsUp();

    /**
     * A query of thread statuses. (connection, data transfer, etc.)
     * 
     * @return An integer-integer map of statuses. First integer is connection
     *         id and the second is connection status.
     */
    public Map<Integer, Integer> getStatus();

    /**
     * A request to start shutdown routines regarding networking. This stops
     * connection listening, and starts to disconnect threads.
     */
    public void shutDown();

    /**
     * The method is used to register objects that need to be notified when the
     * connection this specific ID gets shut down.
     * 
     * @param connId
     *            The connection id.
     * @param connUser
     *            The object to be registered for listening.
     * @return Returns the connection id. Or 0 in case of failure.
     */
    public int registerAnomalyListener(int connId, ClientResponder connUser);

}
