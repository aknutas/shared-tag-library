package controller;

import data.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import scripts.Parser;
import scripts.ScriptGenerator;




public class Controller {

	public Library myLib;
	/**
	 * Contains bookshelves currently being displayed by the gui or
	 * being used by the search program
	 */
	private Map<Integer,Bookshelf> checkedOutBs;
	/**
	 * Contains bookshelves currently being displayed by the gui or
	 * being used by the search program
	 */
	private Map<Integer,Book> checkedOutBooks;
	
	
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
		checkedOutBooks = new HashMap<Integer,Book>();
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
		
		myLib = sg.p.lib;
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
	 * Used for accessing a books that have been retrieved.
	 * @param key the unique key handed to the module for retrieving the Book
	 * @return the bookshelf if the key is valid
	 * @throws IllegalArgumentException if the key is not in the map
	 */
	public Book getBook(Integer key) throws IllegalArgumentException{
		if(!checkedOutBooks.containsKey(key)){
			throw new IllegalArgumentException();
		}
		else
		return checkedOutBooks.get(key);
	}
	/**
	 * request a bookshelf from the library or connection
	 * @param loc
	 * @return the key to reference the the object with the get command
	 */
	public Integer retrieveShelf(String loc){
		Integer num = nextID;


		Iterator<Bookshelf> iter = ((ClientLibrary)myLib).iterator();
		Bookshelf bs;
		while(iter.hasNext()){
			bs = iter.next();
			if(bs.getProperty("Name")==loc){
				checkedOutBs.put(num, bs);
			}
		}
		nextID++;
		return num;
	}
	/**
	 * request a book from the library or connection
	 * @param loc
	 * @return the key to reference the the object with the get command
	 */
	public Integer retrieveBook(String loc){
		
		Integer num = nextID;
		
		return num;
	}
	

	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(Bookshelf bookshelf, String name){
	    return null;
	}
	
	
	/**
	 * Add a book to the library
	 * 
	 * @return the added book (null if error)
	 */
	public Book addBook(Bookshelf bookshelf, Book book){
	    return null;
	}
	
	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Bookshelf bookshelf, String name){
	    return null;
	}
	
	
	/**
	 * Remove a book from the library
	 * 
	 * @return the removed book (null if error)
	 */
	public Book removeBook(Bookshelf bookshelf, Book book){
	    return null;
	}
	
	
	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public Book addBookshelf(Bookshelf bookshelf, String name){
	    return null;
	}
	
	
	/**
	 * Add a bookshelf to the library
	 * 
	 * @return the added bookshelf (null if error)
	 */
	public Book addBookshelf(Bookshelf bookshelf, Book book){
	    return null;
	}
	
	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Book removeBookshelf(Bookshelf bookshelf, String name){
	    return null;
	}
	
	
	/**
	 * Remove a bookshelf from the library
	 * 
	 * @return the removed bookshelf (null if error)
	 */
	public Book removeBookshelf(Bookshelf bookshelf, Book book){
	    return null;
	}
	/*
	If you need any methods add a quick stub and description and ill get to it
	this is just an early version more to follow	
	
	
*/
}
