package butler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import data.Book;

/**
 * The HeadButler class contains references to the VirtualLibraryButler and the
 * RemoteLibraryButlers from the RemoteLibrarys that we are currently connected to.
 * 
 * Book classification is done by the method chain: identify(compareTo(Book)).
 * @author sjpurol
 *
 */
public class HeadButler extends Butler {

	Set<LibraryButler> staff;
	int maxNumOfShelfs;
	double[] lastResults;

	/**
	 * Constructs a new HeadButler. Requires a VirtualLibraryButler that was trained
	 * on the entire local library.
	 * 
	 * @param wadsworth
	 */
	public HeadButler(VirtualLibraryButler wadsworth) {
		staff = new HashSet<LibraryButler>();
		staff.add(wadsworth);

		maxNumOfShelfs = wadsworth.getShelfs().size();

	}

	/**
	 * Adds a new Butler to the staff.
	 * 
	 * @param newButler the new Butler that was hired.
	 * @return false if newButler already exists. 
	 */
	public boolean addButler(LibraryButler newButler) {

		maxNumOfShelfs = Math.max(maxNumOfShelfs, newButler.countShelfs());

		return staff.add(newButler);}

	/**
	 * Fires the given butler.
	 * 
	 * @param oldButler the old Butler that was fired.
	 * @return false if oldButler did not exist.
	 */
	public boolean removeButler(LibraryButler oldButler) {

		if (maxNumOfShelfs == oldButler.countShelfs())
			reCountShelfs();

		return staff.remove(oldButler);
	}
	
	/**
	 * Recounts the max number of shelfs.
	 */
	private void reCountShelfs() {
		int newNum = 0;
		for (LibraryButler b : staff)
			newNum = Math.max(newNum, b.countShelfs());
		maxNumOfShelfs = newNum;
		
	}
	
	/**
	 * Takes a double[][] as input and normalizes each row
	 * to a scale of 0.0 - 1.0
	 * @param input the values to normalize
	 * @return the normalized values
	 */
	private double[][] normalize(double[][] input) {
		
		double[][] normal = new double[input.length][];
		
		for (int i = 0; i < input.length; ++i) {
			
			double[] row = input[i];
			double max = Double.MIN_VALUE;
			
			for (int j = 0; j < row.length; ++j)
				max = Math.max(max, Math.abs(row[j]));
			
			for (int j = 0; j < row.length; ++j)
				row[j] = row[j] / max;
			
			normal[i] = row;
		}
		return normal;
	}
	
	/**
	 * Takes a double[][] and compacts it into a double[].
	 */
	private double[] compact (double[][] input) {
		
		int size = 0;
		int rows = 0;
		for (; rows < input.length; ++rows) {
			size = size + input[rows].length; 
		}
		
		double[] returnValue = new double[size+rows-1];
		int index = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < input[i].length; ++j) {
				returnValue[index] = input[i][j];
				++index;
			}
			if ( !(i == rows-1) )
				returnValue[index] = (-1*i) - 2;
			++index;
		}
		return returnValue;
	}

	@Override
	/**
	 * Assembles the staff to determine book membership.
	 * The return value should only be used as the parameter to "identify(int)"
	 * 
	 * @param b the book to check 
	 */
	public int compareTo(Book b) {

		double[][] answers = new double[staff.size()][];
		
		int butle = 0; //it's what a butler does... he butles.
		for (LibraryButler wadsworth : staff) {
			answers[butle] = wadsworth.assemble(b);
			butle++;
		}
		
		double[] compact = compact(normalize(answers));
		
		double best = Double.MIN_VALUE;
		
		for (int i = 0; i < compact.length; ++i)
			best = Math.max(best, compact[i]);
		
		int index = -1;
		
		for (int i = 0; i < compact.length; ++i) {
			if (compact[i] == best) {
					index = i;
				}
		}
		lastResults = compact;
		
		return index;
	}
	
	/**
	 * Identifies the LibraryButler and Bookshelf that a given index points to within lastResults.
	 * @param index the index to look up.
	 * @return a string of the form: "ButlerName: ShelfName"
	 */
	public String identify(int index) {
		
		int k = index;
		
		do {
		--k;
		}
		while (lastResults[k] >= 0);
		
		int shelfNumber = index-k-1;
		int butlerNumber = -1 * ( (int)lastResults[k] + 2);
		
		Iterator<LibraryButler> staffIt = staff.iterator();
		
		int i = 0;
		while (i < butlerNumber) {
			staffIt.next();
			++i;
		}
		
		LibraryButler best = staffIt.next();
		String shelfName = best.identifyShelf(shelfNumber);
				
		return new String(best.getProperty("name") + ": " + shelfName);
	}
	
	/**
	 * Returns the ButlerWeights object for the only
	 * VirtualLibraryButler in the staff. If there is no
	 * VirtualLibraryButler, returns null.
	 */
	public ButlerWeights exportWeights() {
		VirtualLibraryButler butler = getLocalButler();
		
		if (null == butler)
			return null;
		else
			return butler.getWeights();
	}
	
	/**
	 * Returns the first (hopefully only) VirtualLibraryButler in the staff.
	 * If there is no VirtualLibraryButler in the staff, returns null.
	 */
	public VirtualLibraryButler getLocalButler() {

		for (LibraryButler b : staff)
			if (b instanceof VirtualLibraryButler)
				return ((VirtualLibraryButler) b);
				
		return null;
	}
}
