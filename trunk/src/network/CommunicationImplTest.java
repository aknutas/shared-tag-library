package network;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import network.messages.ChatMessage;
import network.messages.Message;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CommunicationImplTest {

    @Test
    public void testSend() {
	Socket s = null;
	Communication comm = new CommunicationImpl();
	
	Receiver rec = new Receiver();
	rec.start();
	
	try {
	    s = new Socket("localhost", Definitions.PORT);
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	ChatMessage testmessage = new ChatMessage("HULABALOO");
	
	try {
	    comm.Send(s, testmessage);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }

    public class Sender extends Thread {
	public Socket s;

	public void run() {

	}

    }

    public class Receiver extends Thread {
	public Socket s;
	public ServerSocket ss;
	Communication comm;
	Object obj;
	
	Receiver()
	{
	    comm = new CommunicationImpl();
	}

	public void run() {
	    try {
		ss = new ServerSocket(Definitions.PORT);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    try {
		s = ss.accept();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    try {
		obj = comm.Receive(s);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

}
