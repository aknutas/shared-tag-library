/**
 * Contains Methods that search on all possible varieties of searchable objects
 */
package controller;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import data.Book;
import data.BookQuery;
import data.Bookshelf;
import data.Library;
import data.VirtualBookshelf;

public abstract class ControllerSearch {
    /**
     * Searchs with a string on a collection of libraries
     * 
     * @param searchTerm
     *            the term to search
     * @param allLibs
     *            a collection of libraries
     * @return a shelf for each library
     */
    public static Collection<Bookshelf> searchAlllibs(String searchTerm,
	    Collection<Library> allLibs) {
	Iterator<Library> iter = allLibs.iterator();
	Collection<Bookshelf> shelves = new Vector<Bookshelf>();

	while (iter.hasNext()) {
	    shelves.add(search(searchTerm, iter.next()));
	}
	return shelves;
    }

    /**
     * Searchs with a string on a collection of libraries and flattens the
     * results
     * 
     * @param searchTerm
     *            the term to search
     * @param allLibs
     *            a collection of libraries
     * @return a shelf for each library
     */
    public static Bookshelf searchAlllibsFlat(String searchTerm,
	    Collection<Library> allLibs) {
	Iterator<Library> iter = allLibs.iterator();
	VirtualBookshelf vb = new VirtualBookshelf();
	while (iter.hasNext()) {
	    Bookshelf avb = search(searchTerm, iter.next());
	    vb = (VirtualBookshelf) vb.union(avb);
	}

	return vb;
    }

    /**
     * Searchs with a string on a library and flattens the results
     * 
     * @param searchTerm
     *            the term to search
     * @param aLib
     *            a library
     * @return a shelf for each library
     */
    public static Bookshelf search(String searchTerm, Library aLib) {
	if (searchTerm == null)
	    return null;
	Bookshelf result = new VirtualBookshelf("Search on " + searchTerm);
	BookQuery bq = new BookQuery();
	bq.match(searchTerm);
	Iterator<Bookshelf> iter = aLib.iterator();
	Bookshelf bs;
	while (iter.hasNext()) {
	    bs = iter.next();
	    Iterator<Book> bookiter = bs.iterator();
	    Book book = null;
	    while (bookiter.hasNext()) {
		book = bookiter.next();
		if (bq.compareTo(book) == 0) {
		    result.insert(book);
		}
	    }

	}
	return result;
    }

    public static Bookshelf search(String str, Collection<Bookshelf> bookshelves) {
	if (str == null)
	    return null;
	Bookshelf result = new VirtualBookshelf("Search of " + str);
	BookQuery bq = new BookQuery();
	bq.match(str);
	Iterator<Bookshelf> iter = bookshelves.iterator();
	Bookshelf bs;
	while (iter.hasNext()) {
	    bs = iter.next();
	    Iterator<Book> bookiter = bs.iterator();
	    Book book = null;
	    while (bookiter.hasNext()) {
		book = bookiter.next();
		if (bq.compareTo(book) == 0) {
		    result.insert(book);
		}
	    }
	}
	return result;
    }

    public static Bookshelf search(String str, Bookshelf bookshelf) {
	if (str == null)
	    return null;
	Bookshelf result = new VirtualBookshelf("Search of " + str);
	BookQuery bq = new BookQuery();
	bq.match(str);
	Iterator<Book> bookiter = bookshelf.iterator();
	Book book = null;
	while (bookiter.hasNext()) {
	    book = bookiter.next();
	    if (bq.compareTo(book) == 0) {
		result.insert(book);
	    }
	}
	return result;
    }

    public static Collection<Bookshelf> searchAlllibs(
	    Map<String, Vector<String>> list, Collection<Library> allLibs) {
	Iterator<Library> iter = allLibs.iterator();
	Collection<Bookshelf> shelves = new Vector<Bookshelf>();

	while (iter.hasNext()) {
	    shelves.add(search(list, iter.next()));
	}
	return shelves;
    }

    /**
     * a method for use in multi term specific area searching
     * 
     * @param list
     * @return
     */
    public static Bookshelf search(Map<String, Vector<String>> list,
	    Collection<Bookshelf> bookshelves) {
	if (list == null)
	    return null;
	Bookshelf result = new VirtualBookshelf("Search Result shelf");
	BookQuery bq = createQueryObject(list);

	Iterator<Bookshelf> shelveiter = bookshelves.iterator();
	Bookshelf bs;
	while (shelveiter.hasNext()) {
	    bs = shelveiter.next();
	    Iterator<Book> bsiter = bs.iterator();
	    Book book = null;
	    while (bsiter.hasNext()) {
		book = bsiter.next();
		if (bq.compareTo(book) == 0) {
		    result.insert(book);
		}
	    }

	}
	return result;
    }

    public static Bookshelf search(Map<String, Vector<String>> list,
	    Library aLib) {
	if (list == null)
	    return null;
	Bookshelf result = new VirtualBookshelf("Search Result shelf");
	BookQuery bq = createQueryObject(list);

	Iterator<Bookshelf> libiter = aLib.iterator();
	Bookshelf bs;
	while (libiter.hasNext()) {
	    bs = libiter.next();
	    Iterator<Book> bsiter = bs.iterator();
	    Book book = null;
	    while (bsiter.hasNext()) {
		book = bsiter.next();
		if (bq.compareTo(book) == 0) {
		    result.insert(book);
		}
	    }

	}
	return result;
    }

    /**
     * a method for use in multi term specific area searching
     * 
     * @param list
     * @return
     */
    public static Bookshelf search(Map<String, Vector<String>> list,
	    Bookshelf bookshelf) {
	if (list == null)
	    return null;
	Bookshelf result = new VirtualBookshelf("Search Result shelf");
	BookQuery bq = createQueryObject(list);

	Iterator<Book> bookiter = bookshelf.iterator();
	Book book = null;
	while (bookiter.hasNext()) {
	    book = bookiter.next();
	    if (bq.compareTo(book) == 0) {
		result.insert(book);
	    }
	}
	return result;
    }

    public static BookQuery createQueryObject(Map<String, Vector<String>> list) {
	BookQuery bq = new BookQuery();
	Iterator<Entry<String, Vector<String>>> iter = list.entrySet()
		.iterator();
	while (iter.hasNext()) {
	    Entry<String, Vector<String>> enter = iter.next();
	    Vector<String> v = enter.getValue();
	    for (int i = 0; i < v.size(); i++) {
		if (enter.getKey().equalsIgnoreCase("tag")) {
		    if (v.elementAt(i).indexOf("-") == 0)
			bq.matchTag(v.elementAt(i).substring(1), true);
		    else
			bq.matchTag(v.elementAt(i), false);
		}
		if (v.elementAt(i).indexOf("-") == 0)
		    bq.matchProperty(enter.getKey(), v.elementAt(i)
			    .substring(1), true);
		else
		    bq.matchProperty(enter.getKey(), v.elementAt(i), false);

	    }
	}
	return bq;
    }

}
