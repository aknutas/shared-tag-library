package controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import org.junit.Test;

import data.*;

public class ControllerTest {

    @Test
    public void testController() {
	Controller cont;
	try {
	    cont = new Controller();
	    assertTrue(cont.nextID == 1);
	    assertTrue(cont.qb != null);
	    assertTrue(cont.myLib != null);
	    assertTrue(cont.cntrl != null);
	    assertTrue(cont.modifiedBs != null);
	    assertTrue(cont.connectionIds != null);
	    assertTrue(cont.connectionAlias != null);
	    assertTrue(cont.remoteLibs != null);
	    assertTrue(cont.importedLibs != null);
	    
	    if(cont.recconectFlag){
		    assertTrue(!cont.connectionIds.isEmpty());
		    assertTrue(!cont.connectionAlias.isEmpty());
		    assertTrue(!cont.remoteLibs.isEmpty());
		    assertTrue(!cont.importedLibs.isEmpty());
	    }
	} catch (UnknownHostException e) {
	    System.err.println("UnknownHostException at testController " + e);
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    System.err.println("IllegalArgumentException at testController " + e);
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    System.err.println("NullPointerException at testController " + e);
	    e.printStackTrace();
	} catch (IOException e) {
	    System.err.println("IOException at testController " + e);
	    e.printStackTrace();
	} catch (RemoteObjectException e) {
	    System.err.println("RemoteObjectException at testController " + e);
	    e.printStackTrace();
	}
    }

    @Test
    public void testSetFocus() {
	Controller cont;
	Bookshelf bs;
	Bookshelf bs2;
	try {
	    cont = new Controller();
	    bs = cont.addBookshelf("phil");
	    bs2 = cont.focus;
	    assertTrue(cont.focus.getProperty("Name").compareTo("phil")==1);
//	for (Book b : bs)
//	    System.out.println(b.getProperty("title"));

	} catch (UnknownHostException e) {
	    System.err.println("UnknownHostException at testSetFocus " + e);
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    System.err.println("IllegalArgumentException at testSetFocus " + e);
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    System.err.println("NullPointerException at testSetFocus " + e);
	    e.printStackTrace();
	} catch (IOException e) {
	    System.err.println("IOException at testSetFocus " + e);
	    e.printStackTrace();
	} catch (RemoteObjectException e) {
	    System.err.println("RemoteObjectException at testSetFocus " + e);
	    e.printStackTrace();
	}
   }
 //   @Test
//    public void testRetrieveShelf() {
//	Controller cont;
//	Bookshelf bs;
//	Bookshelf bs2;
//	try {
//	    cont = new Controller();
//	    bs = cont.addBookshelf("phil");
//	    Integer i = cont.retrieveShelf("phil");
//	    bs2 =cont.getBs(i);
//	    assertTrue(bs.equals(bs2));	    
//
//	} catch (UnknownHostException e) {
//	    System.err.println("UnknownHostException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (IllegalArgumentException e) {
//	    System.err.println("IllegalArgumentException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (NullPointerException e) {
//	    System.err.println("NullPointerException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (IOException e) {
//	    System.err.println("IOException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (RemoteObjectException e) {
//	    System.err.println("RemoteObjectException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	}
//    }
//    
//    @Test
//    public void testGetBs() {
//	Controller cont;
//	Bookshelf bs;
//	Bookshelf bs2;
//	try {
//	    cont = new Controller();
//	    bs = cont.addBookshelf("phil");
//	    Integer i = cont.retrieveShelf("phil");
//	    bs2 =cont.getBs(i);
//	    assertTrue(bs.equals(bs2));	    
//
//	} catch (UnknownHostException e) {
//	    System.err.println("UnknownHostException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (IllegalArgumentException e) {
//	    System.err.println("IllegalArgumentException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (NullPointerException e) {
//	    System.err.println("NullPointerException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (IOException e) {
//	    System.err.println("IOException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	} catch (RemoteObjectException e) {
//	    System.err.println("RemoteObjectException at testRetrieveShelf " + e);
//	    e.printStackTrace();
//	}
//    }



