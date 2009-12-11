package operations;

import data.*;

import java.util.*;

public abstract class BookshelfOperations{

	private static int numBooks;
	private static int numTags;

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

		Bookshelf newBS = new VirtualBookshelf();

		int i = 0;
		int currentSize = 0;

		for( Bookshelf s: shelfs ){
			//System.out.println("Shelf " + i);
			//System.out.println("   has " + s.size() + " books.");
			currentSize += s.size();
			if (null == s) throw new IllegalArgumentException("Collection contains a null shelf");
			newBS = newBS.union(s);
			i++;
		}

		//System.out.println("Total there are " + currentSize + " books.");

		return newBS;
	}
	/**
	 * This operation takes a Collection of Bookshelfs and unions
	 * all of them into one Bookshelf.
	 * 
	 * @param shelfs the Collection of Bookshelfs
	 * @return a new Bookshelf containing all of the books of the input.
	 * @throws IllegalArgumentException if collection is/contains a null shelf.
	 */
	public static VirtualBookshelf virtualUnion(Collection<VirtualBookshelf> shelfs) throws IllegalArgumentException{

		if (null == shelfs) throw new IllegalArgumentException("collection cannot be null");

		VirtualBookshelf newBS = new VirtualBookshelf();

		int i = 0;
		int currentSize = 0;

		for( VirtualBookshelf s: shelfs ){
			//System.out.println("Shelf " + i);
			//System.out.println("   has " + s.size() + " books.");
			currentSize += s.size();
			if (null == s) throw new IllegalArgumentException("Collection contains a null shelf");
			newBS = (VirtualBookshelf)newBS.union(s);
			i++;
		}

		//System.out.println("Total there are " + currentSize + " books.");

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
	 * Returns an iterator of all of the tags on the given bookshelf. (not sorted)
	 * @param shelf the input bookshelf
	 * @return see description
	 */
	public static Iterator<Map.Entry<String, Integer>> enumerateTags(Bookshelf shelf){

		numBooks = 0;
		numTags = 0;

		Iterator<Book> shelfIt = shelf.iterator();
		Map<String, Integer> map = new HashMap<String, Integer>();

		while (shelfIt.hasNext()){		//each book on the shelf
			Book current = shelfIt.next();
			Iterator<Map.Entry<String, Integer>> tags = current.enumerateTags();
			++numBooks;

			while (tags.hasNext()){		//each tag on a book
				Map.Entry<String, Integer> temp = tags.next();
				map.put(temp.getKey(), temp.getValue());
				++numTags;

			}

		}
		return map.entrySet().iterator();

	}

	/**
	 * Returns an iterator containing the first num tags on a bookshelf (sorted by weight).
	 * 
	 * @param shelf the input bookshelf
	 * @param num the number of tags
	 * @return see description
	 */
	public static Iterator<Map.Entry<String, Integer>> enumerateTags(Bookshelf shelf, int num){

		numBooks = 0;
		numTags = 0;

		Iterator<Book> shelfIt = shelf.iterator();
		ArrayList<Map.Entry<String, Integer>> tagList = new ArrayList<Map.Entry<String, Integer>>();
		Map<String, Integer> map = new HashMap<String, Integer>();


		while (shelfIt.hasNext()){		//each book on the shelf
			Book current = shelfIt.next();
			Iterator<Map.Entry<String, Integer>> tags = current.enumerateTags();
			++numBooks;

			while (tags.hasNext()){		//each tag on a book
				Map.Entry<String, Integer> temp = tags.next();

				if (map.containsKey(temp.getKey()))	//a shelf's tags are the sum of its books' tags
					map.put(temp.getKey(), temp.getValue() + map.remove(temp.getKey()));
				else
					map.put(temp.getKey(), temp.getValue());
				++numTags;
			}
		}


		int i = 0;

		tagList.addAll(map.entrySet());

		do {

		} while (i < num);

		return mergeSort(tagList).iterator();

	}

	private static List<Map.Entry<String, Integer>> mergeSort(List<Map.Entry<String, Integer>> list){

		int size = list.size();
		int half = size/2;

		if (size <= 1)
			return list;
		else {
			List<Map.Entry<String, Integer>> list1 = list.subList(0, half-1);
			List<Map.Entry<String, Integer>> list2 = list.subList(half, size-1);
			list1 = mergeSort(list1);
			list2 = mergeSort(list2);
			return merge(list1, list2);
		}
	}

	private static List<Map.Entry<String, Integer>> merge(List<Map.Entry<String, Integer>> list1, List<Map.Entry<String, Integer>> list2){

		List<Map.Entry<String, Integer>> mergedList = new ArrayList<Map.Entry<String, Integer>>();

		int i = 0;	//index for list1
		int j = 0;	//index for list2

		Map.Entry<String, Integer> tag1;
		Map.Entry<String, Integer> tag2;
		
		while (i < list1.size() && j < list2.size()){	//increment through the lists until the end of one.
			
			tag1 = list1.get(i);
			tag2 = list2.get(j);

			if (tag1.getValue() >= tag2.getValue()){
				mergedList.add(tag1);
				++i;
			}
			else{
				mergedList.add(tag2);
				++j;
			}
		}
		
		//only 1 of these while loops should execute. 
		
		while (i < list1.size()){	//if list1 is not empty, merge the remaining tags
			tag1 = list1.get(i);
			mergedList.add(tag1);
			++i;
		}
		
		while (j < list2.size()){	//if list2 is not empty, merge the remaining tags
			tag2 = list2.get(j);
			mergedList.add(tag2);
			++j;
		}

		return mergedList;
	}

	/**
	 * Returns the number of books in the given VirtualBookshelf.
	 * @return see description
	 */
	public static int countBooks(VirtualBookshelf shelf){
		
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext()){
			++num;
			books.next();
		}
		
		return num;
	}

	/**
	 * Returns the number of tags in the given shelf.
	 * @return see description
	 */
	public static int countTags(VirtualBookshelf shelf){
		
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext())
			num += BookOperations.countTags(books.next());
		
		return num;
	}
	
	/**
	 * Returns the number of properties in the given collection of shelfs
	 * @return see description
	 */
	public static int countProperties(VirtualBookshelf shelf){
		
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext())
			num += BookOperations.countProperties(books.next());
		
		return num;
	}

	/**
	 * Returns the number of books in the given collection of shelfs.
	 * @return see description
	 */
	public static int countBooks(Collection<Bookshelf> shelfs){
		
		Bookshelf shelf = union(shelfs);
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext()){
			++num;
			books.next();
		}
		
		return num;
	}

	/**
	 * Returns the number of tags in the given collection of shelfs.
	 * @return see description
	 */
	public static int countTags(Collection<Bookshelf> shelfs){
		
		Bookshelf shelf = union(shelfs);
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext())
			num += BookOperations.countTags(books.next());
		
		return num;
	}
	
	/**
	 * Returns the number of properties in the given collection of shelfs
	 * @return see description
	 */
	public static int countProperties(Collection<Bookshelf> shelfs){
		
		Bookshelf shelf = union(shelfs);
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext())
			num += BookOperations.countProperties(books.next());
		
		return num;
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
