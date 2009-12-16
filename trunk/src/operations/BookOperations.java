package operations;

import data.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Contains
 * 
 * @author sjpurol
 * 
 */
public abstract class BookOperations {

    /**
     * if needed
     * 
     * @param book
     * @return
     */
    public static boolean isVirtual(Book book) {

	return book instanceof VirtualBook;
    }

    public static int countTags(Book book) {
	int num = 0;

	for (Map.Entry<String, Integer> tag : book.enumerateTags())
	    ++num;

	return num;
    }

    public static int countProperties(Book book) {
	int num = 0;

	for (Map.Entry<String, String> tag : book.enumerateProperties())
	    ++num;

	return num;
    }

}
