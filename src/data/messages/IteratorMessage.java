package data.messages;

/**
 * The IteratorMessage extends the DataMessage class and is used to pass allows
 * for iterator objects to retrieve remote objects.
 * 
 * @author Andrew Alm
 */
public class IteratorMessage extends RoutedMessage {

    private static final long serialVersionUID = 4603374207926067891L;

    public static final int MSG_MORE = 2;

    /**
     * Creates a new IteratorMessage object with the given message type and id.
     * 
     * @param messageType
     *            the message type
     * @param id
     *            the iterator id
     */
    public IteratorMessage(int messageType, int id) {
	super(messageType, id);
    }

}
