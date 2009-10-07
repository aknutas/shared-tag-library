package operations;

import data.*;

import java.util.*;

public abstract class BookshelfOperations{
	
	/**
	 * This operation takes a Collection of Bookshelfs and unions
	 * all of them into one Bookshelf.
	 * 
	 * @param shelfs the Collection of Bookshelfs
	 * @return a new Bookshelf containing all of the books of the input.
	 * @throws IllegalArgumentException if collection is/contains a null shelf.
	 */
	
	public static Bookshelf union(Collection<Bookshelf> shelfs) throws IllegalArgumentException{
		
		if (null == shelfs) throw new IllegalArgumentException("collection cannot be null");
		
		VirtualBookshelf newBS = new VirtualBookshelf();
		
		for( Bookshelf s: shelfs ){
			if (null == s) throw new IllegalArgumentException("Collection contains a null shelf");
			newBS.union(s);	
		}
		
		return newBS;
	}

	public static Bookshelf subset(Collection<Bookshelf> shelfs, Bookshelf basis) throws IllegalArgumentException{
		
		if (null == shelfs) throw new IllegalArgumentException("collection cannot be null");
		
		VirtualBookshelf newBS = new VirtualBookshelf();
		VirtualBookshelf temp;
		
		Iterator<Bookshelf> shelfsIt = shelfs.iterator();
		VirtualBookshelfIterator basisBooks; 
		
		if (basis instanceof VirtualBookshelf){
			
			while(shelfsIt.hasNext()){
			
				Bookshelf tempBS = shelfsIt.next();
				Iterator<Book> tempIt = tempBS.enumerate();
				
				while(tempIt.hasNext()){
					
					temp = (VirtualBookshelf) basis;
					basisBooks = (VirtualBookshelfIterator)temp.enumerate();
				
					Book current = null;
					Iterator<Map.Entry<String,Integer>> currentWeights;
				
					while(basisBooks.hasNext()){
					
						current = basisBooks.next();
						currentWeights = current.enumerateTags();
					
						while(currentWeights.hasNext()){
							
											
						
						}
					
					}	
					
				}
				
				
			}
			
			
		}
		
		return null;
	}
	
	
	public static void main(String []args){
		
		try{
			BookshelfOperations.union(null);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		VirtualBookshelf a = new VirtualBookshelf();
		VirtualBookshelf b = new VirtualBookshelf();
		VirtualBookshelf c = new VirtualBookshelf();
		VirtualBookshelf d = new VirtualBookshelf();
		
		a.insert(new VirtualBook("a", "b"));
		a.insert(new VirtualBook("c", "d"));
		a.insert(new VirtualBook("e", "f"));
		a.insert(new VirtualBook("g", "h"));
		a.insert(new VirtualBook("i", "j"));
		a.insert(new VirtualBook("k", "l"));
		
		b.insert(new VirtualBook("m", "n"));
		b.insert(new VirtualBook("o", "p"));
		b.insert(new VirtualBook("q", "r"));
		b.insert(new VirtualBook("s", "t"));
		b.insert(new VirtualBook("u", "v"));
		b.insert(new VirtualBook("w", "x"));
		
		c.insert(new VirtualBook("1", "2"));
		c.insert(new VirtualBook("3", "4"));
		c.insert(new VirtualBook("5", "6"));
		c.insert(new VirtualBook("7", "8"));
		c.insert(new VirtualBook("9", "10"));
		c.insert(new VirtualBook("11", "12"));

		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		
		try{
			Stack<Bookshelf> tmp = new Stack<Bookshelf>();
			tmp.add(a);
			tmp.add(null);
			BookshelfOperations.union(tmp);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		Stack<Bookshelf> stackOfBooks = new Stack<Bookshelf>();
		stackOfBooks.add(a);
		stackOfBooks.add(b);
		
		Bookshelf aUnionB = BookshelfOperations.union(stackOfBooks);
		
		Iterator<Book> itA = a.enumerate();
		Iterator<Book> itB = b.enumerate();
		
		boolean two = true;
		
		while (itA.hasNext()){
			Book temp = itA.next();
			if (two && aUnionB.remove(temp)){}	//do nothing
			else
				two = false;
					
		}
		while (itB.hasNext()){
			Book temp = itB.next();
			if (two && aUnionB.remove(temp)){}	//do nothing
			else
				two = false;
		}
		
		if (!two)
			System.out.println("Size: 2 failed");
		
		
		
	}
}
