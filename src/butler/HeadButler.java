package butler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import data.Book;
import data.VirtualBook;
import data.VirtualBookshelf;

/**
 * The HeadButler class contains references to the VirtualLibraryButler and the
 * RemoteLibraryButlers from the RemoteLibrarys that we are currently connected to.
 * 
 * Book classification is done by the method chain: identify(compareTo(Book)).
 * @author sjpurol
 *
 */
public class HeadButler implements Butler {

	Set<LibraryButlerInterface> staff;
	int maxNumOfShelfs;
	double[] lastResults;

	/**
	 * Constructs a new HeadButler. Requires a VirtualLibraryButler that was trained
	 * on the entire local library.
	 * 
	 * Recommended constructor calls:
	 * 
	 * HeadButler name = new HeadButler(trainedButler);
	 * 
	 * HeadButler name = new HeadButler(new VirtualLibraryButler(__getPreviousButlerWeightsFromDB()__));
	 * 
	 * @param wadsworth the first VirtualLibraryButler.
	 */
	public HeadButler(VirtualLibraryButler wadsworth) throws IllegalArgumentException{
		if (null == wadsworth)
			throw new IllegalArgumentException("VirtualLibraryButler cannot be null.");
		staff = new HashSet<LibraryButlerInterface>();
		staff.add(wadsworth);

		maxNumOfShelfs = wadsworth.countShelfs();

	}
	
	/**
	 * Constructs a new HeadButler from a collection of ButlerWeights. These weights
	 * would most likely be pulled from the database.
	 * 
	 * @param weights
	 * @throws IllegalArgumentException
	 */
	public HeadButler(Collection<ButlerWeights> weights) throws IllegalArgumentException {
		if (null == weights)
			throw new IllegalArgumentException("weights cannot be null");
		staff = new HashSet<LibraryButlerInterface>();
		for (ButlerWeights bw : weights)
			staff.add(new VirtualLibraryButler(bw));
		
		reCountShelfs();
	}

	/**
	 * Adds a new Butler to the staff.
	 * 
	 * @param newButler the new Butler that was hired.
	 * @return false if newButler already exists. 
	 */
	public boolean addButler(LibraryButlerInterface newButler) {

		maxNumOfShelfs = Math.max(maxNumOfShelfs, newButler.countShelfs());

		return staff.add(newButler);
	}
	
