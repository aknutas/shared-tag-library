package controller;

import java.io.IOException;
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

import network.Control;
import network.ControlImpl;
import network.Definitions;
import network.messages.ChatMessage;
import network.messages.Message;
import scripts.Parser;
import scripts.ScriptGenerator;
import butler.ButlerWeights;
import data.Book;
import data.Bookshelf;
import data.Library;
import data.LibraryResponder;
import data.PersistentLibrary;
import data.RemoteLibrary;
import data.RemoteObjectException;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;
import database.Access;
import database.AccessImpl;
import database.QueryBuilder;
import database.QueryBuilderImpl;

public class Controller {
    /**
     * Contains bookshelves currently being displayed by the gui or being used
     * by the search program
     */
    public Vector<Bookshelf> modifiedBs;
    /**
     * the network controller
     */
    public Control cntrl;
    /**
     * the rename of a ip or Name for representing connections <IP/name , Alias>
     */
    public HashMap<String, String> connectionAlias;

    public String connectionID = "conID";
    /**
     * the Name or ip address of a connection and its ID <IP/name , id>
     */
    public Map<String, Integer> connectionIds;
    /**
     * the currently focused book shelf used in addBook
     */
    public Bookshelf focus;



    public HashMap<Integer, VirtualLibrary> importedLibs;

    public String libID = "Name";

    public PersistentLibrary myLib;

    /**
     * the united querybuilder for the program
     */
    public QueryBuilder qb;
    public boolean recconectFlag;
    /**
     * the id of a connection and the remotelibrary that represents it
     */
    public Map<Integer, RemoteLibrary> remoteLibs;

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

	cntrl = new ControlImpl(new LibraryResponder(myLib));
	setLibName(myLib ,"kabob");
	ProgramProperties props = ProgramProperties.getInstance();
	modifiedBs = new Vector<Bookshelf>();
	recconectFlag = false;
	connectionIds = new HashMap<String, Integer>();
	connectionAlias = new HashMap<String, String>();
	remoteLibs = new HashMap<Integer, RemoteLibrary>();
	importedLibs = new HashMap<Integer, VirtualLibrary>();

	setupconnections(props);

