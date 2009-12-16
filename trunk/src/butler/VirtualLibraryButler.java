package butler;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import operations.BookshelfOperations;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.Strategy;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodGaussian;
import org.encog.neural.networks.training.strategy.SmartLearningRate;
import org.encog.neural.pattern.SOMPattern;
import org.encog.util.math.rbf.GaussianFunction;
import org.slf4j.LoggerFactory;

import data.Book;
import data.Bookshelf;
import data.VirtualBook;
import data.VirtualBookshelf;
import data.VirtualLibrary;

/**
 * The VirtualLibraryButler class is an extension of LibraryButler. It is
 * trained with the local VirtualLibrary.
 * 
 * @author sjpurol
 * 
 */
public class VirtualLibraryButler extends LibraryButler {

	private Set<Bookshelf> sortedShelfs;
	private Thread training;

	/**
	 * Test method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		VirtualLibrary emptyLib = new VirtualLibrary();
		VirtualLibraryButler dumbBut = new VirtualLibraryButler(emptyLib);

		// ScriptGenerator sg = new ScriptGenerator("src\\scripts\\en_US.dic");
		// sg.generateLibrary(20, 5);

		VirtualBookshelf bs = new VirtualBookshelf();

		Book green = new VirtualBook("Green Eggs and Ham", "Dr. Seuss");
		String[] greenTags = { "childrens books", "dr seuss", "classic",
				"early reader", "rhyming", "kids", "childhood memories",
				"humor", "accelerated reader", "seuss", "green eggs and ham",
				"beginner book", "childrens literature", "easy read",
				"new experiences", "picture books", "sonlight 1",
				"young child books", "andrew", "awesome", "beginners guide",
				"book", "book recommendations", "caldecott author",
				"caldecott winning illustrator", "childerends books",
				"childrens illustration", "closing", "comedy", "dr suess",
				"em and viv", "family game", "fantasy",
				"farrahs favorite books", "favorite suess book", "funny books",
				"gift idea", "graphic sf reader", "green eggs", "ham",
				"head start", "influence", "kindergarten", "lexile_30",
				"new readers", "persuasion", "picture book",
				"present for scottie", "quirky", "read-again books", "sales",
				"seuss dr", "sonlight 1 readers", "the best children books",
				"this is one fantastic book for the kids", "tyl",
				"wonderful book", "young" };

		int[] greenWeights = { 76, 51, 42, 37, 29, 64, 71, 24, 36, 4, 3, 2, 2,
				2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1 };

		for (int i = 0; i < greenTags.length; ++i) {
			int num = greenWeights[i];
			while (num > 0) {
				green.tag(greenTags[i]);
				--num;
			}
		}

		bs.insert(green);

		Book cat = new VirtualBook("The Cat in the Hat", "Dr. Seuss");
		String[] catTags = { "childrens books", "early reader",
				"childrens literature", "kids", "young child books",
				"accelerated reader", "bedtime story",
				"caldecott winning illustrator", "childrens illustration",
				"dr seuss", "elementary", "elementary books", "picture book",
				"seuss" };
		int[] catWeights = { 56, 43, 82, 72, 22, 11, 51, 61, 71, 1, 1, 1, 1, 1 };
		for (int i = 0; i < catTags.length; ++i) {
			int num = catWeights[i];
			while (num > 0) {
				cat.tag(catTags[i]);
				--num;
			}
		}

		bs.insert(cat);

		Book fish = new VirtualBook("One Fish Two Fish Red Fish Blue Fish",
				"Dr. Seuss");

		String[] fishTags = { "childrens books", "dr seuss", "kids",
				"the best kids books", "rhyming", "humor", "book", "cindy",
				"easy reader", "sonlight 1", "a must read for every child",
				"abby", "animal stories", "bedtime book", "beginner readers",
				"	beginning reader", "beginning reading", "childhood memories",
				"children books", "childrens classics", "childrens poems",
				"childres books", "comics", "dr seuss one fish two fish",
				"early reader", "early readers", "first book", "fish",
				"imagination", "kidsbook", "mathilda", "picture books",
				"repetitive books", "rhyme", "rhyming books", "rhythmic books",
				"s web", "seuss", "silliness", "sonlight 1 readers",
				"still funny", "suess", "toddlers", "tyke", "young readers" };

		int[] fishWeights = { 81, 47, 15, 11, 10, 8, 6, 2, 2, 2, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1 };
		for (int i = 0; i < fishTags.length; ++i) {
			int num = fishWeights[i];
			while (num > 0) {
				fish.tag(fishTags[i]);
				--num;
			}
		}

		bs.insert(fish);

		Book places = new VirtualBook("Oh, the Places You'll Go!", "Dr. Seuss");

		String[] placesTags = { "childrens books", "dr seuss",
				"graduation gift", "graduation", "inspirational", "advice",
				"book", "gift idea", "rhyming", "adult", "kids", "seuss",
				"enlightening", "humor",
				"a wish book for college bound first year...", "abby",
				"adolecvent", "aging", "birth", "business", "camino",
				"celebrations", "cheer up", "classic", "diy success",
				"dr suess", "employment", "encouragement", "explore feelings",
				"faith", "fear", "graduation gift - high school or college",
				"graduation with advise in humor", "great graduation gift",
				"grief and loss", "guid", "help",
				"high school graduation gift idea", "hope", "hopeful",
				"inspirational and entertaining", "inspirational book",
				"journey of life", "jubilee", "justin matott", "keepsake",
				"marriage", "matott", "midlife", "motivational", "my books",
				"new experiences", "not just for kids", "personal growth",
				"picture books", "profound", "real life", "recognition gift",
				"riley", "sand", "school program gift", "success", "suess",
				"teen", "the minds eye of children thank you dr s...",
				"the success of robert fitzgibbons", "uplifting",
				"wedding speeches", "when i was a boy i dreamed",
				"when i was a girl i dreamed", "young adult",
				"young child books" };

		int[] placesWeights = { 45, 40, 33, 26, 24, 12, 10, 6, 6, 5, 5, 3, 2,
				2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		// System.out.println(placesTags.length);
		// System.out.println(placesWeights.length);

		for (int i = 0; i < placesTags.length; ++i) {
			int num = placesWeights[i];
			while (num > 0) {
				places.tag(placesTags[i]);
				--num;
			}

		}

		bs.insert(places);

		Book it = new VirtualBook("It", "Stephen King");
		String[] itTags = { "stephen king", "horror", "horror book", "fiction",
				"book recommendations", "yearly harvest", "dark fantasy",
				"ryan callaway", "book", "clowns", "dark tower", "childhood",
				"it", "clown", "friendship vking", "long book",
				"movie tie-ins", "american best sellers", "beautyful",
				"book to film", "buitful", "cesarnda", "coming of age",
				"contemporary", "dark", "fantasy", "fear", "gay", "gothic",
				"great thriller", "greatest horror ever made", "gripping",
				"happy", "horrorbook", "king had better", "lovely",
				"mares likes", "modern gothic", "monsters", "movie tie-in",
				"my books", "not worth it", "novel", "novels",
				"one of my favorites", "possible-kindle", "read", "review",
				"roger3", "room 237", "shelf", "stephen king horror",
				"stephen king it", "supernatural", "suspense", "thriller",
				"urban fantasy", "wordy" };

		int[] itWeights = { 24, 73, 26, 21, 18, 13, 10, 10, 9, 8, 4, 3, 3, 2,
				2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1 };

		for (int i = 0; i < itTags.length; ++i) {
			int num = itWeights[i];
			while (num > 0) {
				it.tag(itTags[i]);
				--num;
			}
		}
		VirtualBookshelf shelf = new VirtualBookshelf();
		shelf.insert(it);

		Book catBack = new VirtualBook("The Cat in the Hat Comes Back",
				"Dr. Seuss");
		String[] backTags = { "dr seuss", "childrens books", "beginner books",
				"childrens classics", "classic", "rhyming", "collectible",
				"kids", "beginner readers", "childrens", "childrens series",
				"1958 edition", "cat in the hat", "cat in the hat 2",
				"childrens poems", "dr seuss cd", "dr suess", "early reader",
				"early readers", "fantasy", "graphic sf reader", "humor",
				"kids - dr seuss", "kidsbook", "kindergarten", "picture books",
				"seuss", "the cat in the hat", "toddlers", "vintage",
				"young child books", "young readers" };

		int[] backWeights = { 18, 16, 9, 8, 6, 6, 5, 3, 2, 2, 2, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		for (int i = 0; i < backTags.length; ++i) {
			int num = backWeights[i];
			while (num > 0) {
				catBack.tag(backTags[i]);
				--num;
			}
		}

		Book salem = new VirtualBook("Salems' Lot", "Stephen King");
		String[] salemTags = { "horror", "stephen king", "vampire", "fiction",
				"book", "king", "dark tower", "books with bite",
				"contemporary", "fabulous", "ps gifford", "adventure",
				"room 237", "audio book", "bababooey", "ben",
				"book recommendations", "books to read in the dead of night",
				"boring", "dark fantasy", "dr seuss", "favorite horror",
				"harbinger of doom", "horror scary", "king of all",
				"mary kate", "mass market paperback", "modern gothic",
				"not free sf reader", "novels",
				"one of my favorite king stories", "shelf", "smart horror",
				"vampire books", "vamps", "writer" };

		int[] salemWeights = { 84, 29, 47, 13, 12, 9, 7, 3, 3, 3, 3, 2, 2, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1 };

		for (int i = 0; i < salemTags.length; ++i) {
			int num = salemWeights[i];
			while (num > 0) {
				salem.tag(salemTags[i]);
				--num;
			}
		}

		Book interview = new VirtualBook("Interview with a Vampire",
				"Anne Rice");
		String[] interviewTags = { "vampire", "anne rice",
				"vampire chronicles", "gothic horror", "rice", "horror",
				"book", "lestat", "paranormal romance", "book recommendations",
				"books with bite", "dead souls and dark alleys", "fiction",
				"vampire drama", "ghosts", "interview with the vampire",
				"interview with the vampire anne rice", "suspense",
				"titus mcguire", "adventure", "annerice", "armand",
				"books read anne rice", "bradpitt", "coolmovies",
				"extremely good stuff", "fiction book", "france",
				"historical dimensions and perspectives", "historical fiction",
				"humor", "interview", "louisiana", "love it", "macabre",
				"modern gothic tale", "mona", "mystery", "neworleans",
				"nice book", "not free sf reader", "novel", "paperback",
				"poetic", "read", "rue royale", "sanctuary of darkness",
				"scary stories", "series", "silverbullet",
				"southern discomfort", "spouse", "stake", "summerreading",
				"trips and journeys", "uncompromising", "vampire book",
				"vampire novel", "women", "women writers" };
		int[] interviewWeights = { 134, 67, 36, 18, 10, 191, 7, 5, 5, 4, 3, 3,
				3, 3, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1 };

		for (int i = 0; i < interviewTags.length; ++i) {
			int num = interviewWeights[i];

			while (num > 0) {
				interview.tag(interviewTags[i]);
				--num;
			}
		}

		VirtualLibraryButler buddy = new VirtualLibraryButler("buddy");
		VirtualLibraryButler wadsworth = new VirtualLibraryButler("wadsworth");

		System.out.println("The Cat in the Hat Comes Back: "
				+ dumbBut.checkBook(catBack).getProperty("name"));

		// VirtualFlatShelf flatBS = new VirtualFlatShelf(bs);
		// System.out.println("created flatBS");
		// List<VirtualFlatShelf> flatShelfs = new
		// ArrayList<VirtualFlatShelf>();
		// flatShelfs.add(flatBS);
		VirtualBookshelf horror = new VirtualBookshelf();

		horror.setProperty("name", "horror");
		bs.setProperty("name", "Seussville");

		System.out.println(horror.getProperty("name"));
		System.out.println(bs.getProperty("name"));

		// horror.insert(interview);
		horror.insert(salem);
		horror.insert(it);

		// VirtualFlatShelf flatHorror = new VirtualFlatShelf(horror);
		// flatShelfs.add(flatHorror);

		// buddy.initialize(flatShelfs);
		Set<Bookshelf> allShelfs = new HashSet<Bookshelf>();
		allShelfs.add(horror);
		allShelfs.add(bs);

		Bookshelf bigShelf = horror.union(bs);

		buddy.initialize(allShelfs);

		while (buddy.isTraining()) {
			// wait
		}

		System.out.println(buddy.getProperty("name") + " results:\n");

		System.out.println("The Cat in the Hat Comes Back: "
				+ buddy.checkBook(catBack).getProperty("name"));
		System.out.println("Salem's Lot: "
				+ buddy.checkBook(salem).getProperty("name"));
		System.out.println("Interview with a Vampire: "
				+ buddy.checkBook(interview).getProperty("name"));
		System.out.println("The Cat in the Hat: "
				+ buddy.checkBook(cat).getProperty("name"));
		System.out.println("One Fish Two Fish Red Fish Blue Fish: "
				+ buddy.checkBook(fish).getProperty("name"));

		for (int n = 2; n < 5; ++n) {
			wadsworth.initialize(bigShelf, n);
			System.out.println(wadsworth.getProperty("name") + " results (" + n
					+ " shelfs):\n");
			wadsworth.checkBook(catBack);
			wadsworth.checkBook(salem);
			wadsworth.checkBook(interview);
			wadsworth.checkBook(cat);
			wadsworth.checkBook(fish);
		}
	}

	/**
	 * Creates this with an existing ButlerWeights object The Butler will not
	 * function properly until one of the initialize methods are called.
	 * 
	 * @param weights
	 *            the existing ButlerWeights object.
	 */
	public VirtualLibraryButler(ButlerWeights weights) {
		setMatrixes(weights);
		initialized = true;
		org.encog.util.logging.Logging.stopConsoleLogging();
	}

