package data.messages;

import java.io.*;
import java.util.*;

/**
 * The TrackedMessage is an abstract class which extends the Message object. It
 * used to keep statistics about each type of message in the system.
 * TrackedMessages can be used simply be having a message class extend
 * TrackedMessage, a tracked message will not affect the size or behavior of a
 * message.
 * 
 * @author Andrew Alm
 */
public abstract class TrackedMessage extends RemoteMessage {

    private static final long serialVersionUID = -5462976729539985299L;

    private transient static Map<Class<? extends TrackedMessage>, Map<Integer, Integer>> messagesSent;
    private transient static Map<Class<? extends TrackedMessage>, Map<Integer, Integer>> messagesRecieved;
    private transient static Map<Class<? extends TrackedMessage>, Map<Integer, Integer>> bytesSent;
    private transient static Map<Class<? extends TrackedMessage>, Map<Integer, Integer>> bytesRecieved;

    static {
	TrackedMessage.messagesSent = new HashMap<Class<? extends TrackedMessage>, Map<Integer, Integer>>();
	TrackedMessage.messagesRecieved = new HashMap<Class<? extends TrackedMessage>, Map<Integer, Integer>>();
	TrackedMessage.bytesSent = new HashMap<Class<? extends TrackedMessage>, Map<Integer, Integer>>();
	TrackedMessage.bytesRecieved = new HashMap<Class<? extends TrackedMessage>, Map<Integer, Integer>>();
    }

    /**
     * Creates a new TrackedMessage with the given message type.
     * 
     * @param messageType
     *            the type of this message
     */
    public TrackedMessage(int messageType) {
	super(messageType);
    }

    /**
     * Gets the total number of messages sent thus far.
     * 
     * @return the number of messages sent
     */
    public static int countSentMessages() {
	int sum = 0;

	for (Class<? extends TrackedMessage> messageClassType : TrackedMessage.messagesSent
		.keySet())
	    sum += TrackedMessage.countSentMessages(messageClassType);

	return sum;
    }

    /**
     * Gets the total number of messages of the given message class type that
     * have been sent.
     * 
     * @param messageClassType
     *            the class of the message to count.
     * 
     * @return the number of messages sent
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countSentMessages(
	    Class<? extends TrackedMessage> messageClassType)
	    throws NullPointerException {
	return TrackedMessage.countSentMessages(messageClassType, -1);
    }

    /**
     * Gets the total number of messages sent which are of the given class type
     * and message type.
     * 
     * @param messageClassType
     *            the class type of the message
     * @param messageType
     *            the message type
     * 
     * @return the number of message sent
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countSentMessages(
	    Class<? extends TrackedMessage> messageClassType, int messageType)
	    throws NullPointerException {
	if (null == messageClassType)
	    throw new NullPointerException("messageClassType cannot be null");

	return TrackedMessage.reduceMap(TrackedMessage.unmapType(
		TrackedMessage.messagesSent, messageClassType), messageType);
    }

    /**
     * Gets the total number of messages received thus far.
     * 
     * @return the number of messages sent
     */
    public static int countReceivedMessages() {
	int sum = 0;

	for (Class<? extends TrackedMessage> messageClassType : TrackedMessage.messagesSent
		.keySet())
	    sum += TrackedMessage.countReceivedMessages(messageClassType);

	return sum;
    }

    /**
     * Gets the total number of messages received of the message class type
     * given.
     * 
     * @param messageClassType
     *            the class of the message to count.
     * 
     * @return the number of messages sent
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countReceivedMessages(
	    Class<? extends TrackedMessage> messageClassType) {
	return TrackedMessage.countReceivedMessages(messageClassType, -1);
    }

    /**
     * Gets the total number of messages received which are of the given class
     * type and message type.
     * 
     * @param messageClassType
     *            the class type of the message
     * @param messageType
     *            the message type
     * 
     * @return the number of message sent
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countReceivedMessages(
	    Class<? extends TrackedMessage> messageClassType, int messageType) {
	if (null == messageClassType)
	    throw new NullPointerException("messageClassType cannot be null");

	return TrackedMessage
		.reduceMap(TrackedMessage.unmapType(
			TrackedMessage.messagesRecieved, messageClassType),
			messageType);
    }

    /**
     * Gets the total number of bytes sent thus far.
     * 
     * @return the number of bytes sent
     */
    public static int countSentBytes() {
	int sum = 0;

	for (Class<? extends TrackedMessage> messageClassType : TrackedMessage.messagesSent
		.keySet())
	    sum += TrackedMessage.countSentBytes(messageClassType);

	return sum;
    }

    /**
     * Gets the total number of bytes sent of the message class type given.
     * 
     * @param messageClassType
     *            the class type of the message to count.
     * 
     * @return the number of messages sent
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countSentBytes(
	    Class<? extends TrackedMessage> messageClassType) {
	return TrackedMessage.countSentBytes(messageClassType, -1);
    }

    /**
     * Gets the total number of bytes sent which are of the given class type and
     * message type.
     * 
     * @param messageClassType
     *            the class type of the message
     * @param messageType
     *            the message type
     * 
     * @return the number of message sent
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countSentBytes(
	    Class<? extends TrackedMessage> messageClassType, int messageType)
	    throws NullPointerException {
	if (null == messageClassType)
	    throw new NullPointerException("messageClassType cannot be null");

	return TrackedMessage.reduceMap(TrackedMessage.unmapType(
		TrackedMessage.bytesSent, messageClassType), messageType);
    }

    /**
     * Gets the total number of bytes received thus far.
     * 
     * @return the number of bytes received
     */
    public static int countReceivedBytes() {
	int sum = 0;

	for (Class<? extends TrackedMessage> messageClassType : TrackedMessage.messagesSent
		.keySet())
	    sum += TrackedMessage.countSentBytes(messageClassType);

	return sum;
    }

