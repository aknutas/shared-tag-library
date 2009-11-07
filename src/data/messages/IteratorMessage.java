package data.messages;

/**
 * The IteratorMessage extends the DataMessage class and is used to pass
 * allows for iterator objects to retrieve remote objects.
 *  
 * @author Andrew Alm
 */
public class IteratorMessage extends DataMessage {

	/**
	 * Creates a new IteratorMessage object with the given message type.
	 * 
	 * @param messageType the message type
	 */
	public IteratorMessage(int messageType) {
		super(messageType);
	}

}
