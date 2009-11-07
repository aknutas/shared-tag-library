package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import data.messages.DataMessage;

import network.messages.Message;
import network.messages.ReplyMessage;
import network.Definitions;

/**
 * Class ClientThread A Thread for connecting to other program instances.
 * 
 * @author Antti Knutas
 * 
 */
public class ClientThread extends CommThread {

    public ClientThread (long myid, Socket s, ServerMessageReciever messageReceiver) {
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
	long tempcompid;
	long tempmsgid;
	ClientMessageReceiver cmr;
	DataMessage tempdm;
	ReplyMessage rm;
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
	    if (obj.getClass().getName().equals(
		    data.messages.DataMessage.class.getName())) {
		tempdm = (data.messages.DataMessage) obj;
		tempcompid = tempdm.getComID();
		tempmsgid = tempdm.getMsgID();
		DataMessage replydm = (DataMessage) messageReceiver
			.onMessageRecive(tempdm);

		rm = new ReplyMessage(tempdm, tempcompid, tempmsgid);
		try {
		    comm.Send(s, rm);
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    } else if (obj.getClass().getName().equals(
		    network.messages.ReplyMessage.class.getName())) {
		rm = (ReplyMessage) obj;
		tempmsgid = rm.getMsgID();
		tempdm = rm.getDatamessage();

		cmr = replymap.get(tempmsgid);
		replymap.remove(tempmsgid);
		cmr.onMessageRecive(tempdm);
	    } else {
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
