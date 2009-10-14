package scripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ScriptGenerator {
	public Vector<String> dictionary;
	private final File dictionaryFile;
	private final File targetFile;
	Random numgen; 
	  public ScriptGenerator(String fileName){
		  dictionary = new Vector<String>();
		  dictionaryFile = new File(fileName);  
		  targetFile = new File("C:\\Users\\Robert\\workspace2\\Tags\\src\\books.txt");
		  numgen = new Random( 32564379 );
	  }

	public static void main (String[] aguments){
		ScriptGenerator sg = new ScriptGenerator("C:\\Users\\Robert\\workspace2\\Tags\\src\\en_US.dic");
		try{
		sg.processLineByLine();
		}
		catch (Exception e){
			System.err.println(e);
		}
		sg.generateLibrary();
		Parser p = new Parser("C:\\Users\\Robert\\workspace2\\Tags\\src\\books.txt");

		try{
			p.processLineByLine();
		}
	    catch (Exception e){
	    	System.err.println(e);
	    }
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
	  
	  public void generateLibrary(){

		  	try{
		  		BufferedWriter output = new BufferedWriter(new FileWriter(targetFile));
			    try {
			      //FileWriter always assumes default encoding is OK!
			    for(int i =0;i<10;i++){
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
			    }
			    finally {
			      output.close();
			    }
		  	}
		  	catch(Exception e){
		  		System.out.println("failed to generate library" +e);
		  	}
	  }
	  private String generateTitle(){
		  String s="TITLE: ";
		  s=s.concat(dictionary.elementAt(numgen.nextInt(dictionary.size())));
		  s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		  s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		  return s;
	  }
	  private String generateAuthor(){
		  String s="AUTHOR: ";
		  String[] honorific = {"Mr. ","Mrs. ","Ms.","Sir "};
		  s=s.concat(honorific[numgen.nextInt(4)]);
		  s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
		  return s;
	  }
	  private String generateGenre(){
		  String s="GENRE: ";
		  String[] genre = {"Horror","Scifi","Fantasy","Romance","Suspense","Mystery","Action","Historical","Non-Fiction","Blimp"};
		  s=s.concat(genre[numgen.nextInt(10)]);
		  return s;
	  }
	  private String generateTags(){
		  String s="TAG: ";
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
