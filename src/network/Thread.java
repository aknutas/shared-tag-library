package network;

import java.net.Socket;
import java.util.*;
import database.QueryBuilder;
import database.QueryBuilderImpl;

/**
 * Class Thread
 */
public class Thread {

    private int status;
    private Socket s;
    private Communication comm = new CommunicationImpl();
    private QueryBuilder qb = new QueryBuilderImpl();

    public Thread() {
    };

    /**
     * Set the value of status
     * 
     * @param newVar
     *            the new value of status
     */
    private void setStatus(int newVar) {
	status = newVar;
    }

    /**
     * Get the value of status
     * 
     * @return the value of status
     */
    private int getStatus() {
	return status;
    }

    public void sendMsg() {
    }

    public void getMsg() {
    }

}
