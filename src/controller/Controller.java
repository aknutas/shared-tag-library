package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import network.Control;
import network.ControlImpl;
import network.Definitions;
import network.messages.ChatMessage;
import network.messages.Message;
import scripts.InOutParser;
import butler.ButlerWeights;
import butler.HeadButler;
import butler.RemoteLibraryButler;
import data.Book;
import data.Bookshelf;
import data.Library;
import data.LibraryResponder;
import data.PersistentLibrary;
import data.VirtualBook;
import data.VirtualBookshelf;
import database.Access;
import database.AccessImpl;
import database.QueryBuilder;
import database.QueryBuilderImpl;

public class Controller {

	/**
	 * The network control object
	 */
	public Control cntrl;
	/**
	 * The local Persistent Library
	 */
	public PersistentLibrary myLib;
	/**
	 * The united QueryBuilder for the program
	 */
	public QueryBuilder qb;
	/**
	 * The currently focused book shelf used in add remove methods
	 */
	public Bookshelf focus;
	/**
	 * Contains bookshelves currently displayed or modified for eventual saving
	 */
	public Vector<Bookshelf> modifiedBs;

	/**
	 * a connection and its associated library
	 */
	public Vector<ConnectionMetadata> connections;
	
	
	/**
	 * The Head butler
	 */
	public HeadButler HB;
	/**
	 * The default controller constructor all things that need to be initialized
	 * on startup should be here
	 */
	@SuppressWarnings("unchecked")
	public Controller() {
		// load in library
		qb = new QueryBuilderImpl();
		myLib = new PersistentLibrary(qb);
		
		cntrl = new ControlImpl(new LibraryResponder(myLib));
		myLib.setProperty("name", "My Library");
		modifiedBs = new Vector<Bookshelf>();
		ProgramProperties props = ProgramProperties.getInstance();
		connections = (Vector<ConnectionMetadata>) props
				.getProperty("controller::connections");
		if (connections == null)
			connections = new Vector<ConnectionMetadata>();
		//System.out.println(qb.getButlerWeights().isEmpty());
		// Registering shutdown hooks
		addShutdownHooks();
	}

	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 * @throws IllegalArgumentException
	 *             if the focus shelf is not mutable or book is null
	 */
	public synchronized Book addBook(Book book) throws IllegalArgumentException {
		if (book == null || !(focus instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		focus.insert(book);
		return addBook(focus, book);
	}
	/**
	 * Add a book to the focused shelf in the library
	 * 
	 * @param title
	 *            the title of the book
	 * @param author
	 *            the author of the book
	 * @return the added book (null if error)
	 */
	public synchronized Book addBook(String title, String author)
			throws IllegalArgumentException {
		Book book = addBook(new VirtualBook(title, author));
		return addBook(focus, book);
	}
	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	private synchronized Book addBook(Bookshelf bookshelf, Book book) {
		if (book == null || !(bookshelf instanceof VirtualBookshelf))
			return null;
		bookshelf.insert(book);
		setFocus(bookshelf);
		if (!modifiedBs.contains(bookshelf)) {
			modifiedBs.add(bookshelf);
		}
		return book;
	}



	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public synchronized Bookshelf addBookshelf(Bookshelf bookshelf)
			throws IllegalArgumentException {
		if (bookshelf == null || !(bookshelf instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		if (!modifiedBs.contains(bookshelf)) {
			modifiedBs.add(bookshelf);
		}
		myLib.addBookshelf(bookshelf);
		setFocus(bookshelf);
		return bookshelf;
	}

	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public synchronized Bookshelf addBookshelf(String name)
			throws IllegalArgumentException {
		if (name == null)
			throw new IllegalArgumentException();
		VirtualBookshelf bookshelf = new VirtualBookshelf(name);
		if (!modifiedBs.contains(bookshelf)) {
			modifiedBs.add(bookshelf);
		}
		myLib.addBookshelf(bookshelf);
		setFocus(bookshelf);
		return bookshelf;
	}

	/**
	 * returns the Alias for all connections
	 * 
	 * @return
	 */
	public synchronized Vector<String> getConnections() {
		Vector<String> cons = new Vector<String>();
		if (!connections.isEmpty()) {
			for (int i = 0; i < connections.size(); i++) {
				cons.add(connections.get(i).getAlias());
			}
		}
		return cons;
	}

	/**
	 * adds a connection named alias
	 * 
	 * @param alias
	 * @param hostname
	 */
	public synchronized void addConnection(String alias, String hostname) {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getAlias().equals(alias)) {
				throw new IllegalArgumentException();
			}
		}
		connections.add(new ConnectionMetadata(alias, hostname));
	}

	/**
	 * remove the connection named alias
	 * 
	 * @param alias
	 */
	public synchronized void removeConnection(String alias) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getAlias().equals(alias)) {
				connections.remove(i);
				break;
			}
		}
	}

	
	/**
	 * remove the connection named alias
	 * 
	 * @param alias
	 */
	public synchronized boolean isConnected(String alias) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getAlias().equals(alias)) {
				return connections.get(i).isConnected();
			}
		}
		return false;
	}
	
	
	/**
	 * connects the named alias
	 * 
	 * @throws IllegalArgumentException
	 *             if already connected
	 * @param alias
	 */
	public synchronized void connect(String alias)
			throws IllegalArgumentException {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException("no available connections");
		}
		for (int id = 0; id < connections.size(); id++) {
			if (connections.get(id).getAlias().equals(alias)){
				if (connections.get(id).isConnected())
					throw new IllegalArgumentException("already connected");
				connections.get(id).connect(cntrl);
			}

			//HB.addButler(new RemoteLibraryButler(connections.get(id).getConnectionId()));
		}
	}

	/**
	 * disconnect the named alias
	 * 
	 * @throws IllegalArgumentException
	 *             if not connected
	 * @param alias
	 */
	public synchronized void disconnect(String alias) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int id = 0; id < connections.size(); id++) {
			if (connections.get(id).getAlias().equals(alias)){
				if (!connections.get(id).isConnected())
					throw new IllegalArgumentException();
				connections.get(id).disconnect(cntrl);
			}
		}
	}

	/**
	 * returns the library at the alias consisting of the imported shelves
	 * 
	 * @param alias
	 * @return returns null if not connected
	 */
	public synchronized Library getImportedLibrary(String alias) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int id = 0; id < connections.size(); id++) {
			if (connections.get(id).getAlias().equals(alias)) {
				return connections.get(id).getImportedLibrary();
			}
		}
		return null;
	}

	/**
	 * Gets the available shelves from a remote library
	 * 
	 * @param alias
	 * @return returns null if not connected else an iterator of all shelf names
	 */
	public synchronized Iterable<String> getShelveSelection(String alias) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int id = 0; id < connections.size(); id++) {
			if (connections.get(id).getAlias().equals(alias))
				return connections.get(id).getLibrary().getBookshelfNames();
		}
		return null;
	}

	/**
	 * Sets the selected shelves to a subset of the avaiable shelves
	 * 
	 * @param alias
	 * @return returns null if not connected else the library of selected
	 *         shelves
	 */
	public synchronized Library setShelfSelection(String alias,
			Collection<String> shelves) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int id = 0; id < connections.size(); id++) {
			ConnectionMetadata conn = connections.get(id);
			if (conn.getAlias().equals(alias)){
			    if(conn.isConnected()){
				return importSelectBookshelves(conn.getImportedLibrary(), conn
						.getLibrary(), shelves);
			    }
				else
					System.out.println("EROR not connected");				    
			}
		}
		System.out.println("error finding connection");
		return null;
	}
	/**
	 * Sets the selected shelves to a subset of the avaiable shelves
	 * 
	 * @param alias
	 * @return returns null if not connected else the library of selected
	 *         shelves
	 */
	public synchronized Library setShelfSelectionAll(String alias) {
		if (connections.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (int id = 0; id < connections.size(); id++) {
			ConnectionMetadata conn = connections.get(id);
			if (conn.getAlias().equals(alias)){
			    if(conn.isConnected()){
				return importAllBookshelves(conn.getImportedLibrary(), conn
						.getLibrary());
			    }
				else
					System.out.println("EROR not connected");				    
			}
		}
		System.out.println("error finding connection");
		return null;
	}
	/**
	 * This method registers shutdown hook in the runtime system. The hook is
	 * basically a thread that gets executed moments before the program
	 * terminates. Do not add any commands here that would take longer than a
	 * second to run.
	 */
	public void addShutdownHooks() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				// Closing off network connections
				cntrl.shutDown();

				saveAll();

				// These commands absolutely need to run last, and in this order
				ProgramProperties pp = ProgramProperties.getInstance();
				pp.setProperty("controller::connections", connections);
				pp.saveProperties();
				Access access = AccessImpl.getInstance();
				access.shutdown();
			}
		});

	}

	/**
	 * saves all modified bookshelves into the persistant library
	 */
	public synchronized void saveAll() {

		Iterator<Bookshelf> iter = modifiedBs.iterator();
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			if (bs instanceof VirtualBookshelf)
				myLib.saveBookshelf((VirtualBookshelf) bs);
		}
	}

	/**
	 * Adds all shelves from the remote library to the local library
	 * 
	 * @param local
	 * @param remote
	 */
	private synchronized Library importAllBookshelves(Library local, Library remote) throws NullPointerException{
		if (local == null || remote == null) {
			throw new NullPointerException();
		}
		Iterator<Bookshelf> iter = remote.iterator();
		if (iter == null)
			throw new NullPointerException("remote library iterator failure");
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			local.addBookshelf(bs);
		}
		return local;
	}

	/**
	 * Adds the selected shelves from the remote library to the local library
	 * 
	 * @param local
	 * @param remote
	 * @param selection
	 * @return
	 */
	private synchronized Library importSelectBookshelves(Library local,
			Library remote, Collection<String> selection) throws NullPointerException {
		System.out.println("Selecting shelves");
	    if (local == null || remote == null || selection == null) {
			throw new NullPointerException();
		}
	    	for(String str:selection)
			System.out.println(str);
	    	    
		Iterable<Bookshelf> shelves = remote.getBookshelf(selection);
		if(shelves==null)
			System.out.println("No valid shelves");	    
		Iterator<Bookshelf> iter = shelves.iterator();
		if (iter == null)
			throw new NullPointerException("remote library iterator failure");
		Bookshelf bs;
		while (iter.hasNext()) {
			bs = iter.next();
			local.addBookshelf(bs);
		}
		return local;
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
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public synchronized Book removeBook(Book book)
			throws IllegalArgumentException {
		if (!focus.contains(book))
			throw new IllegalArgumentException();
		focus.remove(book);
		myLib.saveBookshelf((VirtualBookshelf) focus);
		return book;
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public synchronized Book removeBook(Bookshelf bookshelf, Book book)
			throws IllegalArgumentException {
		setFocus(bookshelf);
		return removeBook(book);
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public synchronized Book removeBook(Bookshelf bookshelf, String name)
			throws IllegalArgumentException {
		setFocus(bookshelf);
		return removeBook(name);
	}

	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public synchronized Book removeBook(String name)
			throws IllegalArgumentException {
		if (name == null)
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
	 * Remove a bookshelf from the library not currently implemented
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public synchronized Bookshelf removeBookshelf(Bookshelf bookshelf) {
		setFocus(bookshelf);
		return removeBookshelf();
	}

	/**
	 * Remove a bookshelf from the library not currently implemented
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public synchronized Bookshelf removeBookshelf() {
		if (focus != null) {
			Bookshelf bs = focus;
			modifiedBs.remove(focus);
			if (!myLib.deleteBookshelf((VirtualBookshelf) focus))
				throw new IllegalArgumentException();
			focus = null;
			return bs;
		}
		return null;
	}

	/**
	 * Returns a butlerweights if something has been stored, or a null
	 * otherwise.
	 * 
	 * @return a stored Butler
	 */
	public synchronized ButlerWeights retrieveButlerWeights() {
		List<ButlerWeights> butlerList = qb.getButlerWeights();
		if (butlerList.size() == 0)
			return null;

		// TODO Storing several butlers and then retrieving the one you want
		return butlerList.get(0);
	}

	/**
	 * returns the iterator containing your library's bookshelves
	 * 
	 * @return the iterator
	 */
	public synchronized Iterator<Bookshelf> retrieveLibrary() {
		return myLib.iterator();
	}

	/**
	 * Returns the different libraries to search over
	 * 
	 * @return a list of libraries including local and local
	 */
	public synchronized Vector<String> searchOptions() {
		Vector<String> vec = new Vector<String>();
		vec.add("ALL");
		vec.add("My Library");
		for (int i = 0; i < connections.size(); i++) {
			vec.add(connections.get(i).getAlias());
		}
		return vec;
	}

	/**
	 * Search for the search terms on the target as selected from
	 * searchOptions()
	 * 
	 * @param target
	 * @param searchTerms
	 * @return
	 */
	public synchronized Bookshelf searchOn(String target, String searchTerms) {

		if (target.equals("ALL"))
			return searchAll(searchTerms);
		if (target.equals("My Library")) {
			return search(searchTerms, myLib);
		}
		for (int i = 0; i < connections.size(); i++) {
			if (target.equals(connections.get(i).getAlias())) {
				return search(searchTerms, connections.get(i).getLibrary());
			}
		}
		return null;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public synchronized Bookshelf searchAll(String str) {

		Vector<Library> libs = new Vector<Library>();
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).isConnected())
				libs.add(connections.get(i).getLibrary());
		}
		libs.add(myLib);
		return ControllerSearch.searchAlllibsFlat(str, libs);
	}

	/**
	 * A search matching any criteria with a single string on a library
	 * 
	 * @param str
	 * @param aLib
	 * @return
	 */
	private synchronized Bookshelf search(String str, Library aLib) {
		return ControllerSearch.search(str, aLib);
	}

	/**
	 * Sets the focus of the controller to a single bookshelf for use in methods
	 * instead of a bookshelf paramater MUST BE A VIRTUAL BOOKSHELF
	 * 
	 * @param bookshelf
	 *            the bookshelf in question
	 * @param id
	 *            its checkedOutBs id
	 * @throws IllegalArgumentException
	 *             the paramaters may not be invalid or null
	 */
	public synchronized void setFocus(Bookshelf bookshelf)
			throws IllegalArgumentException {
		if (bookshelf == null || !(bookshelf instanceof VirtualBookshelf))
			throw new IllegalArgumentException();
		focus = bookshelf;
	}

	/**
	 * a connection test method
	 * 
	 * @param target
	 * @param message
	 */
	public void testconnection(Integer target, String message) {
		cntrl.sendMsg(target, new ChatMessage(message));
	}

	/**
	 * writes the library to an file
	 * 
	 * @param fileName
	 */
	public synchronized void writeOut(String fileName) {
		InOutParser inOut = new InOutParser(null, fileName);
		inOut.writeOutLibrary(myLib);
	}

	/**
	 * reads in a file and adds its shelves to the library
	 * 
	 * @param fileName
	 */
	public synchronized void readInLibrary(String fileName) {
		InOutParser inOut = new InOutParser(fileName, null);
		try {
			inOut.processLineByLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Library lib = inOut.getParsedLib();
		Iterator<Bookshelf> iter = lib.iterator();
		while (iter.hasNext()) {
			System.out.println("loop");
			addBookshelf(iter.next());
		}
	}

	/**
	 * writes the library to an file
	 * 
	 * @param fileName
	 */
	public synchronized void writeOut(File file) throws FileNotFoundException {
		InOutParser inOut = new InOutParser(null, file);
		if (inOut != null) {
			inOut.writeOutLibrary(myLib);
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * reads in a file and adds its shelves to the library
	 * 
	 * @param fileName
	 */
	public synchronized void readInLibrary(File file)
			throws FileNotFoundException {
		InOutParser inOut = new InOutParser(file, null);
		try {
			inOut.processLineByLine();
		} catch (FileNotFoundException e) {
			throw e;
		}
		Library lib = inOut.getParsedLib();
		if (lib != null) {
			Iterator<Bookshelf> iter = lib.iterator();
			while (iter.hasNext()) {
				addBookshelf(iter.next());
			}
		} else {
			throw new FileNotFoundException();
		}
	}
}
