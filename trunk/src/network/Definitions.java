package network;

import controller.ProgramProperties;

public class Definitions {
    
    static ProgramProperties pp;
    public static final int PORT;
    public static final int TIMEOUT;
    public static final int SO_TIMEOUT;
    
    static
    {
	pp = ProgramProperties.getInstance();
	PORT = (Integer) pp.getProperty("network::port");
	TIMEOUT = (Integer) pp.getProperty("network::timeout");
	SO_TIMEOUT = (Integer) pp.getProperty("network::so_timeout");
    }
    
    //Thread statuses
    public static final int DISCONNECTED = 10;
    public static final int CONNECTED = 11;

    //Configuration
//    public static final int PORT = 24600;
    //The connection timeout
//    public static final int TIMEOUT = 4;
    //Timeout during communication
//    public static final int SO_TIMEOUT = 45;
}
