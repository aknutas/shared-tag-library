package controller;

import data.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import scripts.Parser;
import scripts.ScriptGenerator;

import org.joone.engine.Matrix;




public class Controller {

	public ClientLibrary myLib;
	/**
	 * Contains bookshelves currently being displayed by the gui or
	 * being used by the search program
	 */
	private Map<Integer,Bookshelf> checkedOutBs;

	public Integer nextID;
	
	/**
	 * the default controller constructor
	 * 
	 * all things that need to be initialized on startup should be here
	 */
	public Controller(){
		//load in library
		myLib = new ClientLibrary();
		nextID =0;
		//load the the previous state of the gui if we need it
		checkedOutBs = new HashMap<Integer,Bookshelf>();
	}
	
	
	public void setuptestController(){
		ScriptGenerator sg = new ScriptGenerator("src\\scripts\\en_US.dic");
		try{
			sg.processLineByLine();
		}
		catch (Exception e){
			System.err.println("error in s processline Dic "+ e);
		}
		sg.generateLibrary(10,5);
		sg.p = new Parser("src\\scripts\\books.txt");

		try{
			sg.p.processLineByLine();
		}
		catch (Exception e){
			System.err.println("error in s processline books "+ e);
		}
		
		myLib = (ClientLibrary)sg.p.lib;
	}
	
	
	/**
	 * Used for accessing a bookshelf that have been retrieved.
	 * @param key the unique key handed to the module for retrieving the BS
	 * @return the bookshelf if the key is valid
	 * @throws IllegalArgumentException if the key is not in the map
	 */
	public Bookshelf getBs(Integer key) throws IllegalArgumentException{
		if(!checkedOutBs.containsKey(key)){
			throw new IllegalArgumentException();
		}
		else
		return checkedOutBs.get(key);
	}
	/**
	 * request a bookshelf from the library or connection
	 * @param loc
	 * @return the key to reference the the object with the get command
	 */
	public Integer retrieveShelf(String loc){
		Integer num = nextID;
		Iterator<Bookshelf> iter = myLib.iterator();
		Bookshelf bs;
		while(iter.hasNext()){
			bs = iter.next();
			if(bs.getProperty("Name")==loc){
				checkedOutBs.put(num, bs);
				break;
			}
		}
		nextID++;
		return num;
	}

	
	public Vector<String> retrieveLibrary(String loc){
		Iterator<Bookshelf> iter = myLib.iterator();
		Vector<String> names = new Vector<String>();
		Bookshelf bs;
		while(iter.hasNext()){
			bs = iter.next();
			names.add(bs.getProperty("Name"));
		}
		return names;
	}
	

	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(Bookshelf bookshelf, String name, String title){
		if(name== null || title == null)
		    return null;
		return addBook(bookshelf, new VirtualBook(name,title));
	}
	
	
	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(Bookshelf bookshelf, Book book){
		if(book== null)
		    return null;
		bookshelf.insert(book);
		return book;
	}
	
	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Bookshelf bookshelf, String name){
		if(name== null )
		    return null;
		Iterator<Book> iter = bookshelf.iterator();
		Book book = null;
		while(iter.hasNext()){
			book = iter.next();
			if(book.getProperty("Name")==name){
				removeBook(bookshelf,book);
				break;
			}
		}
		return book;
	}
	
	
	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Bookshelf bookshelf, Book book){
		if(!bookshelf.contains(book))
		    return null;
		bookshelf.remove(book);
		return book;
	}
	
	
	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public Bookshelf addBookshelf(Bookshelf bookshelf, String name)throws IllegalArgumentException{
		Bookshelf  bs= new VirtualBookshelf(name);
		myLib.addBookshelf(bs);
	    return bs;
	}
	
	
	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public Bookshelf addBookshelf(Bookshelf bookshelf, Book book){
		Bookshelf  bs= new VirtualBookshelf("From book " + book.getProperty("Name"));
		addBook(bs,book);
		myLib.addBookshelf(bs);
	    return bs;	
	    }
	
	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(Bookshelf bookshelf, String name){
		if(bookshelf== null)
			return removeBookshelf(name);
		else 
			return removeBookshelf(bookshelf);
	}
	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(String name){
		if(name== null)
			return null;
		
			
		
		
	    return null;
	}
	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(Bookshelf bookshelf){
	    return null;
	}

	
	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf removeBookshelf(Bookshelf bookshelf, Book book){
	    return null;
	}
	
	
	/**
	 * Initialize a bookshelf with dummy data entries
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Bookshelf initializeDummyData(){
	    return null;
	}
	
	/*
	If you need any methods add a quick stub and description and ill get to it
	this is just an early version more to follow	
	 */
	
	/**
	 * Returns the set of bias Matrixs for the butler(s) that were stored in the DB.
	 * ( Antti, can you "make room" in the database to store this as well? :-D )
	 *
	 * 
	 * @return a stored Butler
	 */
	public Collection<Matrix> retrieveButler(){
		return null;
	}
}
