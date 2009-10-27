package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import network.messages.*;


/**
 * Class ServerThread
 * @author Antti Knutas
 * 
 */
public class ServerThread extends CommThread {

  public ServerThread (long myid, Socket s) {
      messagequeue = new ArrayList<Message>();
      sendqueue = new ArrayList<Message>();
      this.myid = myid;
      this.s = s;
      comm = new CommunicationImpl();
  };
  
  @Override
    public void run() {
	super.setStatus(1);
	Object obj;
	run = true;

	while (!s.isClosed() && run) {
	    try {
		obj = comm.Receive(s);
		// TODO Helloworld hack
		addQueue((network.messages.Message) obj);
	    } catch (IOException e1) {
		System.out.println(e1);
		try {
		    Thread.sleep(100);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	super.setStatus(0);
	return;
    }
}
