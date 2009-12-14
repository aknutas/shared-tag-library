package operations;

import data.*;

import java.util.Iterator;
import java.util.Map;

/**
 * Contains 
 * @author sjpurol
 *
 */
public abstract class BookOperations {
	
	/**
	 * if needed
	 * @param book
	 * @return
	 */
	public static boolean isVirtual(Book book){

		return book instanceof VirtualBook;
	}
	
	public static int countTags(Book book){
		
		Iterator<Map.Entry<String, Integer>> tags = book.enumerateTags();
		int num = 0;
		while (tags.hasNext()){
			++num;
			tags.next();
		}
		
		return num;
	}

	public static int countProperties(Book book){
		int num = 0;
		
		for(Map.Entry<String, String> tag : book.enumerateProperties())
			++num;
		
		return num;
	}
	
}