    /**
     * Gets the total number of bytes received of the message class type given.
     * 
     * @param messageClassType
     *            the class type of the message to count.
     * 
     * @return the number of messages received
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countRecievedBytes(
	    Class<? extends TrackedMessage> messageClassType) {
	return TrackedMessage.countReceivedBytes(messageClassType, -1);
    }

    /**
     * Gets the total number of bytes received which are of the given class type
     * and message type.
     * 
     * @param messageClassType
     *            the class type of the message
     * @param messageType
     *            the message type
     * 
     * @return the number of message received
     * 
     * @throws NullPointerException
     *             if the messageClassType given is null
     */
    public static int countReceivedBytes(
	    Class<? extends TrackedMessage> messageClassType, int messageType)
	    throws NullPointerException {
	if (null == messageClassType)
	    throw new NullPointerException("messageClassType cannot be null");

	return TrackedMessage.reduceMap(TrackedMessage.unmapType(
		TrackedMessage.bytesRecieved, messageClassType), messageType);
    }

    /**
     * This method is used to reduce a Map<Integer,Integer> down to a single int
     * value by addition. If the messageType given is not -1 then this returns
     * the value for the given messageType.
     * 
     * @param map
     *            the map to reduce
     * @param messageType
     *            the type of message to reduce on (or -1 to reduce the entire
     *            map
     * 
     * @return
     * 
     * @throws NullPointerException
     *             if the map given is null
     */
    private static int reduceMap(Map<Integer, Integer> map, int messageType)
	    throws NullPointerException {
	if (null == map)
	    throw new NullPointerException("map cannot be null");

	if (-1 != messageType) {
	    Integer sum = map.get(messageType);
	    if (null == sum)
		sum = 0;

	    return sum;
	}

	int sum = 0;
	for (Integer value : map.values())
	    sum += value;

	return sum;
    }

    /**
     * This method is used to record when a message is sent, it logs the
     * messageType and message size.
     */
    public void messageSent() {
	TrackedMessage.accumulateMap(TrackedMessage.unmapType(
		TrackedMessage.messagesSent, this.getClass()), this
		.getMessageType(), 1);
	TrackedMessage.accumulateMap(TrackedMessage.unmapType(
		TrackedMessage.bytesSent, this.getClass()), this
		.getMessageType(), TrackedMessage.objectSize(this));
    }

    /**
     * This method is used to record when a message is received, it logs the
     * messageType and message size.
     */
    public void messageReceived() {
	TrackedMessage.accumulateMap(TrackedMessage.unmapType(
		TrackedMessage.messagesRecieved, this.getClass()), this
		.getMessageType(), 1);
	TrackedMessage.accumulateMap(TrackedMessage.unmapType(
		TrackedMessage.bytesRecieved, this.getClass()), this
		.getMessageType(), TrackedMessage.objectSize(this));
    }

    /**
     * This method is used to retrieve the message type map associated wit the
     * given class type.
     * 
     * @param map
     *            the map to unmap from
     * @param classType
     *            the class type to unmap with
     * 
     * @return the map for the class type
     * 
     * @throws NullPointerException
     *             if the map or message given is null
     */
    private static Map<Integer, Integer> unmapType(
	    Map<Class<? extends TrackedMessage>, Map<Integer, Integer>> map,
	    Class<? extends TrackedMessage> classType)
	    throws NullPointerException {
	if ((null == map) || (null == classType))
	    throw new NullPointerException("map or message cannot be null");

	Map<Integer, Integer> dataMap = map.get(classType);
	if (null == dataMap) {
	    dataMap = new HashMap<Integer, Integer>();
	    map.put(classType, dataMap);
	}

	return dataMap;
    }

    /**
     * This method is used to accumulate new data into a map. It simply adds the
     * given accumulate value to the previous value in the map.
     * 
     * @param map
     *            the map to accumulate on
     * @param messageType
     *            the message type to accumulate
     * @param accumulate
     *            the amount to add
     * 
     * @throws NullPointerException
     *             if the map given is null
     */
    private static void accumulateMap(Map<Integer, Integer> map,
	    int messageType, int accumulate) throws NullPointerException {
	if (null == map)
	    throw new NullPointerException("map cannot be null");

	Integer value = map.get(messageType);
	if (null == value)
	    value = 0;

	map.put(messageType, value + accumulate);
    }

    /**
     * This method is used to determine the size of a Serializable object in
     * bytes.
     * 
     * @param object
     *            the object to get the size of
     * 
     * @return the size of the object
     * 
     * @throws NullPointerException
     *             if the object given is null
     */
    private static int objectSize(Serializable object)
	    throws NullPointerException {
	if (null == object)
	    throw new NullPointerException("object cannot be null");
	try {
	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    (new ObjectOutputStream(bytes)).writeObject(object);
	    return bytes.size();
	} catch (Exception eX) {
	    return 0;
	}
    }

}
