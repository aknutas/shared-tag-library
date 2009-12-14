package data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * The BookQuery class implements the Comparable interface for Book objects. It
 * is used to construct constraints for the subset operation. A Book will match
 * a BookQuery if it contains at least one of the criteria and does not contain
 * any of the constraints passed in as inverse.
 * 
 * @author Andrew Alm
 */
public final class BookQuery implements Comparable<Book> {

	private Collection<Comparable<String>> tagComparables;
	private Collection<Comparable<Entry<String, String>>> propertyComparables;
	private Collection<Comparable<Book>> bookComparables;

	/* building these patterns frequently could be costly... */
	private Map<String, Pattern> regexpCache;
	
	/**
	 * Creates a new BookQuery with no constraints. A BookQuery with
	 * no constraints will match no books.
	 */
	public BookQuery() {
		this.tagComparables = new LinkedList<Comparable<String>>();
		this.propertyComparables = new LinkedList<Comparable<Entry<String, String>>>();
		this.bookComparables = new LinkedList<Comparable<Book>>();
		
		this.regexpCache = new HashMap<String, Pattern>();
	}
	
	/**
	 * Creates a new BookQuery which uses the given comparable as a
	 * base. This is equivalent to a copy constructor for Comparables
	 * that are instances of BookQuery.
	 *  
	 * @param query the comparable to base this BookQuery from.
	 */
	public BookQuery(Comparable<Book> comparable) {
		this();
		
		if(comparable instanceof BookQuery)
			((BookQuery)comparable).deepCopyInto(this);
		else
			this.and(comparable);
	}
	
	/**
	 * Deep copies all Comparable objects in this BookQuery object
	 * into the given BookQuery using private methods.
	 * 
	 * @param query the BookQuery to copy into.
	 * 
	 * @throws NullPointerException if the query given is null.
	 */
	private void deepCopyInto(BookQuery query) throws NullPointerException {
		if(null == query)
			throw new NullPointerException("query cannot be null");
		
		/* copy book comparables (from 'and') */
		for(Comparable<Book> bookComparable : this.bookComparables)
			query.and(bookComparable);
		
		/* copy tag comparables */
		for(Comparable<String> tagComparable : this.tagComparables)
			query.addTagComparable(tagComparable);
		
		/* copy property comparables */
		for(Comparable<Entry<String, String>> propertyComparable : this.propertyComparables)
			query.addPropertyComparable(propertyComparable);
	}
	
	/**
	 * Adds an 'and' constraint to the BookQuery object. An 'and'
	 * constraint is a Comparable that must match in order for a Book
	 * to match this BookQuery. 
	 * 
	 * @param comparable the comparable to 'and' with.
	 * 
	 * @throws NullPointerException if the comparable given is null.
	 */
	public void and(Comparable<Book> comparable) throws NullPointerException {
		if(null == comparable)
			throw new NullPointerException("comparable cannot be null");
		
		this.bookComparables.add(comparable);
	}

	/**
	 * Adds a 'match' constraint to the BookQuery object. A 'match' 
	 * constraint is a regular expression that is matched against
	 * each tag and property name/value in a Book object.
	 *  
	 * @param regexp the regular expression to use.
	 * 
	 * @throws NullPointerException if the regexp given is null
	 */
	public void match(String regexp) throws NullPointerException, IllegalArgumentException {
		this.match(regexp, false);		
	}
	
	/**
	 * Adds a 'match' constraint to the BookQuery object. A 'match' 
	 * constraint is a regular expression that is matched against
	 * each tag and property name/value in a Book object. If invert 
	 * is true then matching the regular expression will prohibit the
	 * Book from matching the BookQuery.
	 *  
	 * @param regexp the regular expression to use.
	 * @param invert inverse of the regular expression.
	 * 
	 * @throws NullPointerException if the regexp given is null
	 */
	public void match(final String regexp, final boolean invert) throws NullPointerException, IllegalArgumentException {
		this.matchTag(regexp, invert);
		this.matchProperty(regexp, null, invert);
		this.matchProperty(null, regexp, invert);
	}
	
	/**
	 * Adds a 'tag' constraint to the BookQuery object. A 'tag' 
	 * constraint is a regular expression that is matched against
	 * each tag in a Book object.
	 *  
	 * @param regexp the regular expression to use.
	 * 
	 * @throws NullPointerException if the regexp given is null
	 */
	public void matchTag(String regexp) {
		this.matchTag(regexp, false);
	}
	
