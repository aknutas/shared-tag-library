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
			newBS = (VirtualBookshelf) newBS.union(s);	
		}

		return newBS;
	}

	/**
	 * This operation takes a Collection of Bookshelfs and a basis Book
	 * and returns a new Bookshelf containing books similar to the basis.
	 * 
	 * @param shelfs The Collection of Bookshelfs
	 * @param basis The basis book
	 * @return a new bookshelf containing a subset of Books from the Collection that are similar to the basis.
	 * @throws IllegalArgumentException
	 */
	
	public static Bookshelf subset(Collection<Bookshelf> shelfs, Comparable<Book> basis) throws IllegalArgumentException{

		if (null == shelfs) throw new IllegalArgumentException("collection cannot be null");

		Bookshelf allShelfs = union(shelfs);
		Iterator<Book> allBooks = allShelfs.enumerate();

		return allShelfs.subset(basis);
	}

	/**
	 * if needed
	 * @param shelf
	 * @return
	 */
	private static boolean isVirtual(Bookshelf shelf){

		return shelf instanceof VirtualBookshelf;
	}

	/**
	 * if needed
	 * @param book
	 * @return
	 */
	private static boolean isVirtual(Book book){

		return book instanceof VirtualBook;
	}
	
	public static Iterator<Map.Entry<String, Integer>> enumerateTags(Bookshelf shelf){
		
		Iterator<Book> shelfIt = shelf.enumerate();
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		while (shelfIt.hasNext()){		//each book on the shelf
			
			Book current = shelfIt.next();
			Iterator<Map.Entry<String, Integer>> tags = current.enumerateTags();
			
			while (tags.hasNext()){		//each tag on a book
				Map.Entry<String, Integer> temp = tags.next();
				map.put(temp.getKey(), temp.getValue());
				
			}
			
		}
		return map.entrySet().iterator();
		
	}
	
	public static Iterator<Map.Entry<String, Integer>> enumerateTags(Collection<Bookshelf> shelfs){
		
		Bookshelf newShelf = union(shelfs);
		return enumerateTags(newShelf);
		
	}

	/*	public static void main(String []args){

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
		a.setName("a - l");

		b.insert(new VirtualBook("m", "n"));
		b.insert(new VirtualBook("o", "p"));
		b.insert(new VirtualBook("q", "r"));
		b.insert(new VirtualBook("s", "t"));
		b.insert(new VirtualBook("u", "v"));
		b.insert(new VirtualBook("w", "x"));
		b.setName("m - x");

		c.insert(new VirtualBook("1", "2"));
		c.insert(new VirtualBook("3", "4"));
		c.insert(new VirtualBook("5", "6"));
		c.insert(new VirtualBook("7", "8"));
		c.insert(new VirtualBook("9", "10"));
		c.insert(new VirtualBook("11", "12"));
		c.setName("1 - 12");

		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.insert(new VirtualBook("alpha", "beta"));
		d.setName("alphas and betas");

		Stack<Bookshelf> tmp = new Stack<Bookshelf>();
		tmp.add(a);
		BookshelfOperations.union(tmp);

			List<Bookshelf> stackOfBooks = new Vector<Bookshelf>();
			stackOfBooks.add(a);
			stackOfBooks.add(b);

			Bookshelf aUnionB = BookshelfOperations.union(stackOfBooks);

			Iterator<Book> itA = a.enumerate();
			Iterator<Book> itB = b.enumerate();

			boolean two = true;

			while (two && itA.hasNext()){
				Book temp = itA.next();
				if (aUnionB.contains(temp)){}	//do nothing
				else{
					System.out.println("Failed on A: " + temp.getTitle() + "  " + temp.getAuthor());
					two = false;
				}

			}
			while (two & itB.hasNext()){
				Book temp = itB.next();
				if (aUnionB.contains(temp)){}	//do nothing
				else{
					System.out.println("Failed on B: " + temp.getTitle() + "  " + temp.getAuthor());
					two = false;
				}
			}

			if (!two)
				System.out.println("Size 2: failed");
			else
				System.out.println("Size 2: passed");

			Stack<Bookshelf> stackOfBooks2 = new Stack<Bookshelf>();
			stackOfBooks2.add(a);
			stackOfBooks2.add(b);
			stackOfBooks2.add(c);

			Bookshelf abc = BookshelfOperations.union(stackOfBooks2);

			itA = a.enumerate();
			itB = b.enumerate();
			Iterator<Book> itC = c.enumerate();


			boolean three = true;

			while (three && ( itA.hasNext() | itB.hasNext() | itC.hasNext() )){

				Book tempA;
				Book tempB;
				Book tempC;

				if (itA.hasNext())
				{
					tempA = itA.next();
					if (abc.contains(tempA)){}	//do nothing
					else{
						System.out.println("Failed on: " + tempA.getTitle() + "  " + tempA.getAuthor());
						three = false;
					}
				}
				if (itB.hasNext())
				{
					tempB = itB.next();
					if (abc.contains(tempB)){}	//do nothing
					else{
						System.out.println("Failed on: " + tempB.getTitle() + "  " + tempB.getAuthor());
						three = false;
					}
				}
				if (itC.hasNext())
				{
					tempC = itC.next();
					if (abc.contains(tempC)){}	//do nothing
					else{
						System.out.println("Failed on: " + tempC.getTitle() + "  " + tempC.getAuthor());
						three = false;
					}
				}
			}

			if (!three)
				System.out.println("Size 3: failed");
			else
				System.out.println("Size 3: passed");
		}*/
}
