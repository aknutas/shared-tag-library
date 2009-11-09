package operations;

import data.*;

import java.util.*;

public abstract class LibraryOperations {
	
	static int numTags = 0;
	static int numProps = 0;
	static int numBooks = 0;

	public static Iterator<Map.Entry<String, Integer>> getTags(Library lib){
		
		//System.out.println("getTags(lib)");
		
		Bookshelf masterShelf = lib.getMasterShelf();
		
		numTags = 0;
		numBooks = 0;
		//System.out.println("master has " + masterShelf.size() + " books.");
		
		Iterator<Book> master = masterShelf.iterator();
		Map<String, Integer> allTags = new HashMap<String, Integer>();
		
		//System.out.println("master.hasNext(): " + master.hasNext());
		
		while (master.hasNext()){
			++numBooks;
			//System.out.println("Master has next.");
			Book current = master.next();
			Iterator<Map.Entry<String, Integer>> tags = current.enumerateTags();
			while (tags.hasNext()){
				++numTags;
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

		numProps = 0;
		numBooks = 0;
		Bookshelf masterShelf = lib.getMasterShelf();
		
		//System.out.println("master has " + masterShelf.size() + " books.");
		
		Iterator<Book> master = masterShelf.iterator();
		Map<String, String> allProperties = new HashMap<String, String>();
		
		while (master.hasNext()){
			++numBooks;
			//System.out.println("Master has next.");
			Book current = master.next();
			Iterator<Map.Entry<String, String>> props = current.enumerateProperties();
			while (props.hasNext()){
				++numProps;
				Map.Entry<String, String> prop = props.next();
				allProperties.put(prop.getKey(),prop.getValue());

				//System.out.println("Props: " + prop.getKey() + " : " + prop.getValue());
			}
			
		}
		
		return allProperties.entrySet().iterator();
		
	}
	
	public static int getNumTags() {return numTags;}
	
	public static int getNumProps() {return numProps;}
	
	public static int getNumBooks() {return numBooks;}
	
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
