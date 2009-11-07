package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import network.ClientThread;
import network.Communication;
import network.Definitions;
import network.messages.*;

/**
 * Class CommTestServer This is a test listener that initializes the ClientThread and works on its functionality.
 * 
 * @author Antti Knutas
 * 
 */

public class CommTestClient {

    // Variable definitions
    public static final int PORT = 12200;
    Socket s;
    ServerSocket ss;
    Communication comm;
    long time;
    ClientThread ct;
    long myid;

    public static void main(String[] args) {
	CommTestServer application = new CommTestServer();
	application.launch();
    }

    public CommTestClient() {
	time = System.currentTimeMillis();
	System.out.println("Initializing client");
	comm = new network.CommunicationImpl();
	Random random = new Random();
	myid = random.nextLong();
    }

    void launch() {
	Iterator<Message> i;
	List<Message> tempqueue;

	System.out.println("\nMain thread initialized in "
		+ (System.currentTimeMillis() - time) + " milliseconds.\n");

	try {
	    ss = new ServerSocket(PORT);
	} catch (IOException e) {
	    System.out.println("Could not listen on port: " + PORT);
	    System.exit(-1);
	}

	System.out.println("Listening for connections.");

	try {
	    s = ss.accept();
	    ct = new network.ClientThread(myid, s, null);
	    ct.start();
	    System.out.println("Accepted");
	} catch (IOException e) {
	    System.out.println("Accept failed: " + PORT);
	    System.exit(-1);
	}

	// A lot of stuff happens
	while (ct.getStatus() == Definitions.CONNECTED) {
	    tempqueue = ct.getMsg();

	    System.out.println("TS: Loopin'");

	    if (tempqueue != null) {
		i = tempqueue.iterator();
		System.out.println("Iteratin', size: " + tempqueue.size());

		while (i.hasNext()) {
		    Object tryout = i.next();
		    if (tryout.getClass().getName().equals(
			    network.messages.ChatMessage.class.getName())) {
			ChatMessage hello = (ChatMessage) tryout;
			System.out.println(hello.GetMessage());
		    } else {
			System.out
				.println("Unknown Foreign Object recieved. UFO ALERT:"
					+ i.next().getClass().getName());
		    }
		}
	    } else {
		System.out
			.println("Queue still null, read too soon. Retrying.");
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    try {
		Thread.sleep(50);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	System.out.println("Client closed connection.");
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

}
