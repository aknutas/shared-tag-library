package operations;

import data.*;

import java.util.Iterator;
import java.util.Map;

public abstract class BookOperations {
	
	public Iterator<Map.Entry<String, Double>> normalizeTags(VirtualBook b) {
		
		return null;
	}

	/**
	 * if needed
	 * @param book
	 * @return
	 */
	private static boolean isVirtual(Book book){

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
