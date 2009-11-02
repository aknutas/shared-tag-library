package network;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

import network.messages.Message;
import database.QueryBuilder;
import database.QueryBuilderImpl;

/**
 * Class CommThread This class provides common functionality for each of the communication thread classes.
 * 
 * @Author Antti Knutas
 */
public class CommThread extends Thread {
    
    //Thread statuses
    public static final int DISCONNECTED = 0;
    public static final int CONNECTED = 1;

    //Introducing variables
    protected int status;
    protected Socket s;
    protected Communication comm;
    protected boolean run;
    protected ArrayList<Message> messagequeue;
    protected ArrayList<Message> sendqueue;
    protected long myid;

    /**
     * Set the value of status
     * 
     * @param newVar
     *            the new value of status
     */
    public synchronized void setStatus(int newVar) {
	status = newVar;
    }

    /**
     * Get the value of status
     * 
     * @return the value of status
     */
    public synchronized int getStatus() {
	return status;
    }

    public synchronized void sendMsg(network.messages.Message message) {
	// TODO Implement send queue
	try {
	    comm.Send(s, message);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public synchronized List<Message> getMsg() {
	if (!messagequeue.isEmpty()) {
	    ArrayList<Message> returnlist = new ArrayList<Message>(messagequeue);
	    messagequeue.removeAll(returnlist);
	    return returnlist;
	} else
	    return null;
    }

    protected synchronized void addQueue(network.messages.Message message) {
	if (message != null)
	    messagequeue.add(message);
    }

    public void run() {
	// Empty on purpose
    }

    protected CommThread() {
	// Prevents empty default constructor
    }
    
    public void end()
    {
	run=false;
    }

}