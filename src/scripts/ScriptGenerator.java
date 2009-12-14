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
    public Vector<String> seedDictionary;
    private final File dictionaryFile;
    private final File targetFile;
    public Boolean importedDictionary;
    Random numgen;

    /**
     * creates a ScriptGenerator for randomly generating a library save file
     * 
     * @param dictionaryLoc
     *            the target dictionary file to be used as the random generation
     * @param fileName
     *            the ouput file
     */
    public ScriptGenerator(String dictionaryLoc, String fileName) {
	seedDictionary = new Vector<String>();
	dictionaryFile = new File(dictionaryLoc);
	targetFile = new File(fileName);
	numgen = new Random(32564379);
	importedDictionary = false;
    }

    /**
     * imports the seed dictionary for random generation
     * 
     * @throws FileNotFoundException
     */
    public void importSeedDictionary() throws FileNotFoundException {
	processLineByLine();
	if (!seedDictionary.isEmpty())
	    importedDictionary = true;
    }

    /**
     * Scanner Method for parsing into seedDictionary
     * 
     * @throws FileNotFoundException
     */
    protected final void processLineByLine() throws FileNotFoundException {
	Scanner scanner = new Scanner(dictionaryFile);
	try {
	    while (scanner.hasNextLine()) {
		processLine(scanner.nextLine());
	    }
	} catch (Exception e) {
	    System.err.println(e);
	} finally {
	    scanner.close();
	}
    }

    /**
     * Scanner Method for parsing into seedDictionary
     * 
     * @throws FileNotFoundException
     */
    protected void processLine(String line) {
	Scanner scanner = new Scanner(line);
	scanner.useDelimiter("/");
	if (scanner.hasNext()) {
	    String aWord = scanner.next();
	    seedDictionary.add(aWord);
	} else {
	    throw new IllegalArgumentException();
	}
	scanner.close();
    }

    /**
     * Method for generation of a library save file at random
     * 
     * @param numbooks
     *            the number of books in the text file
     * @param shelfsize
     *            number of books on a shelf
     */
    public void generateLibrary(int numbooks, int shelfsize) {
	if (!importedDictionary)
	    throw new IllegalArgumentException("Dictionary not Imported");
	try {
	    BufferedWriter output = new BufferedWriter(new FileWriter(
		    targetFile));
	    try {
		for (int i = 0; i < numbooks; i++) {
		    if (i % shelfsize == 0) {
			if (i != 0) {
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
		    output.write(generateTags());
		    output.newLine();
		    output.write("ENDB:");
		    output.newLine();
		}
		output.write("ENDS:");
		output.newLine();
	    } finally {
		output.write("ENDLIB:");
		output.newLine();
		output.close();
	    }
	} catch (Exception e) {
	    System.out.println("failed to generate library" + e);
	}
    }

    /**
     * generates text for a random shelf
     * 
     * @return
     */
    private String generateShelf() {
	String s = "SHELF:";
	s = s.concat("Name");
	s = s.concat(":"
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	return s;
    }

    /**
     * generates text for a random book title
     * 
     * @return
     */
    private String generateTitle() {
	String s = "TITLE:";
	s = s.concat(seedDictionary.elementAt(numgen.nextInt(seedDictionary
		.size())));
	s = s.concat(" "
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	s = s.concat(" "
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	return s;
    }

    /**
     * generates text for a random author
     * 
     * @return
     */
    private String generateAuthor() {
	String s = "AUTHOR:";
	String[] honorific = { "Mr.", "Mrs.", "Ms.", "Sir" };
	s = s.concat(honorific[numgen.nextInt(4)]);
	s = s.concat(" "
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	return s;
    }

    /**
     * generates 6 random tags
     * 
     * @return
     */
    private String generateTags() {
	String s = "TAG:";
	s = s.concat(seedDictionary.elementAt(numgen.nextInt(seedDictionary
		.size())));
	s = s.concat(","
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	s = s.concat(","
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	s = s.concat(","
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	s = s.concat(","
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	s = s.concat(","
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	s = s.concat(","
		+ seedDictionary.elementAt(numgen
			.nextInt(seedDictionary.size())));
	return s;
    }

}
