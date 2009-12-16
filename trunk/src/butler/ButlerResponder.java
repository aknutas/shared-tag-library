package butler;

import java.util.Collection;

import data.RemoteResponder;
import data.messages.RemoteMessage;

/**
 * The ButlerResponder class extends the RemoteResponder and is used to respond
 * to RemoteMessages from RemoteLibraryButler objects over a network. It
 * generates its response based on the HeadButler object it contains.
 * 
 * @author sjpurol
 */
public class ButlerResponder extends RemoteResponder {

    /**
     * The HeadButler to base responses off of.
     */
    private HeadButler butler;

    /**
     * Creates a new instance of ButlerResponder that passes information from
     * butler over the network.
     * 
     * @param butler
     *            the butler that this responder represents.
     * @throws IllegalArgumentException
     *             if butler is null.
     */
    public ButlerResponder(HeadButler butler) throws IllegalArgumentException {
	super();
	if (null == butler)
	    throw new IllegalArgumentException("butler cannot be null.");

	this.butler = butler;
    }

    @Override
    public RemoteMessage onRemoteMessage(RemoteMessage message)
	    throws NullPointerException, IllegalArgumentException {

	if (null == message)
	    throw new NullPointerException("message cannot be null");

	if (!(message instanceof ButlerMessage))
	    throw new IllegalArgumentException("illegal message type");

	switch (message.getMessageType()) {
	case ButlerMessage.MSG_INITIALIZE:
	    return this.handleInitializeMessage((ButlerMessage) message);
	default:
	    RemoteMessage error = new ButlerMessage(ButlerMessage.MSG_ERROR);
	    error.queueParameter("unknown message type");
	    return error;
	}

    }

    /**
     * Responds to a request to initialize a RemoteLibraryButler by queuing the
     * ButlerWeights of each VirtualLibraryButler within the HeadButler.
     * 
     * @param message
     *            the message to respond to
     * @return a response.
     */
    private RemoteMessage handleInitializeMessage(ButlerMessage message) {
	if (null == message)
	    throw new NullPointerException("message cannot be null");

	if (ButlerMessage.MSG_INITIALIZE != message.getMessageType())
	    throw new IllegalArgumentException("invalid message type");

	/* create response */
	ButlerMessage response = new ButlerMessage(ButlerMessage.MSG_INITIALIZE);
	response.queueParameter(new Integer(butler.getNumOfLocalButlers()));

	Collection<VirtualLibraryButler> allButlers = butler.getLocalButlers();

	for (VirtualLibraryButler vlb : allButlers)
	    response.queueParameter(vlb.getWeights());
	return response;
    }
}
