package test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;

import data.*;
import data.messages.*;
import database.*;
import network.*;
import network.messages.*;

public class RemoteLibraryTest {
	
	public static void main(String []args) throws UnknownHostException, IOException {
		Control network = new TestNetworkControl();
		Library library = new VirtualLibrary();
		
		/* a bookshelf about programming */
		Bookshelf shelf1 = new VirtualBookshelf();
		shelf1.insert(new VirtualBook("Parallel Programming", "Smith"));
		shelf1.insert(new VirtualBook("Serial Programming", "Smith"));
		shelf1.insert(new VirtualBook("Java Programming", "Jones"));
		shelf1.insert(new VirtualBook("Discrete Structures", "Jones"));
		shelf1.insert(new VirtualBook("Linux Programming", "Torvalds"));
		shelf1.insert(new VirtualBook("Parallel Structures", "Stevenson"));
		library.addBookshelf(shelf1);
		
		/* a bookshelf of classics */
		Bookshelf shelf2 = new VirtualBookshelf();
		shelf2.insert(new VirtualBook("Old Man and the Sea", "Hemingway"));
		shelf2.insert(new VirtualBook("The Great Gatsby", "Fitzgerlard"));
		shelf2.insert(new VirtualBook("Macbeth", "Shakesphere"));
		shelf2.insert(new VirtualBook("Catcher in the Rye", "Salinger"));
		shelf2.insert(new VirtualBook("Crime and Punishment", "Dostoyevsky"));
		library.addBookshelf(shelf2);
		
		/* add library to network */
		((TestNetwork)network).addLibrary("andrew", library);
		
		/* connect */
		int connection = network.connect("andrew");
		Library remoteLibrary = null;
		
		try {
			remoteLibrary = new RemoteLibrary(connection, network);
		}
		catch(RemoteObjectException ex) {
			System.out.println("timeoutdf");
			return;
		}
		
		Bookshelf remoteShelf = remoteLibrary.getMasterShelf();
		Iterator<Bookshelf> it = remoteLibrary.iterator();
		for(Bookshelf shelf : remoteLibrary) {
			System.out.println("shelf");
		}
		
		long messagesSent = ((TestNetworkControl)network).getMessagesSent();
		long bytesSent = ((TestNetworkControl)network).getBytesSent();;
		
		long messagesReceived = ((TestNetworkControl)network).getMessagesReceived();
		long bytesReceived = ((TestNetworkControl)network).getBytesReceived();
		
		System.out.println("# == Network Statistics ==");
		System.out.println("# Message Sent           : " + messagesSent);
		System.out.println("# Bytes Sent             : " + bytesSent);
		System.out.println("# Mean Message Size Sent : " + (((double)bytesSent) / ((double)messagesSent)));
		System.out.println("#");
		System.out.println("# Message Received           : " + messagesReceived);
		System.out.println("# Bytes Received             : " + bytesReceived);
		System.out.println("# Mean Message Size Received : " + (((double)bytesReceived) / ((double)messagesReceived)));
		System.out.println("#");
		System.out.println("# Message Transferred           : " + (messagesSent + messagesReceived));
		System.out.println("# Bytes Transferred             : " + (bytesSent + bytesReceived));
		System.out.println("# Mean Message Size Transferred : " + (((double)(bytesSent + bytesReceived)) / ((double)(messagesSent + messagesReceived))));
		
		
	}
	
}
