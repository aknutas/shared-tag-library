package butler;

import data.messages.RoutedMessage;

public class ButlerMessage extends RoutedMessage {
	
	private static final long serialVersionUID = 3618806708211736297L;
	
	// message types
	public static final int MSG_INITIALIZE = 2;
	
	public ButlerMessage(int messageType, int id) {
		super(messageType, id);
	}
}