	/**
	 * Sets the name property of this to name. The Butler will not function
	 * properly until one of the initialize methods are called.
	 */
	public VirtualLibraryButler(String name) {
		properties = new HashMap<String, String>();
		properties.put("name", name);
		initialized = false;
		org.encog.util.logging.Logging.stopConsoleLogging();
	}

	/**
	 * Creates and initializes a new Butler from a shelf and number of shelfs.
	 * Butler is immediately ready for use.
	 * 
	 * @param shelf
	 * @param numShelfs
	 */
	VirtualLibraryButler(Bookshelf shelf, int numShelfs) {
		properties = new HashMap<String, String>();
		sortedShelfs = initialize(shelf, numShelfs);
		org.encog.util.logging.Logging.stopConsoleLogging();
	}

	/**
	 * Creates and initializes a new Butler from a VirtualLibrary. Butler is
	 * immediately ready for use.
	 * 
	 * @param virtLib
	 */
	public VirtualLibraryButler(VirtualLibrary virtLib) {
		properties = new HashMap<String, String>();
		properties.put("library", virtLib.getProperty("name"));
		org.encog.util.logging.Logging.stopConsoleLogging();
		Collection<Bookshelf> allShelfs = new HashSet<Bookshelf>();
		if (virtLib.getMasterShelf().size() > 0) {
			for (Bookshelf b : virtLib)
				allShelfs.add(b);
			this.initialize(allShelfs);
		} else
			initialized = false;
	}

