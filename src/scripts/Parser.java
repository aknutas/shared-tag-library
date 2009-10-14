package scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parser {

	private final File bookFile;
	private String bookTitle ="";
	private String bookGenre ="";
	private String bookTag ="";
	private String bookAuthor ="";
	//	private Book book;


	public Parser(String File){
		bookFile = new File(File);
	}

	public final void processLineByLine() throws FileNotFoundException {
		Scanner scanner = new Scanner(bookFile);
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
				//book = new VirtualBook(bookTitle,bookAuthor);
				break;
			case GENRE:
				bookGenre="";
				while(scanner.hasNext()){
					bookGenre=bookGenre.concat(scanner.next());
					//book.setProperty("Genre",scanner.next());
					//to be replaced in property upgrade
				}
				break;
			case TAG:
				bookTag="";
				while(scanner.hasNext()){
					bookTag=bookTag.concat(scanner.next()+" ");
					//book.tag(scanner.next());
					//to be replaced in property upgrade
				}
				break;
			case ENDB:
				System.out.println("Title: " + bookTitle);
				System.out.println("bookAuthor: " + bookAuthor);
				System.out.println("bookGenre: " + bookGenre);
				System.out.println("bookTag: " + bookTag);
				break;
				//end of book tag
				//put book on shelf and set null
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
		YEAR, TAG, NOVALUE, ENDB;

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
