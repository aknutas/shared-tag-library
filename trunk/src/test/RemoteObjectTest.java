package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import network.Control;
import data.Book;
import data.Bookshelf;
import data.Library;
import data.RemoteLibrary;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;

public class RemoteObjectTest {
	
	private Control network;
	private Library library1;
	private Library library2;
	
	@Before
	public void initializeLibraries() {
		this.network = new TestNetworkControl();
		this.library1 = new VirtualLibrary();
		this.library2 = new VirtualLibrary();
		
		/* a bookshelf about programming */
		Bookshelf shelf = new VirtualBookshelf("programming");
		shelf.insert(new VirtualBook("Parallel Programming", "Smith"));
		shelf.insert(new VirtualBook("Serial Programming", "Smith"));
		shelf.insert(new VirtualBook("Java Programming", "Jones"));
		shelf.insert(new VirtualBook("Discrete Structures", "Jones"));
		shelf.insert(new VirtualBook("Linux Programming", "Torvalds"));
		shelf.insert(new VirtualBook("Parallel Structures", "Stevenson"));
		library1.addBookshelf(shelf);
		
		/* books about mathematics */
		shelf = new VirtualBookshelf("mathematics");
		shelf.insert(new VirtualBook("Calculus", "Thomas"));
		shelf.insert(new VirtualBook("Linear Algebra", "Moore"));
		shelf.insert(new VirtualBook("Elements I", "Euclid"));
		shelf.insert(new VirtualBook("Elements II", "Euclid"));
		shelf.insert(new VirtualBook("Elements III", "Euclid"));
		shelf.insert(new VirtualBook("Elements IV", "Euclid"));
		shelf.insert(new VirtualBook("Elements V", "Euclid"));
		shelf.insert(new VirtualBook("Elements VI", "Euclid"));
		shelf.insert(new VirtualBook("Elements VII", "Euclid"));
		shelf.insert(new VirtualBook("Elements VIII", "Euclid"));
		shelf.insert(new VirtualBook("Elements IX", "Euclid"));
		shelf.insert(new VirtualBook("Elements X", "Euclid"));
		shelf.insert(new VirtualBook("Elements XI", "Euclid"));
		shelf.insert(new VirtualBook("Elements XII", "Euclid"));
		shelf.insert(new VirtualBook("Elements XIII", "Euclid"));
		shelf.insert(new VirtualBook("On the Sphere and the Cylinder I", "Archimedes"));
		shelf.insert(new VirtualBook("On the Sphere and the Cylinder II", "Archimedes"));
		shelf.insert(new VirtualBook("On Spirals", "Archimedes"));
		shelf.insert(new VirtualBook("On the Equilibrium of Planes I", "Archimedes"));
		shelf.insert(new VirtualBook("On the Equilibrium of Planes II", "Archimedes"));
		shelf.insert(new VirtualBook("Quadrature of the Parabola", "Archimedes"));
		shelf.insert(new VirtualBook("Conics I", "Appolonius"));
		shelf.insert(new VirtualBook("Conics II", "Appolonius"));
		shelf.insert(new VirtualBook("Conics III", "Appolonius"));
		shelf.insert(new VirtualBook("Introduction to Arithmetic I", "Gerasa"));
		shelf.insert(new VirtualBook("Introduction to Arithmetic II", "Gerasa"));
		library1.addBookshelf(shelf);
		
		/* a bookshelf of classics */
		shelf = new VirtualBookshelf("classics");
		shelf.insert(new VirtualBook("Old Man and the Sea", "Hemingway"));
		shelf.insert(new VirtualBook("The Great Gatsby", "Fitzgerlard"));
		shelf.insert(new VirtualBook("Macbeth", "Shakesphere"));
		shelf.insert(new VirtualBook("Catcher in the Rye", "Salinger"));
		shelf.insert(new VirtualBook("Crime and Punishment", "Dostoyevsky"));
		library1.addBookshelf(shelf);
		
		/* add library connections to test network */
		((TestNetwork)network).addLibrary("library1", library1);
		((TestNetwork)network).addLibrary("library2", library2);
	}
	
	@After
	public void printNetworkStatistics() {
		/* prevent exceptions in this code from reaching JUnit */
		try {
			long messagesSent = ((TestNetworkControl)this.network).getMessagesSent();
			long bytesSent = ((TestNetworkControl)this.network).getBytesSent();
			
			long messagesReceived = ((TestNetworkControl)this.network).getMessagesReceived();
			long bytesReceived = ((TestNetworkControl)this.network).getBytesReceived();
			
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
			System.out.println("# == End Statistics ==");
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * This method tests the functionality of the Library interface
	 * over a simulated network. 
	 * 
	 * @throws Excecption any sort of Exception not caught is
	 *         considered an error
	 */
	@Test
	public void testLibrary() throws Exception {
		Library remoteLibrary = new RemoteLibrary(this.network.connect("library1"), this.network);
		
		/* test addBookshelf (not allowed, should return false) */
		Assert.assertFalse(remoteLibrary.addBookshelf(new VirtualBookshelf("test")));
		
		/* test removeBookshelf (not allowed, should return false) */
		Assert.assertFalse(remoteLibrary.removeBookshelf(new VirtualBookshelf("test")));
		
		/* test getBookshelfNames */
		for(String shelfName : remoteLibrary.getBookshelfNames())
			Assert.assertNotNull(this.library1.getBookshelf(shelfName));
		
		/* test of getBookshelf */
		for(Bookshelf shelf : remoteLibrary)
			Assert.assertNotNull(this.library1.getBookshelf(shelf.getProperty("name")));
		
		/* test of getBookshelf(Collection) */
		Collection<String> shelfList = new LinkedList<String>();
		shelfList.add("mathematics");
		shelfList.add("programming");
		
		for(Bookshelf shelf : remoteLibrary.getBookshelf(shelfList))
			Assert.assertNotNull(this.library1.getBookshelf(shelf.getProperty("name")));
		
		/* test of getMasterBookshelf */
		for(Book book : remoteLibrary.getMasterShelf()) {
			boolean found = false;
			
			for(Bookshelf shelf : this.library1) {
				if(shelf.contains(book)) {
					found = true;
					break;
				}
			}
			
			Assert.assertTrue(found);
		}
	}
	
	/**
	 * This method tests the functionality of the Bookshelf interface
	 * over a simulated network.
	 * 
	 * @throws Exception any sort of Exception not caught is
	 *         considered an error
	 */
	@Test
	public void testBookshelf() throws Exception {
		Library remoteLibrary = new RemoteLibrary(this.network.connect("library1"), this.network);
		Bookshelf remoteShelf = remoteLibrary.getBookshelf("mathematics");
		System.out.println(remoteShelf);
		
		/* test of insert */
		Assert.assertFalse(remoteShelf.insert(new VirtualBook("Illegal Operation", "Illegal Operation")));
	}
	
}
