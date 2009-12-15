package butler;

import data.messages.RemoteMessage;

public class ButlerMessage extends RemoteMessage {
	
	private static final long serialVersionUID = 3618806708211736297L;
	
	// message types
	public static final int MSG_INITIALIZE = 2;
	
	public ButlerMessage(int messageType) {
		super(messageType);
	}
}
