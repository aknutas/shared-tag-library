package test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import network.Communication;
import network.messages.*;

public class Commtest {

    // Variable definitions
    public static final int PORT = 12200;
    Socket s;
    Communication comm;
    Object obj;
    long time;

    public static void main(String[] args) {
	Commtest application = new Commtest();
	application.launch();
    }

    Commtest() {
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
		obj = comm.Receive(s);
	    } catch (IOException e1) {
		System.out.println(e1);
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }

	    if (obj != null) {
		if (obj.getClass().getName().equals(
			network.messages.ChatMessage.class.getName())) {
		    System.out.println(obj.getClass().getName());
		} else {
		    System.out
			    .println("Unknown Foreign Object recieved. UFO ALERT:"
				    + obj.getClass().getName());
		}
	    } else {
		System.out
			.println("Socket still null, read too soon. Retrying.");
	    }

	    try {
		Thread.sleep(1000);
	    } catch (InterruptedException e) {
		e.printStackTrace();
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
