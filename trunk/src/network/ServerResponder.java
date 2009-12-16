package network;

import network.messages.*;

/**
 * The ServerMessageReceiver interface is used to allow client objects to
 * receive response messages from a remote client. ServerMessageReciever objects
 * are expected to respond to a Message.
 * 
 * @author Andrew Alm
 */
public interface ServerResponder {

    /**
     * This method will be fired when a Message for this given object is
     * received. This method should return a Message object to send to the
     * client.
     * 
     * @param message
     *            the Message which was received
     * 
     * @return the Message to send in response.
     * 
     * @throws IllegalArgumentException
     *             if the message cannot be processed
     * @throws NullPointerException
     *             if the message given is null
     */
    public Message onMessage(Message message);

    /**
     * This method is called when a client disconnects from the server.
     * 
     * @param connection
     *            the connection id that disconnected
     */
    public void onDisconnect(int connection);

    /**
     * This method is called when a loop is detected in the network path.
     * 
     * @param connection
     *            the connection id where the loop occurs
     */
    public void onLoop(int connection);

}