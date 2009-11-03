package network;

import java.util.List;
import java.util.Map;

import network.messages.Message;
import network.messages.Reply;

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
     */
    public int connect(String address);

    /**
     * A command to disconnect a certain client or server connection.
     * 
     * @return int
     * @param connection
     *            The connection ID to be disconnected.
     */
    public int disconnect(int connection);

    /**
     * A command to send a data object to the specified connection, expecting a
     * reply to the reply object.
     * 
     * @return Reply The reply object.
     * @param connection
     *            The connection ID.
     * @param message
     *            The message to be sent.
     * @see Reply
     */
    public Reply sendMsgGetReply(int connection, Message message);

    /**
     * A command to send a data object to the specified connection, with no
     * direct reply expected.
     * 
     * @param connection
     *            The connection ID.
     * @param message
     *            The message to be sent.
     */
    public void sendMsgNoReply(int connection, Message message);

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

}
