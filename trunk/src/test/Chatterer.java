package test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import network.Communication;
import network.Definitions;
import network.messages.*;

/**
 * Class Chatterer This class is a test object sender for outgoing
 * communication.
 * 
 * @author Antti Knutas
 * 
 */

public class Chatterer {

    // Variable definitions
    public static final int PORT = 12200;
    Socket s;
    Communication comm;
    Object obj;
    long time;
    int i = 1;

    public static void main(String[] args) {
	Chatterer application = new Chatterer();
	application.launch();
    }

    public Chatterer() {
	time = System.currentTimeMillis();
	System.out.println("Initializing client");
	comm = new network.CommunicationImpl();
	try {
	    s = new Socket("127.0.0.1", Definitions.PORT);
	} catch (UnknownHostException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    void launch() {

	System.out.println("\nMain thread initialized in "
		+ (System.currentTimeMillis() - time) + " milliseconds.\n");

	while (!s.isClosed()) {
	    try {
		comm.Send(s, new ChatMessage("HILIRIMPSIS " + i));
		System.out.println("Sent" + i);
		i++;
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		break;
	    }
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
