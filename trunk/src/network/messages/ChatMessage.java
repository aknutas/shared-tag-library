package network.messages;

import java.io.Serializable;

/**
 * A simple chat message.
 * 
 * @author Antti Knutas
 * 
 */

public class ChatMessage extends Message implements Serializable {

    private static final long serialVersionUID = -5117029102342280006L;
    private String message;

    /**
     * Private default constructor. Do not create empty messages.
     */
    @SuppressWarnings("unused")
    private ChatMessage() {

    }

    public ChatMessage(String message) {
	if (message == null)
	    this.message = "Hi";
	else
	    this.message = message;
    }

    public String GetMessage() {
	return message;
    }
}
