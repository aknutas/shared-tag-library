package butler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import data.Book;
import data.Bookshelf;

/**
 * The FlatShelf class unexpectedly implements Book. A FlatShelf can be thought of as
 * taking all of the books on a VirtualBookshelf and combining the tag information together.
 * Flattening a shelf is a one-way operation. It it not possible to convert from a FlatShelf
 * back to a Bookshelf. Also, flattening a shelf loses all information about individual books.
 * @author sjpurol
 *
 */
public final class FlatShelf implements Book {

	Map<String, Integer> tags;
	Map<String, String> properties;

	/**
	 * Creates a new FlatShelf from the given VirtualBookshelf
	 * @param shelf
	 */
	public FlatShelf(Bookshelf shelf) {

		tags = new HashMap<String, Integer>();
		properties = new HashMap<String, String>();

		for (Book b : shelf) {

			Iterator<Map.Entry<String, String>> bookProps = b.enumerateProperties();

			while (bookProps.hasNext()) {
				Map.Entry<String, String> prop = bookProps.next();
				//System.out.println(prop.getKey() + ": " + prop.getValue());
				if (!tags.containsKey(prop.getValue()))
					tags.put(prop.getValue(), 1);
				else
					tags.put(prop.getValue(), (tags.get(prop.getValue()) + 1));
			}

			Iterator<Map.Entry<String, Integer>> bookTags = b.enumerateTags();

			while (bookTags.hasNext()) {
				Map.Entry<String, Integer> tag = bookTags.next();
				if (!tags.containsKey(tag))
					tags.put(tag.getKey(), tag.getValue());
				else
					tags.put(tag.getKey(), (tags.get(tag.getKey()) + tag.getValue()));
			}

		}

		Iterator<Map.Entry<String, String>> shelfProps = shelf.enumerateProperties();

		while (shelfProps.hasNext()) {
			Map.Entry<String, String> prop = shelfProps.next();
			properties.put(prop.getKey(), prop.getValue());
		}

	}
	
	public int getWeight(String tag) {return tags.get(tag);}

	@Override
	public Iterator<Entry<String, String>> enumerateProperties() {return properties.entrySet().iterator();}

	@Override
	public String getProperty(String name) throws NullPointerException {return properties.get(name);}

	/**
	 * Not valid for a FlatShelf. Returns null.
	 */
	@Override
	public String setProperty(String name, String value) throws NullPointerException {return null;}

	@Override
	public Iterator<Entry<String, Integer>> enumerateTags() {return tags.entrySet().iterator();}

	@Override
	public int getTagCount() {return tags.size();}

	/**
	 * Not valid for a FlatShelf. Returns 0.
	 */
	@Override
	public int tag(String tag) throws NullPointerException {return 0;}

	/**
	 * Not valid for a FlatShelf. Returns 0.
	 */
	@Override
	public int untag(String tag) throws NullPointerException {return 0;}

	/**
	 * Not valid for a FlatShelf. Returns 0.
	 */
	@Override
	public int weight(String tag) throws NullPointerException {return 0;}

}
