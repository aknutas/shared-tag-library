package butler;

import java.util.Map;

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
	
	HeadButler butler;
	
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
	public RemoteLibraryButler(int connection, Control network, long timeout) throws NullPointerException, RemoteObjectException {
		super(connection, network, timeout);
				
		ButlerMessage message = new ButlerMessage(ButlerMessage.MSG_INITIALIZE);
		RemoteMessage response = this.send(message, timeout);
		int numberOfButlers = ((Integer)response.dequeParameter()).intValue();
		VirtualLibraryButler butOne = new VirtualLibraryButler((ButlerWeights)response.dequeParameter());
		butler = new HeadButler(butOne);
		
		for (int i = 0; i < numberOfButlers-1; ++i) {
			ButlerWeights bw = (ButlerWeights)response.dequeParameter();
			VirtualLibraryButler but = new VirtualLibraryButler(bw);
			butler.addButler(but);
		}
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
	public RemoteLibraryButler(int connection, Control network) throws NullPointerException, RemoteObjectException {
		this(connection, network, 5000);
	}

	@Override
	public Map.Entry<FlatShelf, Double> assemble(Book b) {return butler.remoteAssemble(b);}

	/**
	 * Returns the max number of shelfs within the HeadButler
	 */
	@Override
	public int countShelfs() {
		butler.reCountShelfs();
		return butler.maxNumOfShelfs;
	}
	
	/**
	 * Not valid for a RemoteLibraryButler. Returns null.
	 */
	@Override
	public String identifyShelf(int index) {return null;}

	/**
	 * Not valid for a RemoteLibraryButler. Returns null.
	 */
	@Override
	public double[] readyBook(Book b) {return null;}

	/**
	 * Not valid for a RemoteLibraryButler. Returns null.
	 */
	@Override
	public String getProperty(String name) {return null;}

	/**
	 * Not valid for a RemoteLibraryButler. Returns null.
	 */
	@Override
	public ButlerWeights getWeights() {return null;}

}
