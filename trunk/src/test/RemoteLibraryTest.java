package test;

import java.io.IOException;
import java.net.UnknownHostException;

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
		library.addBookshelf(shelf1);
		
		/* add library to network */
		((TestNetwork)network).addLibrary("andrew", library);
		
		/* connect */
		int connection = network.connect("andrew");
		Library remoteLibrary = new RemoteLibrary(connection, network);
		
		Bookshelf remoteShelf = remoteLibrary.getMasterShelf();
		
	}
	
}
