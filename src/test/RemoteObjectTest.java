package test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map.Entry;

import junit.framework.Assert;
import network.Control;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.Book;
import data.BookQuery;
import data.Bookshelf;
import data.Library;
import data.RemoteLibrary;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;

/**
 * This is the unit tests for the RemoteObject system
 * .
 * @author Andrew Alm
 */
public class RemoteObjectTest {

	private Control network;
	private Library library1;
	private Library library2;

	/**
	 * Used to setup the libraries and bookshelves for the test
	 * before each test is run.
	 */
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
		shelf.insert(new VirtualBook("On the Sphere and the Cylinder I",
				"Archimedes"));
		shelf.insert(new VirtualBook("On the Sphere and the Cylinder II",
				"Archimedes"));
		shelf.insert(new VirtualBook("On Spirals", "Archimedes"));
		shelf.insert(new VirtualBook("On the Equilibrium of Planes I",
				"Archimedes"));
		shelf.insert(new VirtualBook("On the Equilibrium of Planes II",
				"Archimedes"));
		shelf
				.insert(new VirtualBook("Quadrature of the Parabola",
						"Archimedes"));
		shelf.insert(new VirtualBook("Conics I", "Appolonius"));
		shelf.insert(new VirtualBook("Conics II", "Appolonius"));
		shelf.insert(new VirtualBook("Conics III", "Appolonius"));
		shelf.insert(new VirtualBook("Introduction to Arithmetic I", "Gerasa"));
		shelf
				.insert(new VirtualBook("Introduction to Arithmetic II",
						"Gerasa"));
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
		((TestNetwork) network).addLibrary("library1", library1);
		((TestNetwork) network).addLibrary("library2", library2);
	}

	/**
	 * Prints usage statistics at end of a test case.
	 */
	@After
	public void printNetworkStatistics() {
		/* prevent exceptions in this code from reaching JUnit */
		try {
			long messagesSent = ((TestNetworkControl) this.network)
					.getMessagesSent();
			long bytesSent = ((TestNetworkControl) this.network).getBytesSent();

			long messagesReceived = ((TestNetworkControl) this.network)
					.getMessagesReceived();
			long bytesReceived = ((TestNetworkControl) this.network)
					.getBytesReceived();

			System.out.println("# == Network Statistics ==");
			System.out.println("# Message Sent           : " + messagesSent);
			System.out.println("# Bytes Sent             : " + bytesSent);
			System.out.println("# Mean Message Size Sent : "
					+ (((double) bytesSent) / ((double) messagesSent)));
			System.out.println("#");
			System.out.println("# Message Received           : "
					+ messagesReceived);
			System.out.println("# Bytes Received             : "
					+ bytesReceived);
			System.out.println("# Mean Message Size Received : "
					+ (((double) bytesReceived) / ((double) messagesReceived)));
			System.out.println("#");
			System.out.println("# Message Transferred           : "
					+ (messagesSent + messagesReceived));
			System.out.println("# Bytes Transferred             : "
					+ (bytesSent + bytesReceived));
			System.out
					.println("# Mean Message Size Transferred : "
							+ (((double) (bytesSent + bytesReceived)) / ((double) (messagesSent + messagesReceived))));
			System.out.println("# == End Statistics ==");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This method tests the functionality of the Library interface 
	 * over a simulated network. It does the following tests:
	 *   1. Attempt to add a bookshelf (operation not allowed)
	 *   2. Attempt to remove a bookshelf (operation not allowed)
	 *   3. Get the names of all bookshelves in a library
	 *   4. Fetch a bookshelf by name
	 *   5. Get a Iterable collection of bookshelves by using a
	 *      collection of names
	 *   6. Get the master bookshelf for the library
	 *   7. Iterator over all bookshelves in a library
	 * 
	 * @throws Excecption
	 *             any sort of Exception not caught is considered an error
	 */
	@Test
	public void testLibrary() throws Exception {
		int count;
		Library remoteLibrary = new RemoteLibrary(this.network
				.connect("library1"), this.network);

		/* test addBookshelf (not allowed, should return false) */
		Assert.assertFalse(remoteLibrary.addBookshelf(new VirtualBookshelf(
				"test")));

		/* test removeBookshelf (not allowed, should return false) */
		Assert.assertFalse(remoteLibrary.removeBookshelf(new VirtualBookshelf(
				"test")));

		/* test getBookshelfNames */
		count = 0;
		for (String shelfName : remoteLibrary.getBookshelfNames()) {
			Assert.assertNotNull(this.library1.getBookshelf(shelfName));
			count += 1;
		}

		Assert.assertEquals(3, count);

		/* test of getBookshelf */
		count = 0;
		for (Bookshelf shelf : remoteLibrary) {
			Assert.assertNotNull(remoteLibrary.getBookshelf(shelf
					.getProperty("name")));
			count += 1;
		}

		Assert.assertEquals(3, count);

		/* test of getBookshelf(Collection) */
		Collection<String> shelfList = new LinkedList<String>();
		shelfList.add("mathematics");
		shelfList.add("programming");

		count = 0;
		for (Bookshelf shelf : remoteLibrary.getBookshelf(shelfList)) {
			Assert.assertNotNull(this.library1.getBookshelf(shelf
					.getProperty("name")));
			count += 1;
		}

		Assert.assertEquals(2, count);

		/* test of getMasterBookshelf */
		for (Book book : remoteLibrary.getMasterShelf()) {
			boolean found = false;

			for (Bookshelf shelf : this.library1) {
				if (shelf.contains(book)) {
					found = true;
					break;
				}
			}

			Assert.assertTrue(found);
		}

		/* test of iterator() */
		count = 0;
		for (Bookshelf shelf : remoteLibrary)
			count += 1;

		Assert.assertEquals(3, count);

	}

	/**
	 * This method tests the functionality of the Bookshelf interface 
	 * over a simulated network. It does the following tests:
	 *   1. Attempt to insert book onto remote shelf (illegal
	 *      operation)
	 *   2. Attempt to remote a book from a remote shelf (illegal
	 *      operation)
	 *   3. Attempt to remove all books from a remote shelf (illegal
	 *      operation)
	 *   4. Iterator over all books on a shelf
	 * 
	 * @throws Excecption
	 *             any sort of Exception not caught is considered an error
	 */
	@Test
	public void testBookshelf() throws Exception {
		int count;

		Library remoteLibrary = new RemoteLibrary(this.network
				.connect("library1"), this.network);
		Bookshelf remoteShelf = remoteLibrary.getBookshelf("classics");

		/* test of insert */
		Assert.assertFalse(remoteShelf.insert(new VirtualBook(
				"Illegal Operation", "Illegal Operation")));

		/* test of remove */
		Assert.assertFalse(remoteShelf.remove(new VirtualBook(
				"Illegal Operation", "Illegal Operation")));

		/* test of removeAll */
		Assert.assertFalse(remoteShelf.removeAll());

		/* test of iterator */
		count = 0;
		for (Book book : remoteShelf)
			count += 1;

		Assert.assertEquals(5, count);
	}

	/**
	 * This method tests the functionality of the Book interface over
	 * a simulated network. It does the following tests:
	 *   1. Tag a book
	 *   2. Untag a book
	 *   3. Get weight of a tag
	 *   4. Get count of number of tags book contains
	 *   5. Iterate through all tags
	 *   6. Make a virtual clone of a remote book
	 * 
	 * @throws Excecption
	 *             any sort of Exception not caught is considered an error
	 */
	@Test
	public void testBook() throws Exception {
		int count;

		Library remoteLibrary = new RemoteLibrary(this.network
				.connect("library1"), this.network);
		Bookshelf remoteShelf = remoteLibrary.getBookshelf("programming");

		/* test of tag */
		for (Book book : remoteShelf) {
			Assert.assertEquals(1, book.tag("programming"));
			Assert.assertEquals(2, book.tag("programming"));
			Assert.assertEquals(3, book.tag("programming"));

			Assert.assertEquals(1, book.tag("computer science"));
		}

		/* test of untag */
		for (Book book : remoteShelf)
			Assert.assertEquals(2, book.untag("programming"));

		/* test of weight */
		for (Book book : remoteShelf) {
			Assert.assertEquals(2, book.weight("programming"));
			Assert.assertEquals(1, book.weight("computer science"));
			Assert.assertEquals(0, book.weight("foobar"));
		}

		/* test of getTagCount */
		for (Book book : remoteShelf)
			Assert.assertEquals(4, book.getTagCount()); /*
														 * two extra for
														 * title/author
														 */

		/* test of enumerateTags */
		for (Book book : remoteShelf) {
			count = 0;
			for (Entry<String, Integer> tag : book.enumerateTags())
				count += 1;

			Assert.assertEquals(book.getTagCount(), count);
		}

		/* test of makeVirtual */
		for (Book book : remoteShelf) {
			Assert.assertTrue(book.makeVirtual() instanceof VirtualBook);
			Assert.assertEquals(book.getProperty("title"), book.makeVirtual()
					.getProperty("title"));
		}

	}

	/**
	 * This method tests the functionality of the BookQuery class 
	 * over a simulated network. It does the following tests:
	 *   1. Test bookquery match searching
	 *   2. Test bookquery mathProperty searching
	 * 
	 * @throws Excecption
	 *             any sort of Exception not caught is considered an error
	 */
	@Test
	public void bookQueryTest() throws Exception {
		int count;

		Library remoteLibrary = new RemoteLibrary(this.network
				.connect("library1"), this.network);
		Bookshelf programmingShelf = remoteLibrary.getBookshelf("programming");
		Bookshelf mathShelf = remoteLibrary.getBookshelf("mathematics");
		BookQuery query;

		/* test of match */
		count = 0;
		query = new BookQuery();
		query.match(".*Programming.*");

		for (Book book : programmingShelf.subset(query))
			count += 1;

		Assert.assertEquals(4, count);

		/* test of matchProperty */
		count = 0;
		query = new BookQuery();
		query.matchProperty("title", ".*Elements.*", false);

		for (Book book : mathShelf.subset(query))
			count += 1;

		Assert.assertEquals(13, count);
	}

}