	Set<Bookshelf> getSortedShelfs() {
		return sortedShelfs;
	}

	private NeuralData compute(NeuralData b) throws IllegalArgumentException {
		if (this.isTraining())
			throw new IllegalArgumentException("This butler is still training.");
		else
			return brain.compute(b);
	}

	/**
	 * Checks an input book against the trained network. Returns the flatshelf
	 * that best matches. If no shelf matches, returns null. If the network has
	 * not been initialized, returns null.
	 * 
	 * @param b
	 *            The book to check.
	 */
	public FlatShelf checkBook(Book b) {

		double[] inputValues = readyBook(b);

		if (inputValues[0] == inputValues[1])
			return new FlatShelf();

		NeuralData book = new BasicNeuralData(inputValues);
		NeuralData output;
		try {
			output = this.compute(book);
		} catch (Exception e) {
			return new FlatShelf();
		}

		double best = Double.MIN_VALUE;

		for (int i = 0; i < numShelfs; ++i)
			best = Math.max(best, output.getData(i));

		int index = -1;
		for (int i = 0; i < numShelfs; ++i)
			if (best == output.getData(i))
				index = i;

		// System.out.println(b.getProperty("title") + ": " +
		// identifyShelf(index) + "(" + index + ")");
		System.out
				.println(b.getProperty("title") + ": " + identifyShelf(index));

		initialized = true;

		FlatShelf fs = getShelfs().get(index);
		fs.setProperty("library", this.getProperty("library"));

		return fs;
	}

