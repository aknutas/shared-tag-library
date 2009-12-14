package butler;

import network.Control;
import data.Book;
import data.RemoteObject;
import data.RemoteObjectException;
import data.messages.RemoteMessage;

/**
 * The RemoteLibraryButler class is an extension of RemoteObject and an 
 * implementation of LibraryButlerInterface. It retrieves a ButlerWeights
 * object from the remote server and creates a new VirtualLibraryButler
 * from the ButlerWeights
 * 
 * @author sjpurol
 *
 */
public class RemoteLibraryButler extends RemoteObject implements LibraryButlerInterface {
	
	final int id;
	VirtualLibraryButler butler;
	
	/**
	 * Creates a new RemoteLibraryButler object.
	 * 
	 * @param connection the connection ID
	 * @param network the network controller
	 * @param timeout the timeout in ms
	 * @param id the ID of this
	 * @throws NullPointerException
	 * @throws RemoteObjectException
	 */
	public RemoteLibraryButler(int connection, Control network, long timeout, int id) throws NullPointerException, RemoteObjectException {
		super(connection, network, timeout);
		this.id = id;
		
		RemoteMessage message = new ButlerMessage(ButlerMessage.MSG_INITIALIZE, this.id);
		RemoteMessage response = this.send(message, timeout);
		
		ButlerWeights weights = (ButlerWeights)response.dequeParameter();
		butler = new VirtualLibraryButler(weights);
		
	}

	/**
	 * Creates a new RemoteLibraryButler object.
	 * 
	 * @param connection the connection ID
	 * @param network the network controller
	 * @param id the ID of this
	 * @throws NullPointerException
	 * @throws RemoteObjectException
	 */
	public RemoteLibraryButler(int connection, Control network, int id) throws NullPointerException, RemoteObjectException {
		super(connection, network);
		this.id = id;
		
		RemoteMessage message = new ButlerMessage(ButlerMessage.MSG_INITIALIZE, this.id);
		RemoteMessage response = this.send(message);

		ButlerWeights weights = (ButlerWeights)response.dequeParameter();
		butler = new VirtualLibraryButler(weights);
	}

	@Override
	public int compareTo(Book b) {return butler.compareTo(b);}

	@Override
	public double[] assemble(Book b) {return butler.assemble(b);}

	@Override
	public int countShelfs() {return butler.countShelfs();}

	@Override
	public String identifyShelf(int index) {return butler.identifyShelf(index);}

	@Override
	public double[] readyBook(Book b) {return butler.readyBook(b);}

	@Override
	public String getProperty(String name) {return butler.getProperty(name);}

	@Override
	public ButlerWeights getWeights() {return butler.getWeights();}

}
