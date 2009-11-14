package network;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import data.messages.RemoteMessage;
import network.messages.*;

/**
 * Class ServerThread A class for listening to connections.
 * 
 * @author Antti Knutas
 * 
 */
public class ServerThread extends CommThread {

    public ServerThread(long myid, Socket s, ServerResponder sr) {
	messagequeue = new ArrayList<Message>();
	sendqueue = new ArrayList<Message>();
	replymap = new HashMap<Long, ClientResponder>();
	this.myid = myid;
	this.s = s;
	comm = new CommunicationImpl();
	// Debug
	// System.out.println("Initialized CommServerThread");
	super.setStatus(Definitions.CONNECTED);
	messageReceiver = sr;
    };

    @Override
    public void run() {
	long tempcompid;
	long tempmsgid;
	ClientResponder cmr;
	RemoteMessage tempdm;
	ReplyMessage rm;
	Object obj = null;

	run = true;
	// Debug
	// System.out.println("ServerThread: Runnin'");

	while (!s.isClosed() && run) {
	    try {
		obj = comm.Receive(s);
	    } catch (IOException e1) {
		System.out.println(e1);
	    }
	    if (obj != null) {
		// Debug
		/*
		System.out.println("ServerThread Got: "
			+ obj.getClass().getName()); */
		if (obj instanceof data.messages.RemoteMessage) {
		    tempdm = (data.messages.RemoteMessage) obj;
		    tempcompid = tempdm.getComID();
		    tempmsgid = tempdm.getMsgID();
		    RemoteMessage replydm = (RemoteMessage) messageReceiver
			    .onMessage(tempdm);

		    rm = new ReplyMessage(replydm, tempcompid, tempmsgid);
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
		    cmr.onMessage(tempdm);
		} else {
		    Message qo = (network.messages.Message) obj;
		    super.addQueue(qo);
		}
	    } else {
		try {
		    Thread.sleep(15);
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    try {
		Thread.sleep(5);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	super.setStatus(Definitions.DISCONNECTED);
	return;
    }
}
