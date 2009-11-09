package data.messages;

public class BookshelfMessage extends RoutedMessage {

	private static final long serialVersionUID = 1L;
	
	/* message types */
	public static final int MSG_SIZE = 2;
	public static final int MSG_CONTAINS = 3;
	public static final int MSG_EMPTY = 4;
	public static final int MSG_GET = 5;
	public static final int MSG_SET = 6;
	public static final int MSG_PROPERTY_ITERATOR = 7;
	public static final int MSG_ITERATOR = 8;
	
	public BookshelfMessage(int messageType, int id) {
		super(messageType, id);
	}	

}