	/**
	 * Adds a 'tag' constraint to the BookQuery object. A 'tag' 
	 * constraint is a regular expression that is matched against
	 * each tag in a Book object. If invert is true then matching the
	 * regular expression will prohibit the Book from matching the
	 * BookQuery.
	 *  
	 * @param regexp the regular expression to use.
	 * @param invert inverse of the regular expression.
	 * 
	 * @throws NullPointerException if the regexp given is null
	 */
	public void matchTag(final String regexp, final boolean invert) throws NullPointerException {
		if(null == regexp)
			throw new NullPointerException("regexp cannot be null");
		
		final Pattern pattern = this.createRegexp(regexp);
		this.addTagComparable(new Comparable<String>() {
			public int compareTo(String tag) {
				if(null == tag)
					return 1;
				
				return (pattern.matcher(tag).matches()) ? (invert ? -1 : 0) : 1;
			}
		});
	}
	
	/**
	 * Inserts the given Comparable into the Tag comparable list.
	 *  
	 * @param comparable the Comparable to add to the 
	 */
	private void addTagComparable(Comparable<String> comparable) throws NullPointerException {
		if(null == comparable)
			throw new NullPointerException("comparable cannot be null");
		
		this.tagComparables.add(comparable);
	}

	/**
	 * Adds a 'property-name' constraint to the BookQuery object. A 
	 * 'property-name' constraint is a regular expression that is 
	 * matched against each property name.
	 * 
	 *  @param regexp the regexp to use
	 *  
	 *  @throws NullPointerException if the regexp given is null
	 */
	public void matchPropertyName(String regexp) {
		this.matchPropertyName(regexp, false);
	}
	
	/**
	 * Adds a 'property-name' constraint to the BookQuery object. A 
	 * 'property-name' constraint is a regular expression that is 
	 * matched against each property name. If invert is true then 
	 * matching the regular expression(s) will prohibit the Book from
	 * matching the BookQuery.
	 * 
	 *  @param regexp the regexp to use
	 *  @param invert inverse of the regular expression
	 *  
	 *  @throws NullPointerException if the regexp given is null
	 */
	public void matchPropertyName(String regexp, boolean invert) {
		this.matchProperty(regexp, null, invert);
	}
	
	/**
	 * Adds a 'property-value' constraint to the BookQuery object. A 
	 * 'property-value' constraint is a regular expression that is 
	 * matched against each property value.
	 * 
	 *  @param regexp the regexp to use
	 *  
	 *  @throws NullPointerException if the regexp given is null
	 */
	public void matchPropertyValue(String regexp) {
		this.matchPropertyValue(regexp, false);
	}
	
	/**
	 * Adds a 'property-value' constraint to the BookQuery object. A 
	 * 'property-value' constraint is a regular expression that is 
	 * matched against each property value. If invert is true then 
	 * matching the regular expression(s) will prohibit the Book from
	 * matching the BookQuery.
	 * 
	 *  @param regexp the regexp to use
	 *  @param invert inverse of the regular expression
	 *  
	 *  @throws NullPointerException if the regexp given is null
	 */
	public void matchPropertyValue(String regexp, boolean invert) {
		this.matchProperty(null, regexp, false);
	}
	
	/**
	 * Adds a 'property' constraint to the BookQuery object. A 
	 * 'query' constraint is a regular expression that is matched
	 * against each property name and/or value. Either name or value
	 * (but not both) maybe be null, and wont be considered in the
	 * match. If invert is true then matching the regular 
	 * expression(s) will prohibit the Book from matching the
	 * BookQuery.
	 * 
	 *  @param nameRegexp the regular expression to use for the name
	 *         of the property
	 *  @param valueRegexp the regular expression to use for the
	 *         value of the property
	 *  @param invert inverse of the regular expression
	 *  
	 *  @throws NullPointerException if the regexp given is null
	 */
	public void matchProperty(final String nameRegexp, final String valueRegexp, final boolean invert) {
		if((null == nameRegexp) && (null == valueRegexp))
			throw new NullPointerException("nameRegexp and valueRegexp cannot be null");
		
		final Pattern namePattern = (null != nameRegexp) ? this.createRegexp(nameRegexp) : null;
		final Pattern valuePattern = (null != valueRegexp) ? this.createRegexp(valueRegexp) : null;
		
		
		this.addPropertyComparable(new Comparable<Entry<String, String>>() {
			public int compareTo(Entry<String, String> property) {
				if((null == property.getKey()) || (null == property.getValue()))
					return 1;
				
				boolean nameMatch = (null == namePattern) || namePattern.matcher(property.getKey()).matches();
				boolean valueMatch = (null == valuePattern) || valuePattern.matcher(property.getValue()).matches(); 
				
				return (nameMatch && valueMatch) ? (invert ? -1 : 0) : 1;
			}
		});
	}
	