	/**
	 * Very similar to compareTo(Book). Used to identify the order of the output
	 * vector.
	 * 
	 * @param b
	 *            Book to examine
	 * @param bool
	 * @return the index of the proper output value from brain
	 */
	private int checkBook(FlatShelf b) {

		Iterator<Map.Entry<String, Integer>> tags = b.enumerateTags()
				.iterator();
		// Iterator<Map.Entry<String, String>> properties =
		// b.enumerateProperties();

		// InputPairSet inputPairs = new InputPairSet(); // neuron # -> weight

		double[] inputValues = new double[numTags];

		while (tags.hasNext()) {
			Map.Entry<String, Integer> tag = tags.next();
			// System.out.println("tags: " + tag.getKey() + " : " +
			// tag.getValue());
			Integer current = idPairs.get(tag.getKey());
			if (current == null) {
				// current = idPairs.get(OTHER);
				// inputValues[current.getValue()] += tag.getValue();
			} else
				inputValues[current] = tag.getValue();

			// inputPairs.add(new InputPair(current.getValue(),
			// (double)tag.getValue()));

		}

		NeuralData book = new BasicNeuralData(inputValues);
		final NeuralData output = brain.compute(book);

		double best = Double.MIN_VALUE;

		for (int i = 0; i < numShelfs; ++i)
			best = Math.max(best, output.getData(i));

		int index = -1;
		for (int i = 0; i < numShelfs; ++i)
			if (best == output.getData(i))
				index = i;

		return index;
	}

