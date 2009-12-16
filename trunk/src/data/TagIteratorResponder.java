package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * The TagIteratorResponder class extends the IteratorResponder and is used to
 * respond to RemoteTagIterator objects.
 * 
 * @author Andrew alm
 */
public class TagIteratorResponder extends IteratorResponder<Entry<String, Integer>> {

	/**
	 * Creates a new TagIteratorResponder from the given iterator.
	 * 
	 * @param iter the Iterator to use
	 * 
	 * @throws NullPointerException if the iter given is null
	 */
	public TagIteratorResponder(Iterator<Entry<String, Integer>> iter) throws NullPointerException {
		super(iter);
	}

	/**
	 * This converts an Entry object into a RemoteEntry object, which
	 * is guaranteed to be Serializable.
	 * 
	 * @param object the object to serialize
	 * 
	 * @return a Serializable object
	 * 
	 * @throws NullPointerException if the object given is null
	 */
	@Override
	public Serializable serializeObject(Entry<String, Integer> object) throws NullPointerException {
		if(null == object)
			throw new NullPointerException("object cannot be null");
		
		return new RemoteEntry<String, Integer>(object.getKey(), object.getValue());
	}

}
