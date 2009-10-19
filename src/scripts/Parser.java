package scripts;

import database.*;
import data.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

	private final File bookFile;
	private String bookTitle ="";
	private String bookGenre ="";
	private String bookTag ="";
	private String bookAuthor ="";
	private Book b;
	private Bookshelf s;
	public Library lib;
	//	private Book book;


	public Parser(String File){
		bookFile = new File(File);
		lib = new ClientLibrary();
	}

	public final void processLineByLine() throws FileNotFoundException {
		Scanner scanner = new Scanner(bookFile);
		int o = 0;
		try {
			while ( scanner.hasNextLine() ){
				processLine( scanner.nextLine() );
			}
		}
		finally {
			scanner.close();
		}
	}
	protected void processLine(String line){
		//use a second Scanner to parse the content of each line 
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("[: ,]");
		if ( scanner.hasNext() ){
			String regex = scanner.next();




			switch (TagType.toType(regex))
			{
			case TITLE:
				bookTitle="";
				while(scanner.hasNext()){
					bookTitle=bookTitle.concat(scanner.next()+ " ");
				}
				
				break;
			case AUTHOR:
				bookAuthor="";
				while(scanner.hasNext()){
					bookAuthor=bookAuthor.concat(scanner.next());
				}
				b = new VirtualBook(bookTitle,bookAuthor);
				break;
			case GENRE:
				bookGenre="";
			while(scanner.hasNext()){
					bookGenre=bookGenre.concat(scanner.next());
	
					//to be replaced in property upgrade
				}
				b.setProperty("Genre",bookGenre);
				break;
			case TAG:
				bookTag="";
				while(scanner.hasNext()){
					b.tag(scanner.next());
					//to be replaced in property upgrade
				}
				break;
			case SHELF:
				s=new VirtualBookshelf();
				String str;
				str=scanner.next();
				s.setProperty(str, scanner.next());
				break;
			case ENDB:
				s.insert(b);
				b=null;
				break;
				//end of book tag
				//put book on shelf and set null
			case ENDS:
				lib.addBookshelf(s);
				s=null;
				break;
				//end shelf add to library

			default:
				System.out.println("  " + regex +  ".  Empty or invalid line. Unable to process.");
				break;
			}



		}
		else {
			System.out.println("Empty or invalid line. Unable to process.");
		}
		scanner.close();
	}




	public enum TagType
	{
		TITLE, ISBN, AUTHOR, GENRE, 
		YEAR, TAG, NOVALUE, ENDB, SHELF, ENDS;

		public static TagType toType(String str)
		{
			try {
				return valueOf(str);
			} 
			catch (Exception ex) {
				return NOVALUE;
			}
		} 
	}



}