	/**
	 * Takes a collection of bookshelfs and returns a collection of flatshelfs
	 * 
	 * @param books
	 *            the bookshelfs to flatten
	 */
	private Collection<FlatShelf> flattenAll(Collection<Bookshelf> books) {
		Set<FlatShelf> flatShelfs = new HashSet<FlatShelf>();

		for (Bookshelf b : books)
			flatShelfs.add(new FlatShelf(b));

		return flatShelfs;
	}

	/**
	 * Generates the proper type of BasicNetwork for use within a Butler.
	 * 
	 * @return
	 */
	private BasicNetwork generateBrain(int numTags, int numShelfs) {
		SOMPattern pattern = new SOMPattern();
		pattern.setInputNeurons(numTags);
		pattern.setOutputNeurons(numShelfs);

		return pattern.generate();
	}

	@Override
	public String getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * Returns the map of flatShelfs that this Butler was trained on.
	 */
	public Map<Integer, FlatShelf> getShelfs() {
		return flatShelfs;
	}

	/**
	 * Returns the current ButlerWeights object.
	 */
	public ButlerWeights getWeights() {
		return currentWeights;
	}

	/**
	 * Creates the IDPairSet that identifies which tag goes to which input
	 * neuron.
	 * 
	 * @param basis
	 *            the bookshelf that contains the books which contain the tags
	 *            to be ID'ed.
	 */
	private Map<String, Integer> identifyInputs(Bookshelf basis) {

		Map<String, Integer> idPairs = new HashMap<String, Integer>();

		maxTagMag = 0;

		int i = 0;

		Iterator<Book> books = basis.iterator();

		while (books.hasNext()) {
			Book book = books.next();
			Iterator<Map.Entry<String, Integer>> tags = book.enumerateTags()
					.iterator();

			while (tags.hasNext()) {

				Map.Entry<String, Integer> tag = tags.next();

				maxTagMag = Math.max(maxTagMag, Math.abs(tag.getValue()));

				if (!idPairs.containsValue(tag.getKey())) {
					idPairs.put(tag.getKey(), i);
					++i;
				}
			}
		}
		idPairs.put(OTHER, i);

		numTags = i + 1;

		return idPairs;
	}

