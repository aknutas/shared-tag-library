package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * This class allows the serialization and transfer of objects through sockets.
 * 
 * @author Antti Knutas
 * 
 */

public class CommunicationImpl implements Communication {

    /**
     * Tries to get a message from given socket, but with 10ms timeout. After
     * that timeout it returns null, otherwise an object that is received.
     * 
     * @param _socket
     *            Socket to be used
     * @return Object that is received, null if timeout happened (or a deformed
     *         message)
     * @throws IOException
     *             When there is problems with the connection
     */
    public synchronized Object Receive(Socket _socket) throws IOException {
	try {
	    _socket.setSoTimeout(Definitions.SO_TIMEOUT);

	    try {
		if (_socket.getInputStream().available() < 10)
		    return null;
	    } catch (IOException ioe) {
		return null;
	    }

	    ObjectInputStream recvstream = new ObjectInputStream(_socket
		    .getInputStream());
	    Object obj = recvstream.readObject();

	    // recvstream.close();
	    return obj;

	} catch (SocketTimeoutException e) {
	    return null;
	} catch (ClassNotFoundException e) {
	    return null;
	} catch (StreamCorruptedException e) {
	    throw new IOException(e.getMessage());
	}
    }

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
    public synchronized void Send(Socket _socket, Object _data)
	    throws IOException {

	ObjectOutputStream sendstream;
	sendstream = new ObjectOutputStream(_socket.getOutputStream());
	sendstream.writeObject(_data);
	sendstream.flush();
	// sendstream.close();
    }

}
