package operations;

import data.*;

import java.util.*;

public abstract class LibraryOperations {

	public static Iterator<Map.Entry<String, Integer>> getTags(Library lib){
		
		Iterator<Book> master = lib.getMasterShelf().iterator();
		Map<String, Integer> allTags = new HashMap<String, Integer>();
		
		while (master.hasNext()){
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
			}
			
		}
		
		return allTags.entrySet().iterator();
	}
	
	public static Iterator<Map.Entry<String, String>> getProperties(Library lib){
		
		Iterator<Book> master = lib.getMasterShelf().iterator();
		Set<Map.Entry<String, String>> allProperties = new TreeSet<Map.Entry<String, String>>();
		
		while (master.hasNext()){
			Book current = master.next();
			Iterator<Map.Entry<String, String>> props = current.enumerateProperties();
			while (props.hasNext()){
				Map.Entry<String, String> prop = props.next();
				allProperties.add(prop);
			}
		}
		
		return allProperties.iterator();
		
	}
}
