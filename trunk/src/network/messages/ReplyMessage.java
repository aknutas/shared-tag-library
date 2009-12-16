package network.messages;

import data.messages.RemoteMessage;

public class ReplyMessage extends Message {

    private static final long serialVersionUID = 1041061591404140362L;

    RemoteMessage datamessage;

    private ReplyMessage() {

    }

    public ReplyMessage(RemoteMessage datamessage, long comID, long msgID) {
	this.datamessage = datamessage;
	this.comID = comID;
	this.msgID = msgID;
    }

    public synchronized RemoteMessage getDatamessage() {
	return datamessage;
    }

    public synchronized void setDatamessage(RemoteMessage datamessage) {
	this.datamessage = datamessage;
    }

}
