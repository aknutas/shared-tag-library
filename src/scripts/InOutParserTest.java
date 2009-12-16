package scripts;

import static org.junit.Assert.*;

import org.junit.Test;

import data.Book;
import data.Bookshelf;
import data.Library;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;

public class InOutParserTest {

    @Test
    public void testInOutParser() {
	InOutParser inOut = new InOutParser("src\\scripts\\input.txt",
		"src\\scripts\\output.txt");
	assertTrue(inOut.lib != null);
	assertTrue(!inOut.parseFlag);
    }

    @Test
    public void testProcessLineByLine() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetParsedLib() {
	fail("Not yet implemented");
    }

    @Test
    public void testProcessLine() {
	fail("Not yet implemented");
    }

    @Test
    public void testWriteOutLibrary() {
	Library lib = new VirtualLibrary();
	Bookshelf bs = new VirtualBookshelf("frank");
	bs.insert(new VirtualBook("abook", "by a man"));
	lib.addBookshelf(bs);
	InOutParser inOut = new InOutParser("src\\scripts\\input.txt",
		"src\\scripts\\output.txt");
	inOut.writeOutLibrary(lib);
	assertTrue(inOut.outLib != null);
    }

}
