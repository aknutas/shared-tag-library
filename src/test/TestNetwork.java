package test;

import data.*;
import network.*;
import network.messages.*;
import java.util.*;

/**
 * The TestNetwork object is used to simulate a network without the use of
 * sockets. This allows for the Library/Bookshelf/Book remote objects to be
 * tested without having to run two program instances (and have them connect to
 * each other, etc). This is good for testing message passing logic without the
 * added complexity of networking.
 * 
 * @author Andrew Alm
 */
public class TestNetwork {

    private Map<String, Library> clients;

    /**
     * Create a new TestNetwork object with no links.
     */
    public TestNetwork() {
	this.clients = new HashMap<String, Library>();
    }

    public void addLibrary(String hostname, Library library)
	    throws NullPointerException {
	if ((null == hostname) || (null == library))
	    throw new NullPointerException("hostname or library cannot be null");

	this.clients.put(hostname, library);
    }

    public Library removeLibrary(String hostname) throws NullPointerException {
	if (null == hostname)
	    throw new NullPointerException("hostname cannot be null");

	return this.clients.remove(hostname);
    }

    public Library getLibrary(String hostname) throws NullPointerException {
	if (null == hostname)
	    throw new NullPointerException("hostname cannot be null");

	return this.clients.get(hostname);
    }

}
