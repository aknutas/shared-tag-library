package data.messages;

/**
 * A RoutedMessage extends a RemoteMessage and is used for messages which
 * require a id to process.
 * 
 * @author Andrew Alm
 */
public class RoutedMessage extends RemoteMessage {

    private static final long serialVersionUID = -5611699267122408137L;

    private final int id;

    /**
     * Creates a new RoutedMessage of the given message type with the given ID.
     * 
     * @param messageType
     *            the message type to use
     * @param id
     *            the id to use
     */
    public RoutedMessage(int messageType, int id) {
	super(messageType);

	this.id = id;
    }

    /**
     * Get the id of the RoutedMessage.
     * 
     * @return the id of this message
     */
    public int getID() {
	return this.id;
    }

}
