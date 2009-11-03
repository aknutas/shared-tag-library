package network.messages;

import java.io.Serializable;

/**
 * A reply object for library and other data transmissions that expect direct
 * reply message.
 * 
 * @author Antti Knutas
 * 
 */

public class Reply extends Message implements Serializable {

    private static final long serialVersionUID = -4533149474846448423L;
    
    //Errorcodes defined in network.CommThread and network.ControlImpl
    int errorcode;
    int contenttype;
    boolean isready;

    Object content;

    public synchronized int getErrorcode() {
	return errorcode;
    }

    public synchronized void setErrorcode(int errorcode) {
	this.errorcode = errorcode;
    }

    public synchronized int getContenttype() {
	return contenttype;
    }

    public synchronized void setContenttype(int contenttype) {
	this.contenttype = contenttype;
    }

    public synchronized boolean getIsready() {
	return isready;
    }

    public synchronized void setIsready(boolean isready) {
	this.isready = isready;
    }

    public synchronized Object getContent() {
	return content;
    }

    public synchronized void setContent(Object content) {
	this.content = content;
    }

    public Reply() {
	this.isready = false;
    }

}
