package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import network.messages.ChatMessage;
import org.junit.Test;

public class CommunicationImplTest {

    @Test
    public void testSend() {
	Socket s = null;
	Communication comm = new CommunicationImpl();

	Receiver rec = new Receiver();
	rec.start();

	// Waiting for the thread to initialize
	long t0, t1;
	t0 = System.currentTimeMillis();
	do {
	    t1 = System.currentTimeMillis();
	} while (t1 - t0 < 100);

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

	Object comparison = Receiver.obj;
	assert (testmessage.equals(comparison));
	try {
	    rec.join();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Test
    public void testReceive() {
	Socket s = null;
	ServerSocket ss = null;
	Communication comm = new CommunicationImpl();
	Object obj = null;

	try {
	    ss = new ServerSocket(Definitions.PORT);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Sender sender = new Sender();
	sender.start();
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

	Object comparison = Receiver.obj;
	assert (obj.equals(comparison));
    }

    public static class Sender extends Thread {
	public Socket s;
	Communication comm;
	static ChatMessage testmessage;

	public void run() {
	    // Waiting for the thread to initialize
	    long t0, t1;
	    t0 = System.currentTimeMillis();
	    do {
		t1 = System.currentTimeMillis();
	    } while (t1 - t0 < 100);

	    try {
		s = new Socket("localhost", Definitions.PORT);
	    } catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    testmessage = new ChatMessage("HULABALOO");
	    Communication comm = new CommunicationImpl();

	    try {
		comm.Send(s, testmessage);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

    public static class Receiver extends Thread {
	public Socket s;
	public ServerSocket ss;
	Communication comm;
	public static Object obj;

	Receiver() {
	    comm = new CommunicationImpl();
	}

	public Object getTest() {
	    return obj;
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
	    try {
		ss.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }

}
