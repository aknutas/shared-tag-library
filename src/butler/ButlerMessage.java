package butler;

import data.messages.RemoteMessage;

/**
 * The ButlerMessage class extends RemoteMessage and allows a HeadButler from a
 * remote connection to be re-created locally.
 * 
 * @author sjpurol
 * 
 */
public class ButlerMessage extends RemoteMessage {

	private static final long serialVersionUID = 3618806708211736297L;

	// message types
	/**
	 * the int ID of an initialize message.
	 */
	public static final int MSG_INITIALIZE = 2;

	/**
	 * Creates a new ButlerMessage with the given messageType
	 * 
	 * @param messageType
	 */
	public ButlerMessage(int messageType) {
		super(messageType);
	}
}