	// Registering shutdown hooks
	addShutdownHooks();
    }
    
    public void setLibName(Library lib, String name){
	if(lib!=null && name!=null)
	lib.setProperty(libID, name);
    }
    
    

    /**
     * Add a book to the library
     * 
     * @return the added book (null if error)
     */
    public synchronized Book addBook(Book book) throws IllegalArgumentException {
	if (book == null || !(focus instanceof VirtualBookshelf))
	    throw new IllegalArgumentException();
	focus.insert(book);
	myLib.saveBookshelf((VirtualBookshelf) focus);
	return addBook(focus, book);
    }

    /**
     * Add a book to the library
     * 
     * @return the added book (null if error)
     */
    public synchronized Book addBook(Bookshelf bookshelf, Book book) {
	if (book == null || !(bookshelf instanceof VirtualBookshelf))
	    return null;
	bookshelf.insert(book);
	setFocus(bookshelf);
	myLib.saveBookshelf((VirtualBookshelf) bookshelf);
	return book;
    }

    /**
     * Add a book to the library
     * 
     * @return the added book (null if error)
     */
    public synchronized Book addBook(String title, String author)
	    throws IllegalArgumentException {
	Book book = addBook(new VirtualBook(title, author));
	myLib.saveBookshelf((VirtualBookshelf) focus);
	return book;
    }

    /**
     * Add a bookshelf to the library
     * 
     * @return the added bookshelf (null if error)
     */
    public synchronized Bookshelf addBookshelf(Book book) throws IllegalArgumentException {
	if (book == null)
	    throw new IllegalArgumentException();
	VirtualBookshelf bs = new VirtualBookshelf("From book "
		+ book.getProperty("Name"));
	addBook(bs, book);
	myLib.saveBookshelf(bs);
	setFocus(bs);
	return bs;
    }

    /**
     * Add a bookshelf to the library
     * 
     * @return the added bookshelf (null if error)
     */
    public synchronized Bookshelf addBookshelf(String name) throws IllegalArgumentException {
	if (name == null)
	    throw new IllegalArgumentException();
	VirtualBookshelf bs = new VirtualBookshelf(name);
	myLib.saveBookshelf(bs);
	setFocus(bs);
	return bs;
    }

    public synchronized void addConnection(String host) throws IllegalArgumentException,
	    NullPointerException, RemoteObjectException, UnknownHostException,
	    IOException {
	if (host != null) {
	    if (connectionIds.keySet().contains(host)) {
		throw new IllegalArgumentException();
	    }
	    Integer temp;
	    try {
		temp = cntrl.connect(host);
		System.out.println(" im back");
		connectionIds.put(host, temp);
		connectionAlias.put(host, host);
		// testconnection(temp, "Are you still there?");
		RemoteLibrary rl = new RemoteLibrary(temp, cntrl);
		VirtualLibrary vl = new VirtualLibrary();
		vl.setProperty(libID, host);
		vl.setProperty(connectionID, host);
		remoteLibs.put(temp, rl);
		importedLibs.put(temp, vl);
		// TODO remove add all and set up for selective add
		// add change to utilise new structure even though no selective
		// add
		importAllBookshelves(vl, remoteLibs.get(temp));
	    } catch (ConnectException e) {
		System.err.println("connection error with " + host + " reason "
			+ e);
		e.printStackTrace();
	    }
	}
    }

    public synchronized void addConnection(String host, String alias)
	    throws UnknownHostException, IOException, ConnectException,
	    IllegalArgumentException, NullPointerException,
	    RemoteObjectException {
	addConnection(host);
	setConnectionAlias(host, alias);
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
		pp.setProperty("controller::connections", connectionAlias);
		pp.saveProperties();
		Access access = AccessImpl.getInstance();
		access.shutdown();
	    }
	});

    }
    
    
    public synchronized void saveAll(){
	
	Iterator<Bookshelf> iter = modifiedBs.iterator();
	Bookshelf bs;
	while(iter.hasNext()){
	    bs = iter.next();
	    if(bs instanceof VirtualBookshelf)
	    myLib.saveBookshelf((VirtualBookshelf)bs);
	}
    }
    
    
    public synchronized void breakConnection(String host) {
	if (!connectionIds.keySet().contains(host)) {
	    throw new IllegalArgumentException();
	}
	Integer tmp = connectionIds.get(host);
	System.out.println(tmp + " " + host);
	cntrl.disconnect(tmp);
	connectionIds.remove(host);
	connectionAlias.remove(host);
	remoteLibs.remove(tmp);
	importedLibs.remove(tmp);

    }


    public synchronized Set<String> getConnections() {
	return (connectionIds == null) ? null : connectionIds.keySet();
    }

    public synchronized Set<String> getConnectionsAlias() {
	return (connectionAlias == null) ? null : connectionAlias.keySet();
    }

    public synchronized Map<String, String> getConnectionsAliasPairs() {
	return connectionAlias;
    }

    public synchronized void importABookshelves(Library local, Library remote, String select) {
	if (local == null || remote == null) {
	    throw new IllegalArgumentException();
	}
	Bookshelf bs = remote.getBookshelf(select);
	if (bs == null)
	    throw new IllegalArgumentException();
	local.addBookshelf(bs);
    }

    public synchronized void importAllBookshelves(Library local, Library remote) {
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

    public synchronized void importSelectBookshelves(Library local, Library remote,
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

    public synchronized void reconnect(String host) throws UnknownHostException,
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


    /**
     * Remove a book from the library
     * 
     * @return the removed book (null if error)
     */
    public synchronized Book removeBook(Book book) throws IllegalArgumentException {
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
    public synchronized Book removeBook(String name) throws IllegalArgumentException {
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
	if(focus!=null){
	Bookshelf bs = focus;
	modifiedBs.remove(focus);
	if(!myLib.deleteBookshelf((VirtualBookshelf) focus))
	    throw new IllegalArgumentException();
	focus= null;
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
     * Retrieves a vector of all your library's bookshelf names
     * 
     * @return the vector of names
     */
    public synchronized Vector<String> retrieveMyBookshelfNames() {
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
     * returns the collection of the current aliases of the remote libraries
     * 
     * @return the iterator
     */
    public synchronized Collection<String> retrieveRemoteLibraryNames() {
	return connectionAlias.values();
    }
    
    
    public synchronized Map<Integer, VirtualLibrary> retrieveImportedLibsNames() {
	return importedLibs;
    }

    
    /**
     * a method for use in multi term specific area searching ( field to search,
     * searching within)
     * 
     * @param list
     * @return
     */
    public synchronized Bookshelf search(Map<String, Vector<String>> list) {
	return search(list, myLib);
    }
    public synchronized Collection<Bookshelf> searchAll(Map<String, Vector<String>> list) {
	Collection<Entry<Integer,VirtualLibrary>> temp = importedLibs.entrySet();
	Iterator<Entry<Integer, VirtualLibrary>> iter = temp.iterator();
	Vector<Library> libs = new Vector<Library>();
	while(iter.hasNext()){
	    libs.add(iter.next().getValue());
	}
	libs.add(myLib);
	return ControllerSearch.searchAlllibs(list, libs);
    }
    public synchronized Collection<Bookshelf> searchAll(String str) {
	Collection<Entry<Integer,VirtualLibrary>> temp = importedLibs.entrySet();
	Iterator<Entry<Integer, VirtualLibrary>> iter = temp.iterator();
	Vector<Library> libs = new Vector<Library>();
	while(iter.hasNext()){
	    libs.add(iter.next().getValue());
	}
	libs.add(myLib);
	return ControllerSearch.searchAlllibs(str, libs);
    }
    /**
     * a method for use in multi term specific area searching
     * 
     * @param list
     * @return
     */
    public synchronized Bookshelf search(Map<String, Vector<String>> list,
	    Bookshelf bookshelf) {
	return ControllerSearch.search(list, bookshelf);
    }

    public synchronized Bookshelf search(Map<String, Vector<String>> list,
	    Collection<Bookshelf> bookshelves) {
	return ControllerSearch.search(list, bookshelves);
    }

    public synchronized Bookshelf search(Map<String, Vector<String>> list, Library aLib) {
	return ControllerSearch.search(list, aLib);
    }

    /**
     * A search match any criteria with a single string on your local library
     * 
     * @param str
     * @return
     */
    public synchronized Bookshelf search(String str) {
	return ControllerSearch.search(str, myLib);
    }

    /**
     * A search match any criteria with a single string on a bookshelf
     * 
     * @param str
     * @param bookshelf
     * @return
     */
    public synchronized Bookshelf search(String str, Bookshelf bookshelf) {
	return ControllerSearch.search(str, bookshelf);
    }

    public synchronized Bookshelf search(String str, Collection<Bookshelf> bookshelves) {
	return ControllerSearch.search(str, bookshelves);
    }

    /**
     * A search match any criteria with a single string a library
     * 
     * @param str
     * @param aLib
     * @return
     */
    public synchronized Bookshelf search(String str, Library aLib) {
	return ControllerSearch.search(str, aLib);
    }

    public synchronized void setConnectionAlias(String oldAlias, String newalias) {
	if (!connectionAlias.containsValue(oldAlias))
	    throw new IllegalArgumentException();
	Iterator<Entry<String, String>> iter = connectionAlias.entrySet().iterator();
	String host = null;
	while(iter.hasNext()){
		Entry<String, String> ent = iter.next();
		if(ent.getValue().equals(oldAlias)){
			host=ent.getKey();
			connectionAlias.put(ent.getKey(), newalias);
		}
	}
	if(host!=null){
		int i = connectionIds.get(host);
		importedLibs.get(i).setProperty(libID, newalias);		
	}
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
	if(!modifiedBs.contains(bookshelf)){
	    modifiedBs.add(bookshelf);
	}
	focus = bookshelf;
    }
    /**
     * Method for reinitializing or setting up a set of connections if no stored
     * connections it creates an empty set
     * 
     * @param props
     * @throws UnknownHostException
     * @throws IllegalArgumentException
     * @throws NullPointerException
     * @throws ConnectException
     * @throws IOException
     * @throws RemoteObjectException
     */
  //  @SuppressWarnings("unchecked")
    public synchronized void setupconnections(ProgramProperties props)
	    throws UnknownHostException, IllegalArgumentException,
	    NullPointerException, ConnectException, IOException,
	    RemoteObjectException {
	Object o = props.getProperty("controller::connections");
	if (o == null) {
	    recconectFlag = false;
	    connectionIds = new HashMap<String, Integer>();
	    connectionAlias = new HashMap<String, String>();
	    remoteLibs = new HashMap<Integer, RemoteLibrary>();
	    importedLibs = new HashMap<Integer, VirtualLibrary>();
	}
	else if ((o instanceof HashMap<?, ?>)) {
	    recconectFlag = true;
	    connectionIds = new HashMap<String, Integer>();
	    remoteLibs = new HashMap<Integer, RemoteLibrary>();
	    importedLibs = new HashMap<Integer, VirtualLibrary>();

	    HashMap<String, String> tmp = (HashMap<String, String>) o;
	    connectionAlias = tmp;
	    Iterator<Entry<String, String>> iter = tmp.entrySet().iterator();
	    Entry<String, String> e;
	    while (iter.hasNext()) {
		e = iter.next();
		System.out.println(e.getKey());
		reconnect(e.getKey());
	    }
	}
    }

    public void testconnection(Integer target, String message) {
	cntrl.sendMsg(target, new ChatMessage(message));
    }
}
