package test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import network.Communication;
import network.messages.*;

public class Chatterer {

    // Variable definitions
    public static final int PORT = 12200;
    Socket s;
    Communication comm;
    Object obj;
    long time;

    public static void main(String[] args) {
	Chatterer application = new Chatterer();
	application.launch();
    }

    Chatterer() {
	time = System.currentTimeMillis();
	System.out.println("Initializing client");
	comm = new network.CommunicationImpl();
	try {
	    s = new Socket("127.0.0.1", PORT);
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
		comm.Send(s, new ChatMessage("HILIRIMPSIS"));
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

}
