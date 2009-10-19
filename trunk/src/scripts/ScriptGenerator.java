package scripts;
import database.*;
import data.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;


/**
 * Creates a Script generator for creating a random library
 * 
 * @author Robert Amundson
 */
public class ScriptGenerator {
	public Vector<String> dictionary;
	private final File dictionaryFile;
	private final File targetFile;
	Random numgen; 
	/**
	 * creates a script generator with a dictionary input
	 * 
	 * 
	 * @param fileName the target dictionary file
	 */
	public ScriptGenerator(String fileName){
		dictionary = new Vector<String>();
		dictionaryFile = new File(fileName);  
		targetFile = new File("src\\scripts\\books.txt");
		numgen = new Random( 32564379 );
	}

	public static void main (String[] aguments){
		ScriptGenerator sg = new ScriptGenerator("src\\scripts\\en_US.dic");
		try{
			sg.processLineByLine();
		}
		catch (Exception e){
			System.err.println("error in s processline Dic "+ e);
		}
		sg.generateLibrary(10,5);
		Parser p = new Parser("src\\scripts\\books.txt");

		try{
			p.processLineByLine();
		}
		catch (Exception e){
			System.err.println("error in s processline books "+ e);
		}
		
		//at this point should contain a fully set up library with
		// shelves containing 5 books each
		Iterable<Bookshelf> booksf = p.lib.enumerateBookshelves();
		Iterator<Bookshelf> iter = booksf.iterator();
		Bookshelf bs;
		while(iter.hasNext()){
			bs = iter.next();
			System.out.println("name: "+bs.getProperty("Name"));
		}
		
		System.out.println("done");		
	}

	public final void processLineByLine() throws FileNotFoundException {
		Scanner scanner = new Scanner(dictionaryFile);
		try {
			while ( scanner.hasNextLine() ){
				processLine( scanner.nextLine() );
			}
		}
		catch (Exception e){
			System.err.println(e);
		}
		finally {
			scanner.close();
		}
	}

	protected void processLine(String line){
		//use a second Scanner to parse the content of each line 
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter("/");
		if ( scanner.hasNext() ){
			String aWord = scanner.next();
			dictionary.add(aWord);
		}
		else {
			throw new IllegalArgumentException();
		}
		//(no need for finally here, since String is source)
		scanner.close();
	}
/**
 * 
 * @param numbooks the number of books in the text file
 * @param shelfsize number of books on a shelf
 */
	public void generateLibrary(int numbooks,int shelfsize){

		try{
			BufferedWriter output = new BufferedWriter(new FileWriter(targetFile));
			try {
				//FileWriter always assumes default encoding is OK!
				for(int i =0;i<numbooks;i++){
					if(i%shelfsize==0){
						if(i!=0){
						output.write("ENDS:");
						output.newLine();
						}
						output.write(generateShelf());						
						output.newLine();
					}
					output.write(generateTitle());
					output.newLine();
					output.write(generateAuthor());
					output.newLine();
					output.write(generateGenre());
					output.newLine();
					output.write(generateTags());
					output.newLine();
					output.write("ENDB:");
					output.newLine();
				}
				output.write("ENDS:");
				output.newLine();
			}
			finally {
				output.close();
			}
		}
		catch(Exception e){
			System.out.println("failed to generate library" +e);
		}
	}
	private String generateShelf(){
		String s="SHELF:";
		s=s.concat("Name");
		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		return s;
	}
	private String generateTitle(){
		String s="TITLE:";
		s=s.concat(dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		return s;
	}
	private String generateAuthor(){
		String s="AUTHOR:";
		String[] honorific = {"Mr.","Mrs.","Ms.","Sir"};
		s=s.concat(honorific[numgen.nextInt(4)]);
		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		return s;
	}
	private String generateGenre(){
		String s="GENRE:";
		String[] genre = {"Horror","Scifi","Fantasy","Romance","Suspense","Mystery","Action","Historical","Non-Fiction","Blimp"};
		s=s.concat(genre[numgen.nextInt(10)]);
		return s;
	}
	private String generateTags(){
		String s="TAG:";
		s=s.concat(dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		return s;
	}


}
