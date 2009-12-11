//package scripts;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileWriter;
//import java.util.Iterator;
//import java.util.Scanner;
//
//import scripts.Parser.TagType;
//import data.Book;
//import data.Bookshelf;
//import data.PersistentLibrary;
//import data.VirtualBook;
//import data.VirtualBookshelf;
//import database.QueryBuilderImpl;
//
//public class InOutParser {
//
//	private final File inFile;
//	private final File outFile;
//
//	private String bookTitle = "";
//	private String bookAuthor = "";
//	private Book b;
//	private Bookshelf s;
//	public PersistentLibrary lib;
//	// private Book book;
//	private boolean parseFlag;
//
//	public InOutParser(String inputFile,String outputFile) {
//		parseFlag = false;
//		
//		inFile = (inputFile != null) ? null : new File(inputFile);
//		outFile =(outputFile != null) ? null : new File(outputFile);
//		lib = new PersistentLibrary(new QueryBuilderImpl());
//	}
//	
//	public final void processLineByLine() throws FileNotFoundException {
//		if(inFile==null){
//			throw new FileNotFoundException();
//		}
//		Scanner scanner = new Scanner(inFile);
//		int o = 0;
//		try {
//			while (scanner.hasNextLine()) {
//				processLine(scanner.nextLine());
//			}
//		} finally {
//			scanner.close();
//		}
//	}
//	public PersistentLibrary getParsedLib(){
//		if(parseFlag)
//			return lib;
//		return null;
//	}
//	protected void processLine(String line) {
//		// use a second Scanner to parse the content of each line
//		Scanner scanner = new Scanner(line);
//		scanner.useDelimiter("[: ,]");
//		if (scanner.hasNext()) {
//			String regex = scanner.next();
//			switch (TagType.toType(regex)) {
//			case TITLE:
//				bookTitle = "";
//				while (scanner.hasNext()) {
//					bookTitle = bookTitle.concat(scanner.next() + " ");
//				}
//				break;
//			case AUTHOR:
//				bookAuthor = "";
//				while (scanner.hasNext()) {
//					bookAuthor = bookAuthor.concat(scanner.next());
//				}
//				b = new VirtualBook(bookTitle, bookAuthor);
//				break;
//			case TAG:
//				while (scanner.hasNext() && b != null) {
//					b.tag(scanner.next());
//				}
//				break;
//			case SHELF:
//				s = new VirtualBookshelf();
//				String str;
//				str = scanner.next();
//				s.setProperty(str, scanner.next());
//				break;
//			case ENDB:
//				s.insert(b);
//				b = null;
//				break;
//			// end of book tag
//			// put book on shelf and set null
//			case ENDS:
//				lib.addBookshelf(s);
//				s = null;
//				break;
//			// end shelf add to library
//			case ENDLIB:
//				parseFlag = true;
//				break;
//			default:
//				System.out.println("  " + regex
//						+ ".  Empty or invalid line. Unable to process.");
//				break;
//			}
//		} else {
//			System.out.println("Empty or invalid line. Unable to process.");
//		}
//		scanner.close();
//	}
//	public void writeOutLibrary(PersistentLibrary aLib){
//		if(aLib!=null)
//			lib=aLib;
//		else
//			throw new NullPointerException();
//		try{
//			BufferedWriter output = new BufferedWriter(new FileWriter(outFile));
//			try {
//				//FileWriter always assumes default encoding is OK!
//				Iterator<Bookshelf> iter = lib.iterator();
//				while(iter.hasNext()){
//					Bookshelf bs= iter.next();
//					output.write(generateShelf(bs.getProperty("Name")));						
//					output.newLine();
//					Iterator<Book> biter = bs.iterator();
//					while(biter.hasNext()){
//						Book book = biter.next();
//						
//						
//					}
//				}
//				for(int i =0;i<numbooks;i++){
//					if(i%shelfsize==0){
//						if(i!=0){
//						output.write("ENDS:");
//						output.newLine();
//						}
//
//					}
//					output.write(generateTitle());
//					output.newLine();
//					output.write(generateAuthor());
//					output.newLine();
//					output.write(generateGenre());
//					output.newLine();
//					output.write(generateTags());
//					output.newLine();
//					output.write("ENDB:");
//					output.newLine();
//				}
//				output.write("ENDS:");
//				output.newLine();
//			}
//			finally {
//				output.close();
//			}
//		}
//		catch(Exception e){
//			System.out.println("failed to generate library" +e);
//		}
//	}
//	private String generateShelf(String name){
//		String s="SHELF:";
//		s=s.concat("Name");
//		s=s.concat(" " + name);
//		return s;
//	}
//	private String generateTitle(){
//		String s="TITLE:";
//		s=s.concat(dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		return s;
//	}
//	private String generateAuthor(){
//		String s="AUTHOR:";
//		String[] honorific = {"Mr.","Mrs.","Ms.","Sir"};
//		s=s.concat(honorific[numgen.nextInt(4)]);
//		s=s.concat(" " + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		return s;
//	}
//	private String generateGenre(){
//		String s="GENRE:";
//		String[] genre = {"Horror","Scifi","Fantasy","Romance","Suspense","Mystery","Action","Historical","Non-Fiction","Blimp"};
//		s=s.concat(genre[numgen.nextInt(10)]);
//		return s;
//	}
//	private String generateTags(){
//		String s="TAG:";
//		s=s.concat(dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		s=s.concat("," + dictionary.elementAt(numgen.nextInt(dictionary.size())));
//		return s;
//	}
//	public enum TagType {
//		TITLE, AUTHOR, TAG, NOVALUE, ENDB, SHELF, ENDS, ENDLIB;
//		public static TagType toType(String str) {
//			try {
//				return valueOf(str);
//			} catch (Exception ex) {
//				return NOVALUE;
//			}
//		}
//	}
//
//
//
//
//}
