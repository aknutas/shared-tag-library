package operations;

import data.*;

public abstract class BookshelfOperations{

	public static Bookshelf union(Bookshelf[] shelfs) throws IllegalArgumentException{
		
		if (null == shelfs)
			throw new IllegalArgumentException("cannot union a null collection.");
		
		VirtualBookshelf newBS = null;
		
		for( int i = 1; i < shelfs.length; ++i ){
			
			if (null == shelfs[i]){
				String error = "Collection contains a null shelf: " + i;
				throw new IllegalArgumentException(error);
			}
			
			newBS = (VirtualBookshelf) shelfs[0].union(shelfs[i]);
			
		}
		
		if (null == newBS)
			return null;
		else
			return newBS;
	}
	
	public static Bookshelf intersect(Bookshelf[] shelfs) throws IllegalArgumentException{
		
		return null;
	}
}