	/**
	 * Adds a new Butler to the staff, trained on the input virtBS
	 * @return false if "new" butler already exists.
	 */
	public boolean addShelf(VirtualBookshelf virtBS) {	
		return addButler(new VirtualLibraryButler(virtBS, 1));
	}

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
		for (LibraryButlerInterface b : staff)
			newNum = Math.max(newNum, b.countShelfs());
		maxNumOfShelfs = newNum;

	}

	/**
	 * Takes a double[][] as input and normalizes each row
	 * to a scale of -1.0 - 1.0
	 * @param input the values to normalize
	 * @return the normalized values
	 */
	private double[][] normalize(double[][] input) {

		double[][] normal = new double[input.length][];

		for (int i = 0; i < input.length; ++i) {
			//System.out.println("normalize row: " + i + " / " + (input.length-1));
			double[] row = input[i];
			double max = Double.MIN_VALUE;

			for (int j = 0; j < row.length; ++j)
				max = Math.max(max, Math.abs(row[j]));

			for (int j = 0; j < row.length; ++j)
				row[j] = row[j] / max;

			normal[i] = row;
		}

		//System.out.println("normalized");
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

		double[] returnValue = new double[size+rows];
		int index = 0;
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < input[i].length; ++j) {
				//System.out.println("i: " + i + " j: " + j);

				returnValue[index] = input[i][j];
				++index;
			}
			returnValue[index] = (-1*i) - 2;
			//System.out.println(returnValue[index]);

			++index;
		}

		//System.out.println("compacted.");
		return returnValue;
	}

	/**
	 * Assembles the staff to determine book membership.
	 * The return value should only be used as the parameter to "identify(int)"
	 * 
	 * @param b the book to check 
	 */
	@Override
	public int compareTo(Book b) {

		double[][] answers = new double[staff.size()][];

		int butle = 0; //it's what a butler does... he butles.

		for (LibraryButlerInterface wadsworth : staff) {
			answers[butle] = wadsworth.assemble(b);
			butle++;
		}

		double[] compact = compact(normalize(answers));
		
		double best = Double.MIN_VALUE;

		for (int i = 0; i < compact.length; ++i) {
			//System.out.println("compact[" + i + "]: " + compact[i]);
			best = Math.max(best, compact[i]);
		}

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

		//System.out.println("index: "+ index);

		int k = index;

			while ( k > 0 && lastResults[k] >= 0) {
				--k;
			}

		int butlerNumber;

		if (k == 0)
			butlerNumber = 0;
		else
			butlerNumber = -1 * ( (int)lastResults[k] + 2);

		//System.out.println("index: " + index + " k: " + k + " butlerNumber: " + butlerNumber);
		int shelfNumber = index-k-butlerNumber;

		int i = 0;
		LibraryButlerInterface best = null;
		//System.out.println("is " + i + " < " + butlerNumber);
		for (LibraryButlerInterface b : staff)
			if (i == butlerNumber)
				best = b;
			else
				++i;
		
		if (null == best)
			return new String("Error! No best fit detected!");
		
		//System.out.println("best has: " + best.countShelfs() + " shelfs.");
		System.out.println("looking for shelf #" + shelfNumber);
		String shelfName = best.identifyShelf(shelfNumber);

		return new String(best.getProperty("name") + ": " + shelfName);
	}

	/**
	 * Returns a collection of ButlerWeights objects for the
	 * VirtualLibraryButlers in the staff. If there is no
	 * VirtualLibraryButler, returns null.
	 */
	public Collection<ButlerWeights> exportWeights() {
		Collection<VirtualLibraryButler> butler = getLocalButlers();
		Collection<ButlerWeights> oldWeights = new HashSet<ButlerWeights>();

		if (butler.isEmpty())
			return null;
		else {
			for (VirtualLibraryButler wadsworth : butler) 
				oldWeights.add(wadsworth.getWeights());
		}
		return oldWeights;
	}

	/**
	 * Returns a collection of the VirtualLibraryButlers in the staff.
	 * If there are no VirtualLibraryButlers in the staff, returns null.
	 */
	public Collection<VirtualLibraryButler> getLocalButlers() {

		Collection<VirtualLibraryButler> butlers = new HashSet<VirtualLibraryButler>();

		for (LibraryButlerInterface b : staff)
			if (b instanceof VirtualLibraryButler)
				butlers.add((VirtualLibraryButler) b);

		if (butlers.size() > 0)
			return butlers;
		else
			return null;
	}

	public static void main(String[] args) {

		VirtualBookshelf bs = new VirtualBookshelf();

		Book green = new VirtualBook("Green Eggs and Ham", "Dr. Seuss");
		String[] greenTags = {"childrens books","dr seuss","classic",
				"early reader","rhyming","kids","childhood memories",
				"humor","accelerated reader","seuss","green eggs and ham",
				"beginner book","childrens literature","easy read",
				"new experiences","picture books","sonlight 1",
				"young child books","andrew","awesome","beginners guide",
				"book","book recommendations","caldecott author",
				"caldecott winning illustrator","childerends books",
				"childrens illustration","closing","comedy","dr suess",
				"em and viv","family game","fantasy","farrahs favorite books",
				"favorite suess book","funny books","gift idea",
				"graphic sf reader","green eggs","ham","head start",
				"influence","kindergarten","lexile_30","new readers",
				"persuasion","picture book","present for scottie","quirky",
				"read-again books","sales","seuss dr","sonlight 1 readers",
				"the best children books","this is one fantastic book for the kids",
				"tyl","wonderful book","young"};

		int[] greenWeights = {76,51,42,37,29,64,71,24,36,4,3,2,2,2,2,
				2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

		for (int i = 0; i < greenTags.length; ++i){
			int num = greenWeights[i];
			while (num > 0){
				green.tag(greenTags[i]);
				--num;
			}
		}

		bs.insert(green);

		Book cat = new VirtualBook("The Cat in the Hat", "Dr. Seuss");
		String[] catTags = {"childrens books","early reader","childrens literature",
				"kids","young child books","accelerated reader","bedtime story",
				"caldecott winning illustrator","childrens illustration","dr seuss",
				"elementary","elementary books","picture book","seuss"};
		int[] catWeights = {56,43,82,72,22,11,51,61,71,1,1,1,1,1};
		for (int i = 0; i < catTags.length; ++i){
			int num = catWeights[i];
			while (num > 0){
				cat.tag(catTags[i]);
				--num;
			}
		}

		bs.insert(cat);

		Book fish = new VirtualBook("One Fish Two Fish Red Fish Blue Fish", "Dr. Seuss");

		String[] fishTags = {"childrens books","dr seuss","kids",
				"the best kids books","rhyming","humor","book",
				"cindy","easy reader","sonlight 1",
				"a must read for every child","abby","animal stories",
				"bedtime book","beginner readers","	beginning reader",
				"beginning reading","childhood memories","children books",
				"childrens classics","childrens poems","childres books",
				"comics","dr seuss one fish two fish","early reader",
				"early readers","first book","fish","imagination",
				"kidsbook","mathilda","picture books",
				"repetitive books","rhyme","rhyming books",
				"rhythmic books","s web","seuss","silliness",
				"sonlight 1 readers","still funny","suess","toddlers",
				"tyke","young readers"};

		int[] fishWeights = {81,47,15,11,10,8,6,2,2,2,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		for (int i = 0; i < fishTags.length; ++i){
			int num = fishWeights[i];
			while (num > 0){
				fish.tag(fishTags[i]);
				--num;
			}
		}

		bs.insert(fish);

		Book places = new VirtualBook("Oh, the Places You'll Go!", "Dr. Seuss");

		String[] placesTags = {"childrens books","dr seuss",
				"graduation gift","graduation","inspirational",
				"advice","book","gift idea","rhyming","adult",
				"kids","seuss","enlightening","humor",
				"a wish book for college bound first year...","abby",
				"adolecvent","aging","birth","business",
				"camino","celebrations","cheer up","classic",
				"diy success","dr suess","employment",
				"encouragement","explore feelings","faith",
				"fear","graduation gift - high school or college",
				"graduation with advise in humor","great graduation gift",
				"grief and loss","guid","help",
				"high school graduation gift idea","hope","hopeful",
				"inspirational and entertaining","inspirational book",
				"journey of life","jubilee","justin matott",
				"keepsake","marriage","matott","midlife",
				"motivational","my books","new experiences",
				"not just for kids","personal growth","picture books",
				"profound","real life","recognition gift","riley",
				"sand","school program gift","success","suess",
				"teen","the minds eye of children thank you dr s...",
				"the success of robert fitzgibbons","uplifting",
				"wedding speeches","when i was a boy i dreamed",
				"when i was a girl i dreamed","young adult","young child books"};

		int[] placesWeights = {45,40,33,26,24,12,10,6,6,5,5,3,2,2,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

		//System.out.println(placesTags.length);
		//System.out.println(placesWeights.length);

		for (int i = 0; i < placesTags.length; ++i){
			int num = placesWeights[i];
			while (num > 0){
				places.tag(placesTags[i]);
				--num;
			}

		}

		bs.insert(places);

		Book it = new VirtualBook("It", "Stephen King");
		String[] itTags = {"stephen king","horror","horror book","fiction",
				"book recommendations","yearly harvest","dark fantasy",
				"ryan callaway","book","clowns","dark tower","childhood",
				"it","clown","friendship vking","long book","movie tie-ins",
				"american best sellers","beautyful","book to film","buitful",
				"cesarnda","coming of age","contemporary","dark","fantasy",
				"fear","gay","gothic","great thriller",
				"greatest horror ever made","gripping","happy","horrorbook",
				"king had better","lovely","mares likes","modern gothic",
				"monsters","movie tie-in","my books","not worth it","novel",
				"novels","one of my favorites","possible-kindle","read",
				"review","roger3","room 237","shelf","stephen king horror",
				"stephen king it","supernatural","suspense","thriller",
				"urban fantasy","wordy","childrens books"};

		int[] itWeights = {24,73,26,21,18,13,10,10,9,8,4,3,3,2,2,2,2,2,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1, -100};

		for (int i = 0; i < itTags.length; ++i){
			int num = itWeights[i];
			while (num > 0){
				it.tag(itTags[i]);
				--num;
			}
		}

		bs.insert(it);

		Book catBack = new VirtualBook("The Cat in the Hat Comes Back", "Dr. Seuss");
		String[] backTags = {"dr seuss","childrens books","beginner books",
				"childrens classics","classic","rhyming","collectible",
				"kids","beginner readers","childrens","childrens series",
				"1958 edition","cat in the hat","cat in the hat 2",
				"childrens poems","dr seuss cd","dr suess","early reader",
				"early readers","fantasy","graphic sf reader","humor",
				"kids - dr seuss","kidsbook","kindergarten","picture books",
				"seuss","the cat in the hat","toddlers","vintage",
				"young child books","young readers"};

		int[] backWeights = {18,16,9,8,6,6,5,3,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

		for (int i = 0; i < backTags.length; ++i){
			int num = backWeights[i];
			while (num > 0){
				catBack.tag(backTags[i]);
				--num;
			}
		}

		Book salem = new VirtualBook("Salems' Lot", "Stephen King");
		String[] salemTags = {"horror","stephen king","vampire",
				"fiction","book","king","dark tower",
				"books with bite","contemporary","fabulous",
				"ps gifford","adventure","room 237","audio book",
				"bababooey","ben","book recommendations",
				"books to read in the dead of night","boring",
				"dark fantasy","dr seuss","favorite horror",
				"harbinger of doom","horror scary","king of all",
				"mary kate","mass market paperback","modern gothic",
				"not free sf reader","novels",
				"one of my favorite king stories","shelf","smart horror",
				"vampire books","vamps","writer", "childrens book"};

		int[] salemWeights = {84,29,47,13,12,9,7,3,3,3,3,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-523};

		for (int i = 0; i < salemTags.length; ++i){
			int num = salemWeights[i];
			while (num > 0){
				salem.tag(salemTags[i]);
				--num;
			}
		}

		Book interview = new VirtualBook("Interview with a Vampire", "Anne Rice");
		String[] interviewTags = {"vampire","anne rice","vampire chronicles",
				"gothic horror","rice","horror","book","lestat",
				"paranormal romance","book recommendations","books with bite",
				"dead souls and dark alleys","fiction","vampire drama","ghosts",
				"interview with the vampire","interview with the vampire anne rice",
				"suspense","titus mcguire","adventure","annerice","armand",
				"books read anne rice","bradpitt","coolmovies",
				"extremely good stuff","fiction book","france",
				"historical dimensions and perspectives","historical fiction",
				"humor","interview","k s michaels","louisiana","love it",
				"macabre","modern gothic tale","mona","mystery","neworleans",
				"nice book","not free sf reader","novel","paperback",
				"poetic","read","rue royale","sanctuary of darkness",
				"scary stories","series","silverbullet","southern discomfort",
				"spouse","stake","summerreading","trips and journeys",
				"uncompromising","vampire book","vampire novel","women",
				"women writers", "childrens book"};
		int[] interviewWeights = {134,67,36,18,10,91,7,5,5,4,3,3,3,3,2,2,2,
				2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-59};

		for (int i = 0; i < interviewTags.length; ++i){
			int num = interviewWeights[i];

			while (num > 0){
				interview.tag(interviewTags[i]);
				--num;
			}
		}

		System.out.println("Books initialized");

		VirtualLibraryButler wadsworth = new VirtualLibraryButler("wadsworth");

		wadsworth.initialize(bs, 3);

		System.out.println("Wadsworth initialized");
		HeadButler TimCurry = new HeadButler(wadsworth);

		System.out.println(interview.getProperty("title"));
		System.out.println(TimCurry.identify(TimCurry.compareTo(interview)));

	}

}