	/**
	 * Inserts a property comparable into the BookQuery object.
	 * 
	 * @param comparable the comparable to add.
	 * 
	 * @throws NullPointerException if the comparable give is null.
	 */
	private void addPropertyComparable(Comparable<Entry<String, String>> comparable) throws NullPointerException {
		if(null == comparable)
			throw new NullPointerException("comparable cannot be null");
		
		this.propertyComparables.add(comparable);
	}
	
	/**
	 * This method creates (or retrieves from cache) the Pattern that
	 * represents the regexp String given.
	 * 
	 * @param regexp the regexp to create.
	 * 
	 * @return the Pattern for the given regexp
	 */
	private Pattern createRegexp(String regexp) {
		Pattern pattern = this.regexpCache.get(regexp);
		
		if(null == pattern) {
			try {
				pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
				this.regexpCache.put(regexp, pattern);
			}
			catch(PatternSyntaxException ex) {
				throw new IllegalArgumentException("invalid regular nameRegexpexpression");
			}
		}
		
		return pattern;
	}
	
	/**
	 * Determines whether the given Book matches the criteria for
	 * this BookQuery.
	 * 
	 * @param book the Book to check
	 * 
	 * @returns 0 if the book matches the criteria of this book
	 *          query, otherwise -1 or 1
	 */
	@Override
	public int compareTo(Book book) {
		Iterator<Entry<String, Integer>> tagIter = book.enumerateTags();
		Iterator<Entry<String, String>> propertyIter = book.enumerateProperties().iterator();
		
		boolean match = false;

		for(Comparable<Book> comparable : this.bookComparables) {
			if(!comparable.equals(book))
				return -1;
		}
		
		while(tagIter.hasNext() || propertyIter.hasNext()) {
			Entry<String, Integer> tag = (tagIter.hasNext() ? tagIter.next() : null);
			Entry<String, String> property = (propertyIter.hasNext() ? propertyIter.next() : null);
			
			int compareTag = (null != tag) ? this.compare(this.tagComparables, tag.getKey()) : 1;
			int compareProperty = (null != property) ? this.compare(this.propertyComparables, property) : 1;
			
			if((-1 == compareTag) || (-1 == compareProperty))
				return -1;
			
			match = match || ((0 == compareTag) || (0 == compareProperty));				
		}
		
		return match ? 0 : 1;
	}
	
	/**
	 * Determines whether the given value matches the collection of 
	 * Comparables given. In order to match no comparable may return
	 * -1 and at least one comparable must return 0.
	 *  
	 * @param <T> the type of Objects being compared
	 * @param comparables the Comparable objects to compare the value
	 *        with
	 * @param value the value to compare with
	 * 
	 * @return -1: match (invert => true), 0: match, 1: no match
	 * 
	 * @throws NullPointerException if the comparables or value given
	 *         are null
	 */
	private <T> int compare(Iterable<Comparable<T>> comparables, T value) throws NullPointerException {
		if((null == comparables) || (null == value))
			throw new NullPointerException("comparables and value cannot be null");
		
		/* true if 1+ comparables match */
		boolean match = false;
		
		for(Comparable<T> compare : comparables) {
			int result = compare.compareTo(value);
			
			if(-1 == result)
				return -1; /* invert match */
			
			match = match || (0 == result);
		}
		
		return (match ? 0 : 1);
	}
	
	/**
	 * Determines whether this BookQuery is equal to the given
	 * object. Overridden to provide special case for Book objects.
	 * 
	 * @param obj the Object to compare to
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof Book))
			return super.equals(obj);
			
		return (0 == this.compareTo((Book)obj));
	}
	
}
