package controller;

import data.*;
import database.*;
import network.*;
import network.messages.ChatMessage;
import network.messages.Message;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import butler.ButlerWeights;

import scripts.Parser;
import scripts.ScriptGenerator;

public class Controller {

	public String libID = "ID";
	public String connectionID = "conID";
	public PersistentLibrary myLib;
	/**
	 * Contains bookshelves currently being displayed by the gui or being used
	 * by the search program
	 */
	private Map<Integer, Bookshelf> checkedOutBs;
	/**
	 * the Name or ip address of a connection and its ID <IP/name , id>
	 */
	private Map<String, Integer> connectionIds;

	/**
	 * the rename of a ip or Name for representing connections <IP/name , Alias>
	 */
	private HashMap<String, String> connectionAlias;
	/**
	 * the id of a connection and the remotelibrary that represents it
	 */
	private Map<Integer, RemoteLibrary> remoteLibs;

	private Map<Integer, VirtualLibrary> importedLibs;

	/**
	 * the united querybuilder for the program
	 */
	private QueryBuilder qb;
	/**
	 * the network controller
	 */
	public Control cntrl;
	/**
	 * the currently focused book shelf used in addBook
	 */
	public Bookshelf focus;
	/**
	 * the checkedOutBs id of the currently focused Bookshelf
	 */
	public Integer focusID;
	/**
	 * the next sequential id for storage
	 */
	public Integer nextID;

	/**
	 * the default controller constructor
	 * 
	 * all things that need to be initialized on startup should be here
	 * 
	 * @throws RemoteObjectException
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 */
	public Controller() throws UnknownHostException, IllegalArgumentException,
			NullPointerException, IOException, RemoteObjectException {
		// load in library
		qb = new QueryBuilderImpl();
		myLib = new PersistentLibrary(qb);
		nextID = 1;
		cntrl = new ControlImpl(new LibraryResponder(myLib));
		ProgramProperties props = ProgramProperties.getInstance();
		checkedOutBs = new HashMap<Integer, Bookshelf>();

		setupconnections(props);

		// Registering shutdown hooks
		addShutdownHooks();

	}

	@SuppressWarnings("unchecked")
	private void setupconnections(ProgramProperties props)
			throws UnknownHostException, IllegalArgumentException,
			NullPointerException, ConnectException, IOException, RemoteObjectException {
		Object o = props.getProperty("controller::connections");
		if (o == null) {
			connectionIds = new HashMap<String, Integer>();
			connectionAlias = new HashMap<String, String>();
			remoteLibs = new HashMap<Integer, RemoteLibrary>();
			importedLibs = new HashMap<Integer, VirtualLibrary>();
		} else if (!(o instanceof HashMap<?, ?>)) {
			connectionIds = new HashMap<String, Integer>();
			remoteLibs = new HashMap<Integer, RemoteLibrary>();
			importedLibs = new HashMap<Integer, VirtualLibrary>();
			HashMap<String, String> tmp = (HashMap<String, String>) o;
			connectionAlias = tmp;
			connectionIds = new HashMap<String, Integer>();
			Iterator<Entry<String, String>> iter = tmp.entrySet().iterator();
			Entry<String, String> e;
			while (iter.hasNext()) {
				e = iter.next();
				reconnect(e.getKey());
			}
		}

	}

	/**
	 * Sets the focus of the controller to a single bookshelf for use in methods
	 * instead of a bookshelf paramater
	 * 
	 * @param bookshelf
	 *            the bookshelf in question
	 * @param id
	 *            its checkedOutBs id
	 * @throws IllegalArgumentException
	 *             the paramaters may not be invalid or null
	 */
	public void setFocus(Bookshelf bookshelf, Integer id)
			throws IllegalArgumentException {
		if (bookshelf == null || id == null || id < 0)
			throw new IllegalArgumentException();
		focus = bookshelf;

		System.out.println(bookshelf.iterator().hasNext());

		for (Book b : bookshelf)
			System.out.println(b.getProperty("title"));

		focusID = id;
	}

	/**
	 * Used for accessing a bookshelf that have been retrieved.
	 * 
	 * @param key
	 *            the unique key handed to the module for retrieving the BS
	 * @return the bookshelf if the key is valid
	 * @throws IllegalArgumentException
	 *             if the key is not in the map
	 */
	public Bookshelf getBs(Integer key) throws IllegalArgumentException {
		if (!checkedOutBs.containsKey(key)) {
			throw new IllegalArgumentException();
		} else
			return checkedOutBs.get(key);
	}

