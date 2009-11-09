package data.messages;

public class BookshelfMessage extends RoutedMessage {

	/* message types */
	public static final int MSG_SIZE = 2;
	public static final int MSG_CONTAINS = 3;
	public static final int MSG_EMPTY = 4;
	public static final int MSG_PROPERTY = 5;
	
	public BookshelfMessage(int messageType, int id) {
		super(messageType, id);
	}	

}
