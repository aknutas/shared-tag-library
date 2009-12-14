package butler;

import data.RoutedResponder;
import data.messages.BookshelfMessage;
import data.messages.RemoteMessage;
import data.messages.RoutedMessage;

public class ButlerResponder extends RoutedResponder {

	private LibraryButlerInterface butler;

	public ButlerResponder(LibraryButlerInterface butler) throws IllegalArgumentException{
		super();
		if (null == butler)
			throw new IllegalArgumentException("butler cannot be null.");

		this.butler = butler;
	}

	@Override
	public RemoteMessage onRemoteMessage(RemoteMessage message) throws NullPointerException, IllegalArgumentException {
		return null;
	}

	@Override
	public RoutedMessage onRoutedMessage(RoutedMessage message) throws NullPointerException, IllegalArgumentException {

		if (null == message)
			throw new NullPointerException("message cannot be null");

		if ( !(message instanceof ButlerMessage))
			throw new IllegalArgumentException("illegal message type");

		switch(message.getMessageType()) {
		case ButlerMessage.MSG_INITIALIZE:
			return this.handleInitializeMessage((ButlerMessage)message);
		default:
			RoutedMessage error = new BookshelfMessage(BookshelfMessage.MSG_ERROR, this.getID());
		error.queueParameter("unknown message type");
		return error;
		}

	}

	private RoutedMessage handleInitializeMessage(ButlerMessage message) {
		if (null == message)
			throw new NullPointerException("message cannot be null");
		
		if (ButlerMessage.MSG_INITIALIZE != message.getMessageType())
			throw new IllegalArgumentException("invalid message type");
		
		/* create response */
		ButlerMessage response = new ButlerMessage(ButlerMessage.MSG_INITIALIZE, this.getID());
		response.queueParameter(butler.getWeights());
		return response;
	}
}
