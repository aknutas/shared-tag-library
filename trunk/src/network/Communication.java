package network;

import java.io.IOException;
import java.net.Socket;

/**
 * This class allows the serialization and transfer of objects through sockets.
 * 
 * @author Antti Knutas
 * 
 */

public interface Communication {

    /**
     * Sends the given data into the socket that's given as a parameter.
     * 
     * @param _socket
     *            Socket to be used
     * @param _data
     *            Data to be sent
     * @throws IOException
     *             When there is problem with the send or the socket
     *             communication
     */
    void Send(Socket _socket, Object _data) throws IOException;

    /**
     * Tries to get a message from given socket, but with 10ms timeout. After
     * that timeout it returns null, otherwise an object that is received.
     * 
     * @param _socket
     *            Socket to be used
     * @return Object that is received, null if timeout happened (or errorrous
     *         message)
     * @throws IOException
     *             When there is problems with the connection
     */
    Object Receive(Socket _socket) throws IOException;

}
