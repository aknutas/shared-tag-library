package controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import butler.LibraryButlerInterface;

import network.Control;
import data.Bookshelf;
import data.Library;
import data.RemoteLibrary;
import data.VirtualLibrary;

/**
 * The ConnectionMetadta class implements the Serializable interface. It is used
 * to store persistent and state data for a connection. This object is stored in
 * a Map with an alias, in order to promote easy access to connection
 * information by a user supplied alias.
 * 
 * @author Andrew Alm
 */
public class ConnectionMetadata implements Serializable {

	private static final long serialVersionUID = 1L;

	/* persistent variables */
	private final String hostname;
	private final String alias;

	/* transient variables (not saved at serialization time) */
	private transient boolean connected = false;
	private transient int connection = 0;
	private transient Library library = null;
	private transient Library ImportedLibrary = null;
	//private transient LibraryButlerInterface myButler=null;
	/**
	 * Creates a new ConnectionMetadata instance with the given alias and
	 * hostname
	 * 
	 * @param alias
	 *            the alias given to this connection
	 * @param hostname
	 *            the ip address or hostname of the connection
	 * 
	 * @throws NullPointerException
	 *             if the hostname or alias given are null
	 */
	public ConnectionMetadata(String alias, String hostname)
			throws NullPointerException {
		if (null == alias || null == hostname)
			throw new NullPointerException("hostname cannot be null");

		/* set persistent variables */
		this.alias = alias;
		this.hostname = hostname;
	}

	/**
	 * Gets the hostname name stored in this ConnectionMetadata instance.
	 * 
	 * @return the hostname String
	 */
	public String getHostname() {
		return this.hostname;
	}

	/**
	 * Gets the alias name stored in this ConnectionMetadata instance.
	 * 
	 * @return the alias String
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Gets the library stored in this ConnectionMetadata instance.
	 * 
	 * @return the library or null if not connected
	 */
	public Library getLibrary() {
		return this.library;
	}
	/**
	 * Gets the connection int stored in this ConnectionMetadata instance.
	 * 
	 * @return the library or null if not connected
	 */
	public int getConnectionId() {
		return this.connection;
	}
	/**
	 * Gets the Imported library stored in this ConnectionMetadata instance.
	 * 
	 * @return the library or null if not connected
	 */
	public Library getImportedLibrary() {
		return this.ImportedLibrary;
	}
	/**
	 * Determines whether this ConnectionMetadata object has an open connection.
	 * 
	 * @return if there is an open connection, else false
	 */
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Attempts to connect to the host contained in this ConnectionMetadata
	 * object, returning the result.
	 * 
	 * @param network
	 *            the network control object to connect through
	 * 
	 * @return true if connection was successful, else false
	 * 
	 * @throws NullPointerException
	 *             if the network given is null
	 */
	public boolean connect(Control network) throws NullPointerException {
		if (null == network)
			throw new NullPointerException("network cannot be null");

		try {
			this.connected = true;
			this.connection = network.connect(this.hostname);
			this.library = new RemoteLibrary(this.connection, network);
			this.ImportedLibrary = new VirtualLibrary();
		} catch (Exception ex) {
			this.connected = false;
			this.connection = 0;
			this.library = null;
		}

		return this.connected;
	}

	/**
	 * This method is used to disconnect a ConnectionMetadata object.
	 * 
	 * @param network
	 *            the network control object to disconnect through
	 * 
	 * @return true if disconnected, otherwise false
	 * 
	 * @throws NullPointerException
	 *             if the network given is null.
	 */
	public boolean disconnect(Control network) throws NullPointerException {
		if (null == network)
			throw new NullPointerException("network cannot be null");

		if (network.disconnect(this.connection)) {
			this.connected = false;
			this.connection = 0;
			this.library = null;
			this.ImportedLibrary = null;
		}

		return this.connected;
	}
}
