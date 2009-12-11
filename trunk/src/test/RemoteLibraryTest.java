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
		Library library1 = new VirtualLibrary();
		Library library2 = new VirtualLibrary();
		Library library3 = new VirtualLibrary();
		
		/* a bookshelf about programming */
		Bookshelf shelf1 = new VirtualBookshelf();
		shelf1.insert(new VirtualBook("Parallel Programming", "Smith"));
		shelf1.insert(new VirtualBook("Serial Programming", "Smith"));
		shelf1.insert(new VirtualBook("Java Programming", "Jones"));
		shelf1.insert(new VirtualBook("Discrete Structures", "Jones"));
		shelf1.insert(new VirtualBook("Linux Programming", "Torvalds"));
		shelf1.insert(new VirtualBook("Parallel Structures", "Stevenson"));
		library1.addBookshelf(shelf1);
		
		/* a bookshelf of classics */
		Bookshelf shelf2 = new VirtualBookshelf();
		shelf2.insert(new VirtualBook("Old Man and the Sea", "Hemingway"));
		shelf2.insert(new VirtualBook("The Great Gatsby", "Fitzgerlard"));
		shelf2.insert(new VirtualBook("Macbeth", "Shakesphere"));
		shelf2.insert(new VirtualBook("Catcher in the Rye", "Salinger"));
		shelf2.insert(new VirtualBook("Crime and Punishment", "Dostoyevsky"));
		library2.addBookshelf(shelf2);
		
		//library3.addBookshelf(shelf1);
		
		/* add library to network */
		((TestNetwork)network).addLibrary("andrew", library1);
		((TestNetwork)network).addLibrary("antti", library1);
		((TestNetwork)network).addLibrary("patrick", library1);
		
		/* connect */
		int connection1 = network.connect("andrew");
		int connection2 = network.connect("antti");
		int connection3 = network.connect("patrick");
	
		Library remoteLibrary1 = null;
		Library remoteLibrary2 = null;
		Library remoteLibrary3 = null;
		
		try {
			remoteLibrary1 = new RemoteLibrary(connection1, network);
			remoteLibrary2 = new RemoteLibrary(connection2, network);
			remoteLibrary3 = new RemoteLibrary(connection3, network);
			
			library3.addBookshelf(remoteLibrary1.getMasterShelf());
			library3.addBookshelf(remoteLibrary2.getMasterShelf());
		}
		catch(RemoteObjectException ex) {
			System.out.println("timeoutdf");
			return;
		}
		
		for(Bookshelf shelf : remoteLibrary3) {
			System.out.println("shelf size: " + shelf.size());
			
			Iterator<Book> it2 = shelf.iterator();
			for(Book book : shelf) {
				System.out.println("book title: " + book.getProperty("title"));
				System.out.println("book author: " + book.getProperty("author"));
			}
		}
		
		long messagesSent = ((TestNetworkControl)network).getMessagesSent();
		long bytesSent = ((TestNetworkControl)network).getBytesSent();
		
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
		System.out.println("#");
		//System.out.println("# Iterator Message Count : " + TrackedMessage.countReceivedMessages(IteratorMessage.class));
		
		
	}
	
}