	/**
	 * request a bookshelf from the library based on a string name
	 * 
	 * @param loc
	 * @return the key to reference the the object with the get command
	 */
	public Integer retrieveShelf(String loc) throws IllegalArgumentException {
		if (loc == null)
			throw new IllegalArgumentException();
		Integer num = nextID;
		Iterator<Bookshelf> iter = myLib.iterator();
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			if (bs.getProperty("Name") == loc) {
				checkedOutBs.put(num, bs);
				break;
			}
		}
		nextID++;
		return num;
	}

	/**
	 * Retrieves a vector of all the library's bookshelf names
	 * 
	 * @return the vector of names
	 */
	public Vector<String> retrieveLibraryNames() {
		Iterator<Bookshelf> iter = myLib.iterator();
		Vector<String> names = new Vector<String>();
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			names.add(bs.getProperty("Name"));
		}
		return names;
	}

	/**
	 * returns the iterator containing all bookshelfs
	 * 
	 * @return the iterator
	 */
	public Iterator<Bookshelf> retrieveLibrary() {
		return myLib.iterator();
	}

	/**
	 * returns the collection of the current aliases of the lib
	 * 
	 * @return the iterator
	 */
	public Collection<String> retrieveRemoteLibraryNames() {
		return connectionAlias.values();
	}

	/**
	 * Add a book to the library
	 * 
	 * @param bookshelf
	 *            the target bookshelf
	 * @param name
	 * @param title
	 * @return the added book
	 * @throws IllegalArgumentException
	 */
	public Book addBook(Bookshelf bookshelf, String name, String title)
			throws IllegalArgumentException {
		if (name == null || title == null
				|| !(bookshelf instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		Book book = addBook(bookshelf, new VirtualBook(name, title));
		myLib.saveBookshelf((VirtualBookshelf) bookshelf);
		return book;
	}

	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(Bookshelf bookshelf, Book book) {
		if (book == null || !(bookshelf instanceof VirtualBookshelf))
			return null;
		bookshelf.insert(book);
		myLib.saveBookshelf((VirtualBookshelf) bookshelf);
		return book;
	}

	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(String name, String title)
			throws IllegalArgumentException {
		if (name == null || title == null
				|| !(focus instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		Book book = addBook(focus, new VirtualBook(name, title));
		myLib.saveBookshelf((VirtualBookshelf) focus);
		return book;
	}

	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(Book book) throws IllegalArgumentException {
		if (book == null || !(focus instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		focus.insert(book);
		myLib.saveBookshelf((VirtualBookshelf) focus);
		return addBook(focus, book);
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Bookshelf bookshelf, String name)
			throws IllegalArgumentException {
		if (name == null || !(bookshelf instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		Iterator<Book> iter = bookshelf.iterator();
		Book book = null;
		while (iter.hasNext()) {
			book = iter.next();
			if (book.getProperty("Name") == name) {
				removeBook(bookshelf, book);
				break;
			}
		}
		myLib.saveBookshelf((VirtualBookshelf) bookshelf);
		return book;
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(String name) throws IllegalArgumentException {
		if (name == null || !(focus instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		Iterator<Book> iter = focus.iterator();
		Book book = null;
		while (iter.hasNext()) {
			book = iter.next();
			if (book.getProperty("Name") == name) {
				removeBook(focus, book);
				break;
			}
		}
		myLib.saveBookshelf((VirtualBookshelf) focus);
		return book;
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Bookshelf bookshelf, Book book)
			throws IllegalArgumentException {
		if (!bookshelf.contains(book)
				|| !(bookshelf instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		bookshelf.remove(book);
		myLib.saveBookshelf((VirtualBookshelf) bookshelf);
		return book;
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Book book) throws IllegalArgumentException {
		if (!focus.contains(book) || !(focus instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		focus.remove(book);
		myLib.saveBookshelf((VirtualBookshelf) focus);
		return book;
	}

	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public Bookshelf addBookshelf(String name) throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException();

		Integer tmp = nextID;
		VirtualBookshelf bs = new VirtualBookshelf(name);
		myLib.saveBookshelf(bs);
		checkedOutBs.put(tmp, bs);
		setFocus(bs, tmp);
		nextID++;
		return bs;
	}

	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public Bookshelf addBookshelf(Book book) throws IllegalArgumentException {
		if (book == null)
			throw new IllegalArgumentException();
		Integer tmp = nextID;
		VirtualBookshelf bs = new VirtualBookshelf("From book "
				+ book.getProperty("Name"));
		addBook(bs, book);
		myLib.saveBookshelf(bs);
		checkedOutBs.put(tmp, bs);
		nextID++;
		setFocus(bs, tmp);
		return bs;
	}

	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(Bookshelf bookshelf, String name) {
		if (bookshelf == null)
			return removeBookshelf(name);
		else
			return removeBookshelf(bookshelf);
	}

	/**
	 * Remove a bookshelf from the library not currently implemented
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(String name) {
		if (name == null)
			return null;

		return null;
	}

	/**
	 * Remove a bookshelf from the library not currently implemented
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(Bookshelf bookshelf) {
		return null;
	}

	/**
	 * Remove a bookshelf from the library not currently implemented
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(Bookshelf bookshelf, Book book) {
		return null;
	}

	/**
	 * Initialize a bookshelf with dummy data entries
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf initializeDummyData() {
		return null;
	}

	/*
	 * If you need any methods add a quick stub and description and ill get to
	 * it this is just an early version more to follow
	 */

	/**
	 * Returns a butlerweights if something has been stored, or a null
	 * otherwise.
	 * 
	 * @return a stored Butler
	 */
	public ButlerWeights retrieveButlerWeights() {
		List<ButlerWeights> butlerList = qb.getButlerWeights();
		if (butlerList.size() == 0)
			return null;

		// TODO Storing several butlers and then retrieving the one you want
		return butlerList.get(0);
	}

	public Bookshelf search(String str) {
		if (str == null)
			return null;
		Bookshelf result = new VirtualBookshelf("Search of " + str);
		BookQuery bq = new BookQuery();
		bq.match(str);
		Iterator<Bookshelf> iter = myLib.iterator();
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			Iterator<Book> bsiter = bs.iterator();
			Book book = null;
			while (bsiter.hasNext()) {
				book = bsiter.next();
				if (bq.compareTo(book) == 0) {
					result.insert(book);
				}
			}

		}
		return result;
	}

	public Bookshelf search(String str, Bookshelf bookshelf) {
		if (str == null)
			return null;
		Bookshelf result = new VirtualBookshelf("Search of " + str);
		BookQuery bq = new BookQuery();
		bq.match(str);
		Iterator<Book> bsiter = bookshelf.iterator();
		Book book = null;
		while (bsiter.hasNext()) {
			book = bsiter.next();
			if (bq.compareTo(book) == 0) {
				result.insert(book);
			}
		}

		return result;
	}

	/**
	 * a method for use in multi term specific area searching
	 * 
	 * @param list
	 * @return
	 */
	public Bookshelf search(Map<String, Vector<String>> list) {
		if (list == null)
			return null;
		Bookshelf result = new VirtualBookshelf("Search Result shelf");
		BookQuery bq = new BookQuery();
		Iterator<Entry<String, Vector<String>>> iter = list.entrySet()
				.iterator();

		while (iter.hasNext()) {
			Entry<String, Vector<String>> enter = iter.next();
			Vector<String> v = enter.getValue();
			for (int i = 0; i < v.size(); i++) {
				if (enter.getKey().equalsIgnoreCase("tag")) {
					if (v.elementAt(i).indexOf("-") == 0)
						bq.matchTag(v.elementAt(i).substring(1), true);
					else
						bq.matchTag(v.elementAt(i), false);

				}
				if (v.elementAt(i).indexOf("-") == 0)
					bq.matchProperty(enter.getKey(), v.elementAt(i)
							.substring(1), true);
				else
					bq.matchProperty(enter.getKey(), v.elementAt(i), false);

			}
		}
		Iterator<Bookshelf> libiter = myLib.iterator();
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = libiter.next();
			Iterator<Book> bsiter = bs.iterator();
			Book book = null;
			while (bsiter.hasNext()) {
				book = bsiter.next();
				if (bq.compareTo(book) == 0) {
					result.insert(book);
				}
			}

		}
		return result;
	}

	/**
	 * a method for use in multi term specific area searching
	 * 
	 * @param list
	 * @return
	 */
	public Bookshelf search(Map<String, Vector<String>> list,
			Bookshelf bookshelf) {
		if (list == null)
			return null;
		Bookshelf result = new VirtualBookshelf("Search Result shelf");
		BookQuery bq = new BookQuery();
		Iterator<Entry<String, Vector<String>>> iter = list.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Entry<String, Vector<String>> enter = iter.next();
			Vector<String> v = enter.getValue();
			for (int i = 0; i < v.size(); i++) {
				if (enter.getKey().equalsIgnoreCase("tag")) {
					if (v.elementAt(i).indexOf("-") == 0)
						bq.matchTag(v.elementAt(i).substring(1), true);
					else
						bq.matchTag(v.elementAt(i), false);

				}
				if (v.elementAt(i).indexOf("-") == 0)
					bq.matchProperty(enter.getKey(), v.elementAt(i)
							.substring(1), true);
				else
					bq.matchProperty(enter.getKey(), v.elementAt(i), false);

			}
		}

		Iterator<Book> bsiter = bookshelf.iterator();
		Book book = null;
		while (bsiter.hasNext()) {
			book = bsiter.next();
			if (bq.compareTo(book) == 0) {
				result.insert(book);
			}
		}
		return result;
	}

	public void addConnection(String host) throws UnknownHostException,
			IOException, ConnectException, IllegalArgumentException, NullPointerException,
			RemoteObjectException {

		if (host != null) {
			if (connectionIds.keySet().contains(host)) {
				throw new IllegalArgumentException();
			}
			Integer temp = cntrl.connect(host);
			System.out.println(" im back");
			connectionIds.put(host, temp);
			connectionAlias.put(host, host);
			testconnection(temp, "Are you still there?");
			RemoteLibrary rl = new RemoteLibrary(temp, cntrl);
			VirtualLibrary vl = new VirtualLibrary();
			rl.setProperty(libID, host);
			vl.setProperty(libID, host);
			rl.setProperty(connectionID, host);
			vl.setProperty(connectionID, host);
			remoteLibs.put(temp, rl);
			importedLibs.put(temp, vl);
			// TODO remove add all and set up for selective add
			// add change to utilise new structure even though no selective add
			importAllBookshelves(vl, remoteLibs.get(temp));
		}
	}

	private void reconnect(String host) throws UnknownHostException,
			IOException, IllegalArgumentException, NullPointerException,
			RemoteObjectException {

		if (host != null) {
			if (connectionIds.keySet().contains(host)) {
				throw new IllegalArgumentException();
			}
			Integer temp = cntrl.connect(host);
			System.out.println(" im back");
			connectionIds.put(host, temp);
			RemoteLibrary rl = new RemoteLibrary(temp, cntrl);
			VirtualLibrary vl = new VirtualLibrary();
			rl.setProperty(libID, connectionAlias.get(host));
			vl.setProperty(libID, connectionAlias.get(host));
			rl.setProperty(connectionID, host);
			vl.setProperty(connectionID, host);
			remoteLibs.put(temp, rl);
			importedLibs.put(temp, vl);
			// TODO remove add all and set up for selective add
			// add change to utilise new structure even though no selective add
			importAllBookshelves(vl, remoteLibs.get(temp));
		}

	}

	public void breakConnection(String host) {
		if (!connectionIds.keySet().contains(host)) {
			throw new IllegalArgumentException();
		}
		Integer tmp = connectionIds.get(host);
		cntrl.disconnect(tmp);
		connectionIds.remove(host);
		connectionAlias.remove(host);
		remoteLibs.remove(tmp);
		importedLibs.remove(tmp);

	}

	public Set<String> getConnections() {
		return connectionIds.keySet();
	}

	public Set<String> getConnectionsAlias() {
		return connectionAlias.keySet();
	}

	public Map<String, String> getConnectionsAliasPairs() {
		return connectionAlias;
	}

	public String setConnectionAlias(String name, String newalias) {
		if (!connectionAlias.containsKey(name))
			throw new IllegalArgumentException();
		String temp = connectionAlias.put(name, newalias);
		importedLibs.get(name).setProperty(libID, newalias);
		remoteLibs.get(name).setProperty(libID, newalias);
		return temp;
	}

	public void importAllBookshelves(Library local, Library remote) {
		if (local == null || remote == null) {
			throw new IllegalArgumentException();
		}
		Iterator<Bookshelf> iter = remote.iterator();
		if (iter == null)
			throw new IllegalArgumentException();
		Bookshelf bs;
		while (iter.hasNext()) {
			// System.out.println("dsaf");
			bs = iter.next();
			local.addBookshelf(bs);
		}
	}

	public void importSelectBookshelves(Library local, Library remote,
			Collection<String> select) {
		if (local == null || remote == null) {
			throw new IllegalArgumentException();
		}
		Iterator<Bookshelf> iter = remote.getBookshelf(select);
		if (iter == null)
			throw new IllegalArgumentException();
		Bookshelf bs;
		while (iter.hasNext()) {
			// System.out.println("dsaf");
			bs = iter.next();
			local.addBookshelf(bs);
		}
	}

	public void importABookshelves(Library local, Library remote, String select) {
		if (local == null || remote == null) {
			throw new IllegalArgumentException();
		}
		Bookshelf bs = remote.getBookshelf(select);
		if (bs == null)
			throw new IllegalArgumentException();
		local.addBookshelf(bs);
	}

	public void testconnection(Integer target, String message) {
		cntrl.sendMsg(target, new ChatMessage(message));
	}

	public void removeAllBookshelves(Library local, Library remote) {
		if (local == null || remote == null) {
			throw new IllegalArgumentException();
		}

		Iterator<Bookshelf> iter = remote.iterator();
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			local.removeBookshelf(bs);
		}
	}

	/**
	 * This method is responsible for handling and responding to incoming
	 * messages and status changes.
	 * 
	 * @author Antti Knutas (Complain to him about errors)
	 */
	public void messageHandler() {
		// Debug
		// System.out.println("messageHandler activated");

		// Getting the message queue map and making sure that there is content
		Map<Integer, List<Message>> msgMap = cntrl.whatsUp();

		if (msgMap != null) {

			Set<Entry<Integer, List<Message>>> entryset = msgMap.entrySet();
			Iterator<Entry<Integer, List<Message>>> i = entryset.iterator();

			// Iterating through each queue
			while (i.hasNext()) {
				Entry<Integer, List<Message>> tryentry = i.next();
				List<Message> tempqueue = tryentry.getValue();

				// Getting messages from each queue
				// Debug message
				// System.out.println("MH: Loopin'");

				if (tempqueue != null) {
					Iterator<Message> msgiterator = tempqueue.iterator();
					// Debug
					// System.out.println("Iteratin', size: " +
					// tempqueue.size());

					while (msgiterator.hasNext()) {
						// TODO Do actual message-based functionality here
						Object tryout = msgiterator.next();
						if (tryout.getClass().getName().equals(
								network.messages.ChatMessage.class.getName())) {
							ChatMessage hello = (ChatMessage) tryout;
							// TODO Replace console printout with small GUI
							// notification
							System.out.println("Connection "
									+ tryentry.getKey() + " says "
									+ hello.GetMessage());
						} else {
							// Error: This shouldn't happen.
							System.out
									.println("Unknown Foreign Object recieved. UFO ALERT:"
											+ tryentry.getClass().getName());
						}
					}
				}
			}
		} else {
			// Debug
			// System.out.println("Read too soon: msgqueues empty");
		}
		// This is where the heartbeat-initiated connection status iteration
		// happens
		Map<Integer, Integer> statusMap = cntrl.getStatus();

		// Iterating through thread statuses
		if (statusMap != null) {
			Set<Entry<Integer, Integer>> statusEntries = statusMap.entrySet();
			Iterator<Entry<Integer, Integer>> statusIter = statusEntries
					.iterator();

			while (statusIter.hasNext()) {
				Entry<Integer, Integer> tryentry = statusIter.next();
				if (tryentry.getValue() == Definitions.DISCONNECTED) {
					// Events to be triggered with a connection is disconnected.
					// tryentry.getKey will give the disconnected thread's
					// connection id as an integer
					cntrl.disconnect(tryentry.getKey());
				}
			}
		}
	}

	/**
	 * This method registers shutdown hook in the runtime system. The hook is
	 * basically a thread that gets executed moments before the program
	 * terminates. Do not add any commands here that would take longer than a
	 * second to run.
	 */
	public void addShutdownHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {

				// Closing off network connections
				cntrl.shutDown();

				// These commands absolutely need to run last, and in this order
				ProgramProperties pp = ProgramProperties.getInstance();
				pp.setProperty("controller::connections", connectionAlias);
				pp.saveProperties();
				Access access = AccessImpl.getInstance();
				access.shutdown();
			}
		});

	}

	/**
	 * a test method
	 */
	public void setuptestController() {
		ScriptGenerator sg = new ScriptGenerator("src\\scripts\\en_US.dic");
		try {
			sg.processLineByLine();
		} catch (Exception e) {
			System.err.println("error in s processline Dic " + e);
		}
		sg.generateLibrary(10, 5);
		sg.p = new Parser("src\\scripts\\books.txt");

		try {
			sg.p.processLineByLine();
		} catch (Exception e) {
			System.err.println("error in s processline books " + e);
		}

		myLib = sg.p.lib;
	}
}
