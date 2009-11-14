package operations;

import data.*;

import java.util.Iterator;

public abstract class BookOperations {

	/**
	 * if needed
	 * @param book
	 * @return
	 */
	private static boolean isVirtual(Book book){

		return book instanceof VirtualBook;
	}
	
}
