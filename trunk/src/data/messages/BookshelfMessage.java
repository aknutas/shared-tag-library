package data.messages;

/**
 * The BookshelfMessage class extends the RoutedMessage class and is used for
 * all messages between a BookshelfResponder and RemoteBookShelf object over a
 * network.
 * 
 * @author Andrew Alm
 */
public class BookshelfMessage extends RoutedMessage {

    private static final long serialVersionUID = 4554402322430170431L;

    /* message types */
    public static final int MSG_SIZE = 2;
    public static final int MSG_CONTAINS = 3;
    public static final int MSG_EMPTY = 4;
    public static final int MSG_GET = 5;
    public static final int MSG_SET = 6;
    public static final int MSG_PROPERTY_ITERATOR = 7;
    public static final int MSG_ITERATOR = 8;

    /**
     * Creates a new BookshelfMessage of the given type with the given id.
     * 
     * @param messageType
     *            the type of message to create
     * @param id
     *            the routing id of this message
     */
    public BookshelfMessage(int messageType, int id) {
	super(messageType, id);
    }

}
