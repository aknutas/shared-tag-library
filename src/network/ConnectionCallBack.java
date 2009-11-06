package network;

import java.net.Socket;

/**
 * Interface ConnectionCallBack This interface is for those classes that handle
 * a socket after a successful connection.
 * 
 * @author Antti Knutas
 * 
 */
public interface ConnectionCallBack {

    /**
     * A request to start handle the given socket
     * 
     * @param socket
     *            A new socket.
     */
    public void gimmeThread(Socket socket);

}