	/**
	 * Initializes this from an unsorted (or sorted) basis bookshelf sorting the
	 * books into numShelfs or fewer bookshelfs.
	 * 
	 * @param basis
	 *            the bookshelf to learn
	 * @param numShelfs
	 *            the maximum number of shelfs
	 * @return the collection of all bookshelfs generated.
	 */
	public Set<Bookshelf> initialize(Bookshelf basis, int numShelfs)
			throws IllegalArgumentException {
		if (null == basis)
			throw new IllegalArgumentException("input cannot be null.");

		idPairs = identifyInputs(basis); // tag | property -> neuron #
		int numBooks = basis.size();
		this.numShelfs = numShelfs;
		double[][] inputValues = new double[numBooks][numTags];

		brain = generateBrain(numTags, numShelfs);

		int i = 0;

		// parse through the properties and tags of each book and normalize the
		// weights.
		for (Book book : basis) {

			Iterator<Map.Entry<String, Integer>> tags = book.enumerateTags()
					.iterator();

			while (tags.hasNext()) {
				Map.Entry<String, Integer> tag = tags.next();
				Integer index = idPairs.get(tag.getKey());

				if (index.intValue() > numTags) {
					System.out.println("ERROR: INDEX OUT OF BOUNDS!");
					System.out.println("name: " + tag.getKey() + " index: "
							+ index);
					System.out.println("numTags: " + numTags);

				} else if (index.intValue() == numTags) {
					// System.out.println("__OTHER__ detected!");
					inputValues[i][index.intValue()] += ((double) tag
							.getValue() / (double) maxTagMag);
				} else {
					inputValues[i][index.intValue()] = ((double) tag.getValue() / (double) maxTagMag);
				}
			}
			++i;
		}

		NeuralDataSet data = new BasicNeuralDataSet(inputValues, null);

		train(data);

		// Export current butler to ButlerWeights.
		currentWeights = new ButlerWeights(brain.getStructure().getSynapses()
				.get(0).getMatrix(), numTags, flatShelfs, idPairs);
		initialized = true;

		Map<Integer, Bookshelf> newShelfs = new HashMap<Integer, Bookshelf>();
		for (int j = 0; j < numShelfs; ++j)
			newShelfs.put(j, new VirtualBookshelf());

		for (Book book : basis) {

			double[] outputValues = brain.compute(
					new BasicNeuralData(readyBook(book))).getData();

			if (!(outputValues.length == 0)) {
				double[] bestResult = max(outputValues);
				newShelfs.get((int) bestResult[1]).insert(book);
			}
		}

		Map<Integer, Bookshelf> cleanShelfs = new HashMap<Integer, Bookshelf>();
		int j = 0;
		for (Bookshelf b : newShelfs.values()) {
			// System.out.println("(clean up) shelf size: " + b.size());

			if (!b.empty()) {
				cleanShelfs.put(j, b);
				++j;
			}
		}

		// System.out.println("numShelfs input: " + numShelfs);
		// System.out.println("actual numShelfs: " + cleanShelfs.size());

		if (cleanShelfs.size() < numShelfs) {
			// System.out.println("retraining needed. " + numShelfs +
			// " becomes " + cleanShelfs.size());
			return initialize(basis, cleanShelfs.size()); // retrain with the
			// proper size.
		}

		Collection<FlatShelf> randomFlatShelfs = flattenAll(cleanShelfs
				.values());
		flatShelfs = new HashMap<Integer, FlatShelf>();

		// System.out.println("naming and sorting flatshelfs");

		for (FlatShelf fs : randomFlatShelfs) {

			String heaviestTagName = "";
			int heaviestTagWeight = Integer.MIN_VALUE;
			int k = checkBook(fs);

			Iterator<Map.Entry<String, Integer>> fsTags = fs.enumerateTags()
					.iterator();

			while (fsTags.hasNext()) {

				Map.Entry<String, Integer> tag = fsTags.next();
				heaviestTagWeight = Math.max(heaviestTagWeight, tag.getValue());

				if (heaviestTagWeight == tag.getValue())
					heaviestTagName = tag.getKey();

			}

			try {
				cleanShelfs.get(k).setProperty("name", heaviestTagName);
			} catch (Exception e) {
				int timeout = 0;
				while (timeout < 10000) {
					System.out.println(heaviestTagName == null);
					System.out.println(cleanShelfs.get(k) == null);
					timeout++;
				}
			}
			// System.out.println(heaviestTagName + " is shelf #" + k);

			flatShelfs.put(k, new FlatShelf(cleanShelfs.get(k)));

			// System.out.println(flatShelfs.get(k).getProperty("name"));

		}

		// System.out.println("cleanShelfs size: " + cleanShelfs.size());

		return new HashSet<Bookshelf>(cleanShelfs.values());
	}

