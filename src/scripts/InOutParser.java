package scripts;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;

import scripts.Parser.TagType;
import data.Book;
import data.Bookshelf;
import data.Library;
import data.PersistentLibrary;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;
import database.QueryBuilderImpl;

public class InOutParser {

    private final File inFile;
    private final File outFile;

    private String bookTitle = "";
    private String bookAuthor = "";
    private Book abook;
    private Bookshelf ashelf;
    public Library lib;
    public Library outLib;
    public boolean parseFlag;

    /**
     * a Parser that will handle File io of the library
     * 
     * @param inputFile
     * @param outputFile
     */
    public InOutParser(String inputFile, String outputFile) {
	parseFlag = false;

	inFile = (inputFile == null) ? null : new File(inputFile);
	outFile = (outputFile == null) ? null : new File(outputFile);
	lib = new VirtualLibrary();
    }

    /**
     * a Parser that will handle File io of the library
     * 
     * @param inputFile
     * @param outputFile
     */
    public InOutParser(File inputFile, File outputFile) {
	parseFlag = false;
	inFile = inputFile;
	outFile = outputFile;
	lib = new VirtualLibrary();
    }

    /**
     * The method for reading in a library from the inFile
     * 
     * @throws FileNotFoundException
     */
    public final void processLineByLine() throws FileNotFoundException {
	if (inFile == null) {
	    throw new FileNotFoundException();
	}
	Scanner scanner = new Scanner(inFile);
	try {
	    while (scanner.hasNextLine()) {
		processLine(scanner.nextLine());
	    }
	} finally {
	    scanner.close();
	}
    }

    /**
     * returns the library if it has been parsed in
     * 
     * @return
     */
    public Library getParsedLib() {
	if (parseFlag)
	    return lib;
	return null;
    }

    /**
     * the rules for parsing a single line
     * 
     * @param line
     */
    protected void processLine(String line) {
	// use a second Scanner to parse the content of each line
	Scanner scanner = new Scanner(line);
	scanner.useDelimiter("[:,]");
	if (scanner.hasNext()) {
	    String regex = scanner.next();
	    switch (TagType.toType(regex)) {
	    case TITLE:
		bookTitle = scanner.next();
		break;
	    case AUTHOR:
		bookAuthor = scanner.next();
		abook = new VirtualBook(bookTitle, bookAuthor);
		break;
	    case TAG:
		while (scanner.hasNext() && abook != null) {

		    String tag = scanner.next();
		    String nums = scanner.next();
		    if (tag.equals(bookAuthor) || tag.equals(bookTitle))
			abook.untag(tag);
		    int num = Integer.valueOf(nums);
		    System.out.println(tag + "  " + num);
		    for (int i = 0; i < num; i++)
			abook.tag(tag);
		}
		break;
	    case SHELF:
		ashelf = new VirtualBookshelf();
		String str;
		str = scanner.next();
		ashelf.setProperty(str, scanner.next());
		break;
	    case ENDB:
		ashelf.insert(abook);
		abook = null;
		break;
	    // end of book tag
	    // put book on shelf and set null
	    case ENDS:
		lib.addBookshelf(ashelf);
		ashelf = null;
		break;
	    // end shelf add to library
	    case ENDLIB:
		parseFlag = true;
		break;
	    default:
		System.out.println("  " + regex
			+ ".  Empty or invalid line. Unable to process.");
		break;
	    }
	} else {
	    System.out.println("Empty or invalid line. Unable to process.");
	}
	scanner.close();
    }

    /**
     * writes out a the given library to the outfile
     * 
     * @param aLib
     */
    public void writeOutLibrary(Library aLib) {
	if (aLib != null)
	    outLib = aLib;
	else
	    throw new NullPointerException("Bad Library");
	try {
	    BufferedWriter output = new BufferedWriter(new FileWriter(outFile));
	    try {
		System.out.println("entered try catch");
		// FileWriter always assumes default encoding is OK!
		Iterator<Bookshelf> iter = outLib.iterator();
		try {
		    while (iter.hasNext()) {
			System.out.println("bs loop");
			Bookshelf bs = iter.next();
			writeShelf(bs, output);
			Iterator<Book> biter = bs.iterator();
			try {
			    while (biter.hasNext()) {
				System.out.println("book loop");
				Book book = biter.next();
				writeTitle(book, output);
				writeAuthor(book, output);
				writeTags(book, output);
				output.write("ENDB:");
				output.newLine();
			    }
			} catch (Exception e) {
			    System.out
				    .println("failed to generate library in book"
					    + e);
			}
			output.write("ENDS:");
			output.newLine();
		    }
		    output.write("ENDLIB:");
		    output.newLine();
		} catch (Exception e) {
		    System.out.println("failed to generate library in bs" + e);
		}
	    } finally {
		output.flush();
		output.close();
		System.out.println("output closed");
	    }
	} catch (Exception e) {
	    System.out.println("failed to generate library" + e);
	}
	System.out.println("Generated library");

    }

    /**
     * internal method preping first half of a shelf
     * 
     * @param bs
     * @param output
     * @throws IOException
     */
    private void writeShelf(Bookshelf bs, BufferedWriter output)
	    throws IOException {
	String s = "SHELF:";
	s = s.concat("name");
	s = s.concat(":"
		+ ((bs.getProperty("name") != null) ? bs.getProperty("name")
			: "noname"));
	output.write(s);
	output.newLine();
    }

    /**
     * writes the title for a book
     * 
     * @param book
     * @param output
     * @throws IOException
     */
    private void writeTitle(Book book, BufferedWriter output)
	    throws IOException {
	String title = "TITLE:";
	title = title.concat(((book.getProperty("title") != null) ? book
		.getProperty("title") : "notitle"));
	output.write(title);
	output.newLine();
    }

    /**
     * writes the author of a book
     * 
     * @param book
     * @param output
     * @throws IOException
     */
    private void writeAuthor(Book book, BufferedWriter output)
	    throws IOException {
	String author = "AUTHOR:";
	author = author.concat(((book.getProperty("author") != null) ? book
		.getProperty("author") : "noauthor"));
	output.write(author);
	output.newLine();
    }

    /**
     * writes out the tags
     * 
     * @param book
     * @param output
     * @throws IOException
     */
    private void writeTags(Book book, BufferedWriter output) throws IOException {
	String tags = "TAG:";

	for (Entry<String, Integer> ent : book.enumerateTags())
	    tags = tags.concat(ent.getKey() + "," + ent.getValue() + ",");

	tags.substring(0, tags.length() - 1);
	output.write(tags);
	output.newLine();
    }

    /**
     * the tags for parsing a library
     * 
     * @author Robert
     * 
     */
    public enum TagType {
	TITLE, AUTHOR, TAG, NOVALUE, ENDB, SHELF, ENDS, ENDLIB;
	public static TagType toType(String str) {
	    try {
		return valueOf(str);
	    } catch (Exception ex) {
		return NOVALUE;
	    }
	}
    }

}
