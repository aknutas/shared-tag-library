package operations;

import data.*;

import java.util.*;

public abstract class LibraryOperations {
	
	public static Iterator<Map.Entry<String, Integer>> getTags(Library lib){
		
		//System.out.println("getTags(lib)");
		
		Bookshelf masterShelf = lib.getMasterShelf();
		
		//System.out.println("master has " + masterShelf.size() + " books.");
		
		Iterator<Book> master = masterShelf.iterator();
		Map<String, Integer> allTags = new HashMap<String, Integer>();
		
		//System.out.println("master.hasNext(): " + master.hasNext());
		
		while (master.hasNext()){
			//System.out.println("Master has next.");
			Book current = master.next();
			Iterator<Map.Entry<String, Integer>> tags = current.enumerateTags();
			while (tags.hasNext()){
				Map.Entry<String,Integer> tag = tags.next();
				if (allTags.containsKey(tag.getKey())) {
					Integer old = allTags.remove(tag.getKey());
					allTags.put(tag.getKey(), old + tag.getValue());
				}
				else
					allTags.put(tag.getKey(), tag.getValue());
				
				//System.out.println("Tagging: " + tag.getKey() + " : " + tag.getValue());
				
			}
			
		}
		
		return allTags.entrySet().iterator();
	}
	
	public static Iterator<Map.Entry<String, String>> getProperties(Library lib){

		Bookshelf masterShelf = lib.getMasterShelf();
		
		//System.out.println("master has " + masterShelf.size() + " books.");
		
		Iterator<Book> master = masterShelf.iterator();
		Map<String, String> allProperties = new HashMap<String, String>();
		
		while (master.hasNext()) {
			//System.out.println("Master has next.");
			Book current = master.next();
			
			for(Map.Entry<String, String> prop : current.enumerateProperties())
				allProperties.put(prop.getKey(), prop.getValue());
		}
		
		return allProperties.entrySet().iterator();
		
	}

	/**
	 * Returns the number of books in the given VirtualLibrary.
	 * @return see description
	 */
	public static int countBooks(VirtualLibrary lib){
		
		Bookshelf shelf = lib.getMasterShelf();
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext()){
			++num;
			books.next();
		}
		
		return num;
	}

	/**
	 * Returns the number of tags in the given VirtualLibrary.
	 * @return see description
	 */
	public static int countTags(VirtualLibrary lib){

		Bookshelf shelf = lib.getMasterShelf();
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext())
			num += BookOperations.countTags(books.next());
		
		return num;
	}
	
	/**
	 * Returns the number of properties in the given VirtualLibrary.
	 * @return see description
	 */
	public static int countProperties(VirtualLibrary lib){

		Bookshelf shelf = lib.getMasterShelf();
		Iterator<Book> books = shelf.iterator();
		int num = 0;
		while (books.hasNext())
			num += BookOperations.countProperties(books.next());
		
		return num;
	}
	
	public static void main(String []args){
		
		Library lib = new VirtualLibrary();
		Bookshelf bs = new VirtualBookshelf();
		
		Book green = new VirtualBook("Green Eggs and Ham", "Dr. Seuss");
		green.tag("children");
		green.tag("silly");
		green.tag("green");
		green.tag("eggs");
		green.tag("ham");
		bs.insert(green);
		
		Book cat = new VirtualBook("The Cat in the Hat", "Dr. Seuss");
		cat.tag("children");
		cat.tag("cat");
		cat.tag("hat");
		bs.insert(cat);
		
		Book fish = new VirtualBook("One Fish Two Fish Red Fish Blue Fish", "Dr. Seuss");
		fish.tag("fish");
		fish.tag("red");
		fish.tag("blue");
		fish.tag("children");
		bs.insert(fish);
		
		lib.addBookshelf(bs);
		
		Iterator<Map.Entry<String, Integer>> tags = getTags(lib);
		Iterator<Map.Entry<String, String>> props = getProperties(lib);
		
		while (tags.hasNext()){
			Map.Entry<String,Integer> tag = tags.next();
			System.out.println(tag.getKey());
		}
	}
}
