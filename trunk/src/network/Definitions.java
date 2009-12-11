package network;

import controller.ProgramProperties;

/**
 * This class loads up changeable settings, and sets varying static definitions
 * instead of magic numbers.
 * 
 * @author Antti Knutas
 * 
 */
public class Definitions {

    // Reference to the global ProgramProperties
    static ProgramProperties pp;
    // Listening and connection port
    public static final int PORT;
    // Connection timeout
    public static final int TIMEOUT;
    // Socket timeout
    public static final int SO_TIMEOUT;

    static {
	pp = ProgramProperties.getInstance();
	PORT = (Integer) pp.getProperty("network::port");
	TIMEOUT = (Integer) pp.getProperty("network::timeout");
	SO_TIMEOUT = (Integer) pp.getProperty("network::so_timeout");
    }

    // Thread statuses
    public static final int DISCONNECTED = 10;
    public static final int CONNECTED = 11;
    public static final int LOOP_OUT = 12;
    public static final int LOOP_IN = 13;

}
