package network.messages;

import java.io.Serializable;

/**
 * This is an abstract class that provides fields and methods that are common to
 * all network messages. By extending this class other message classes will be
 * visible as a Message, also.
 * 
 * @author Antti Knutas
 * 
 */
@SuppressWarnings("serial")
public abstract class Message implements Serializable {

    long msgID;
    long comID;
    long orgID;

    public synchronized long getMsgID() {
	return msgID;
    }

    public synchronized void setMsgID(long msgID) {
	this.msgID = msgID;
    }

    public synchronized long getComID() {
	return comID;
    }

    public synchronized void setComID(long comID) {
	this.comID = comID;
    }

    public long getOrgID() {
        return orgID;
    }

    public void setOrgID(long orgID) {
        this.orgID = orgID;
    }

}
