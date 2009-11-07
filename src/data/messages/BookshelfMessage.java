package data.messages;

public class BookshelfMessage extends RemoteMessage {

	public static final int MSG_HELLO = 0;
	public static final int MSG_PROPERTY = 1;
	
	private int id;
	
	public BookshelfMessage(int messageType, int id) {
		super(messageType);
		
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

}
