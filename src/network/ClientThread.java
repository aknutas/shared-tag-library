package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import network.messages.Message;
import network.Definitions;

/**
 * Class ClientThread A Thread for connecting to other program instances.
 * 
 * @author Antti Knutas
 * 
 */
public class ClientThread extends CommThread {

    public ClientThread (long myid, Socket s) {
        messagequeue = new ArrayList<Message>();
        sendqueue = new ArrayList<Message>();
        this.myid = myid;
        this.s = s;
        comm = new CommunicationImpl();
	// Debug
	System.out.println("Initialized CommClientThread");
	super.setStatus(Definitions.CONNECTED);
    };
    
    @Override
    public void run() {
	Object obj = null;
	run = true;
	// Debug
	System.out.println("Thread: Runnin'");

	while (!s.isClosed() && run) {
	    try {
		obj = comm.Receive(s);
	    } catch (IOException e1) {
		System.out.println(e1);
	    }
	    if (obj != null)
	    {
		System.out.println("Thread: Got: " + obj.getClass().getName());
		super.addQueue((network.messages.Message) obj);
	    }
	    try {
		Thread.sleep(25);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	super.setStatus(Definitions.DISCONNECTED);
	return;
    }
    
  }
