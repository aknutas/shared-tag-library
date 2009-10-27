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

    private final static int SO_TIMEOUT = 50;

    public synchronized Object Receive(Socket _socket) throws IOException {

	try {
	    _socket.setSoTimeout(SO_TIMEOUT);

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
	}
	/**
	 * TODO A dirty bugfix.. don't know how to handle it better yet.
	 */
	catch (StreamCorruptedException e) {
	    throw new IOException(e.getMessage());
	}
    }

    public synchronized void Send(Socket _socket, Object _data) throws IOException {

	ObjectOutputStream sendstream;
	sendstream = new ObjectOutputStream(_socket.getOutputStream());
	sendstream.writeObject(_data);
	sendstream.flush();
	// sendstream.close();
    }

}
