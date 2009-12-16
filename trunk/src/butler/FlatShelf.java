package butler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jdo.annotations.PersistenceCapable;

import data.Book;
import data.Bookshelf;
import data.VirtualBook;

/**
 * The FlatShelf class unexpectedly implements Book. A FlatShelf is obtained
 * bytaking all of the books on a VirtualBookshelf and combining the tag
 * information together. Flattening a shelf is a one-way operation. It it not
 * possible to convert from a FlatShelf back to a Bookshelf. Also, flattening a
 * shelf loses all information about individual books.
 * 
 * @author sjpurol
 * 
 */
@PersistenceCapable(detachable = "true")
public final class FlatShelf implements Book {

	Map<String, Integer> tags;
	Map<String, String> properties;

	/**
	 * Creates the special empty FlatShelf.
	 */
	public FlatShelf() {
		properties = new HashMap<String, String>();
		properties.put("name", "__empty__");
	}

	/**
	 * Creates a new FlatShelf from the given VirtualBookshelf
	 * 
	 * @param shelf
	 */
	public FlatShelf(Bookshelf shelf) {

		tags = new HashMap<String, Integer>();
		properties = new HashMap<String, String>();

		for (Book b : shelf) {

			// Iterator<Map.Entry<String, String>> bookProps =
			// b.enumerateProperties();

			for (Map.Entry<String, String> prop : b.enumerateProperties()) {
				// Map.Entry<String, String> prop = bookProps.next();
				// System.out.println(prop.getKey() + ": " + prop.getValue());
				if (!tags.containsKey(prop.getValue()))
					tags.put(prop.getValue(), 1);
				else
					tags.put(prop.getValue(), (tags.get(prop.getValue()) + 1));
			}

			Iterator<Map.Entry<String, Integer>> bookTags = b.enumerateTags()
					.iterator();

			while (bookTags.hasNext()) {
				Map.Entry<String, Integer> tag = bookTags.next();
				if (!tags.containsKey(tag))
					tags.put(tag.getKey(), tag.getValue());
				else
					tags.put(tag.getKey(), (tags.get(tag.getKey()) + tag
							.getValue()));
			}

		}

		for (Map.Entry<String, String> prop : shelf.enumerateProperties())
			properties.put(prop.getKey(), prop.getValue());
	}

	@Override
	public Collection<Entry<String, String>> enumerateProperties() {
		return properties.entrySet();
	}

	@Override
	public String getProperty(String name) throws NullPointerException {
		return properties.get(name);
	}

	/**
	 * Not valid for a FlatShelf. Returns null.
	 */
	@Override
	public String setProperty(String name, String value)
			throws NullPointerException {
		return null;
	}

	@Override
	public Iterable<Entry<String, Integer>> enumerateTags() {
		return tags.entrySet();
	}

	@Override
	public int getTagCount() {
		return tags.size();
	}

	/**
	 * Not valid for a FlatShelf. Returns 0.
	 */
	@Override
	public int tag(String tag) throws NullPointerException {
		return 0;
	}

	/**
	 * Not valid for a FlatShelf. Returns 0.
	 */
	@Override
	public int untag(String tag) throws NullPointerException {
		return 0;
	}

	@Override
	public int weight(String tag) throws NullPointerException {

		if (null == tag)
			throw new NullPointerException("tag cannot be null.");

		return tags.get(tag);
	}

	/**
	 * Not valid for a FlatShelf. Returns null.
	 */
	@Override
	public VirtualBook makeVirtual() {
		return null;
	}

}
