package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * The PropertyIteratorResponder class extends the IteratorResponder class for
 * Entry<String, String>. It is used to respond to RemotePropertyIterator
 * objects.
 * 
 * @author Andrew Alm
 */
public class PropertyIteratorResponder extends
	IteratorResponder<Entry<String, String>> {

    /**
     * Creates a new PropertyIteratorResponder from the given iter.
     * 
     * @param iter
     *            the Iterator object to use
     * 
     * @throws NullPointerException
     *             if the iter given is null
     */
    public PropertyIteratorResponder(Iterator<Entry<String, String>> iter)
	    throws NullPointerException {
	super(iter);
    }

    /**
     * This method serializes an Entry into a RemoteEntry in order to ensure it
     * can be serialized.
     * 
     * @param object
     *            the Entry to serialize
     * 
     * @return a Serializable object
     * 
     * @throws NullPointerException
     *             if the object given is null
     */
    @Override
    public Serializable serializeObject(Entry<String, String> object)
	    throws NullPointerException {
	if (null == object)
	    throw new NullPointerException("object cannot be null");

	return new RemoteEntry<String, String>(object.getKey(), object
		.getValue());
    }

}
