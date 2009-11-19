package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class ConnectionListener Listens for incoming connections and does a callback
 * for new server thread when necessary.
 * 
 * @author Antti Knutas
 * 
 */
public class ConnectionListener extends Thread {

    ConnectionCallBack ccb;
    int status;
    ServerSocket ss;
    Socket s;

    ConnectionListener(ConnectionCallBack ccb) {
	this.ccb = ccb;
	status = Definitions.CONNECTED;
    }
    
    public void shutdown()
    {
	status = Definitions.DISCONNECTED;
    }

    public void run() {
	try {
	    ss = new ServerSocket(Definitions.PORT);
	    System.out.println("Listening for connections.");
	} catch (IOException e) {
	    System.out.println("Could not listen on port: " + Definitions.PORT);
	    status = Definitions.DISCONNECTED;
	}

	while (status == Definitions.CONNECTED && !ss.isClosed()) {
	    try {
		s = ss.accept();
		s.setSoTimeout(Definitions.TIMEOUT);
		if (status == Definitions.DISCONNECTED) {
		    s.close();
		    break;
		}
		ccb.gimmeThread(s);
	    } catch (IOException e) {
		System.out.println("Accept failed: " + Definitions.PORT);
	    }
	}
    }
}