    @Test
    public void retrieveMyBookshelfNames() {
	Controller cont;

	try {
	    cont = new Controller();
	    cont.addBookshelf("phil");
	    cont.addBookshelf("bug");
	    cont.addBookshelf("taco");
	    Vector<String> comp = new Vector<String>();
	    Vector<String> comp2 = cont.retrieveMyBookshelfNames();
	    comp.add("phil");
	    comp.add("bug");
	    comp.add("taco");
	    assertTrue(comp2.containsAll(comp));	    

	} catch (UnknownHostException e) {
	    System.err.println("UnknownHostException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    System.err.println("IllegalArgumentException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    System.err.println("NullPointerException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (IOException e) {
	    System.err.println("IOException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (RemoteObjectException e) {
	    System.err.println("RemoteObjectException at testRetrieveShelf " + e);
	    e.printStackTrace();
	}

    }

    @Test
    public void testRetrieveLibrary() {
	Controller cont;
	try {
	    Vector<Bookshelf> comp = new Vector<Bookshelf>();
	    cont = new Controller();
	    comp.add(cont.addBookshelf("phil"));
	    comp.add(cont.addBookshelf("bug"));
	    comp.add(cont.addBookshelf("taco"));
	    Iterator<Bookshelf> iter = cont.retrieveLibrary();
	    Vector<Bookshelf> vec = new Vector<Bookshelf>();
	    while(iter.hasNext()){
		vec.add(iter.next());
	    }
	    assertTrue(vec.size()==3);
	    assertTrue(vec.containsAll(comp));
	    

	} catch (UnknownHostException e) {
	    System.err.println("UnknownHostException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    System.err.println("IllegalArgumentException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (NullPointerException e) {
	    System.err.println("NullPointerException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (IOException e) {
	    System.err.println("IOException at testRetrieveShelf " + e);
	    e.printStackTrace();
	} catch (RemoteObjectException e) {
	    System.err.println("RemoteObjectException at testRetrieveShelf " + e);
	    e.printStackTrace();
	}
    }

    @Test
    public void testRetrieveRemoteLibraryNames() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddBookBookshelfStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddBookBookshelfBook() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddBookStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddBookBook() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookBookshelfString() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookString() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookBookshelfBook() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookBook() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddBookshelfString() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddBookshelfBook() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookshelfBookshelfString() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookshelfString() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookshelfBookshelf() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveBookshelfBookshelfBook() {
	fail("Not yet implemented");
    }

    @Test
    public void testInitializeDummyData() {
	fail("Not yet implemented");
    }

    @Test
    public void testRetrieveButlerWeights() {
	fail("Not yet implemented");
    }

    @Test
    public void testSearchString() {
	fail("Not yet implemented");
    }

    @Test
    public void testSearchStringBookshelf() {
	fail("Not yet implemented");
    }

    @Test
    public void testSearchMapOfStringVectorOfString() {
	fail("Not yet implemented");
    }

    @Test
    public void testSearchMapOfStringVectorOfStringBookshelf() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddConnectionString() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddConnectionStringString() {
	fail("Not yet implemented");
    }

    @Test
    public void testBreakConnection() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetConnections() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetConnectionsAlias() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetConnectionsAliasPairs() {
	fail("Not yet implemented");
    }

    @Test
    public void testSetConnectionAlias() {
	fail("Not yet implemented");
    }

    @Test
    public void testImportAllBookshelves() {
	fail("Not yet implemented");
    }

    @Test
    public void testImportSelectBookshelves() {
	fail("Not yet implemented");
    }

    @Test
    public void testImportABookshelves() {
	fail("Not yet implemented");
    }

    @Test
    public void testTestconnection() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveAllBookshelves() {
	fail("Not yet implemented");
    }

    @Test
    public void testMessageHandler() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddShutdownHooks() {
	fail("Not yet implemented");
    }

    @Test
    public void testSetuptestController() {
	fail("Not yet implemented");
    }

}
