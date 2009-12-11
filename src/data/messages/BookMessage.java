package data.messages;

public class BookMessage extends RoutedMessage {

	private static final long serialVersionUID = -2627248649500987680L;
	
	public static final int MSG_TAG = 2;
	public static final int MSG_UNTAG = 3;
	public static final int MSG_WEIGHT = 4;
	public static final int MSG_TAG_ITERATOR = 5;
	public static final int MSG_GET = 6;
	public static final int MSG_SET = 7;
	public static final int MSG_PROPERTY_ITERATOR = 8;
	public static final int MSG_INITIALIZE = 9;
	
	public BookMessage(int messageType, int id) {
		super(messageType, id);
	}
	
}
