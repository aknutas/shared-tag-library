package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import network.Communication;
import network.messages.*;

public class CommTestServer {

    // Variable definitions
    public static final int PORT = 12200;
    Socket s;
    ServerSocket ss;
    Communication comm;
    Object obj;
    long time;
    network.CommThread ct;
    long myid;

    public static void main(String[] args) {
	CommTestServer application = new CommTestServer();
	application.launch();
    }

    CommTestServer() {
	time = System.currentTimeMillis();
	System.out.println("Initializing client");
	comm = new network.CommunicationImpl();
	Random random = new Random();
	myid = random.nextLong();
    }

    void launch() {
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
	ct = new network.ServerThread(myid, s);
	} catch (IOException e) {
	    System.out.println("Accept failed: " + PORT);
	    System.exit(-1);
	}

	
	while (ct.getStatus() == 1) {
	    tempqueue = ct.getMsg();

	    if (tempqueue != null) {
		Iterator<Message> i = tempqueue.iterator();

		while (i.hasNext()) {
		    if (obj.getClass().getName().equals(
			    network.messages.ChatMessage.class.getName())) {
			System.out.println(obj.getClass().getName());
			ChatMessage hello = (ChatMessage) obj;
			System.out.println(hello.GetMessage());
		    } else {
			System.out
				.println("Unknown Foreign Object recieved. UFO ALERT:"
					+ obj.getClass().getName());
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
	}

	System.out.println("Server closed connection.");
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

}
