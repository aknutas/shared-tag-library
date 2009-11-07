package network.messages;

import data.messages.DataMessage;

public class ReplyMessage extends Message {

    private static final long serialVersionUID = 1041061591404140362L;
    
    DataMessage datamessage;
    
    private ReplyMessage()
    {
	
    }
    
    public ReplyMessage(DataMessage datamessage, long comID, long msgID)
    {
	this.datamessage=datamessage;
	this.comID=comID;
	this.msgID=msgID;
    }

    public synchronized DataMessage getDatamessage() {
	return datamessage;
    }

    public synchronized void setDatamessage(DataMessage datamessage) {
	this.datamessage = datamessage;
    }

}