	/**
	 * Initializes the network with an example collection of bookshelfs.
	 * 
	 * @param basis
	 *            A collection of VirtualBookshelfs
	 * @throws IllegalArgumentException
	 */
	public void initialize(Collection<Bookshelf> basis)
			throws IllegalArgumentException {

		if (null == basis)
			throw new IllegalArgumentException("input cannot be null.");

		Iterator<Bookshelf> shelfs = basis.iterator();

		Bookshelf masterShelf = BookshelfOperations.union(basis);

		idPairs = identifyInputs(masterShelf); // tag | property -> neuron #
		this.numShelfs = basis.size();
		int numBooks = masterShelf.size();

		int i = 0;
		double[][] inputValues = new double[numBooks][numTags];

		while (shelfs.hasNext()) {

			Bookshelf shelf = shelfs.next();
			Iterator<Book> books = shelf.iterator();

			// System.out.println("numShelfs: " + String.valueOf(numShelfs));

			while (books.hasNext()) {

				Book book = books.next();
				Iterator<Map.Entry<String, Integer>> tags = book
						.enumerateTags().iterator();

				while (tags.hasNext()) {

					Map.Entry<String, Integer> tag = tags.next();
					int index = idPairs.get(tag.getKey());

					if (index > numTags) {
						System.out.println("ERROR: INDEX OUT OF BOUNDS!");
						System.out.println("name: " + tag.getKey() + " index: "
								+ index);
						System.out.println("numTags: " + numTags);

					} else if (index == numTags) {
						// System.out.println("__OTHER__ detected!");
						inputValues[i][index] += ((double) tag.getValue() / (double) maxTagMag);
					} else {
						inputValues[i][index] = ((double) tag.getValue() / (double) maxTagMag);
					}
				}
				++i;
			}
		}

		NeuralDataSet data = new BasicNeuralDataSet(inputValues, null);

		brain = generateBrain(numTags, numShelfs);
		train(data);

		flatShelfs = new HashMap<Integer, FlatShelf>();

		for (Bookshelf s : basis) {
			FlatShelf fs = new FlatShelf(s);
			flatShelfs.put(checkBook(fs), fs);
		}

		currentWeights = new ButlerWeights(brain.getStructure().getSynapses()
				.get(0).getMatrix(), numTags, flatShelfs, idPairs);
		// initialized = true;
	}

	/**
	 * Sets the weights within brain to those found in the input ButlerWeights
	 * object.
	 * 
	 * @param weights
	 *            the existing weights to copy.
	 */
	public void setMatrixes(ButlerWeights weights) {

		numTags = weights.getNumTags();
		flatShelfs = weights.getShelfs();
		numShelfs = flatShelfs.size();
		brain = generateBrain(numTags, numShelfs);

		brain.getStructure().getSynapses().get(0).setMatrix(
				weights.getWeights());

		currentWeights = weights;

		initialized = true;
	}

	/**
	 * Trains the network with the specified NeuralDataSet.
	 * 
	 * @param input
	 */
	private void train(NeuralDataSet input) {

		final NeuralDataSet theInput = input;
		final VirtualLibraryButler that = this;

		training = new Thread() {
			public void run() {

				org.encog.util.logging.Logging.setConsoleLevel(Level.OFF);

				final Train train = new CompetitiveTraining(brain, 0.7,
						theInput, new NeighborhoodGaussian(
								new GaussianFunction(0.0, 5.0, 1.5)));
				Strategy smartLearn = new SmartLearningRate();

				smartLearn.init(train);
				train.addStrategy(smartLearn);

				int epoch = 0;
				int errorSize = 250;

				double[] lastErrors = new double[errorSize];

				// training loop
				do {
					train.iteration();
					// System.out.println("Epoch #" + epoch + " Error:" +
					// train.getError()); // + " MovingAvg:" + movingAvg);
					lastErrors[epoch % errorSize] = train.getError();

					double avg = 0;
					for (int i = 0; (i < epoch) && (i < errorSize); ++i)
						avg = (avg * i + lastErrors[i]) / (i + 1);

					if (Math.abs(avg - train.getError()) < 0.01)
						train.setError(0.001);

					epoch++;
				} while (train.getError() > 0.01);

				// System.out.println("training complete.");

				that.initialized = true;
			}
		};

		training.start();
	}

	private boolean isTraining() {
		if (null == training)
			return false;
		else
			return training.isAlive();
	}

}
