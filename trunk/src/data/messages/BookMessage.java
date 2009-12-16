package data.messages;

/**
 * The BookMessage class extends the RoutedMessage class and is used for all
 * messages between a BookResponder and RemoteBook object over a network.
 * 
 * @author Andrew Alm
 */
public class BookMessage extends RoutedMessage {

	private static final long serialVersionUID = -2627248649500987680L;
	
	/* message types */
	public static final int MSG_TAG = 2;
	public static final int MSG_UNTAG = 3;
	public static final int MSG_WEIGHT = 4;
	public static final int MSG_TAG_ITERATOR = 5;
	public static final int MSG_GET = 6;
	public static final int MSG_SET = 7;
	public static final int MSG_PROPERTY_ITERATOR = 8;
	public static final int MSG_INITIALIZE = 9;
	
	/**
	 * Creates a new BookMessage of the given type with the given id.
	 * 
	 * @param messageType the type of message to create
	 * @param id the routing id of this message
	 */
	public BookMessage(int messageType, int id) {
		super(messageType, id);
	}
	
}
