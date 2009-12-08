package butler;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.ContextLayer;
import org.encog.neural.networks.synapse.Synapse;
import org.encog.neural.networks.training.Strategy;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.competitive.CompetitiveTraining;
import org.encog.neural.networks.training.competitive.neighborhood.NeighborhoodGaussian;
import org.encog.neural.networks.training.strategy.SmartLearningRate;
import org.encog.neural.pattern.SOMPattern;
import org.encog.util.math.rbf.GaussianFunction;

import data.Book;
import data.Bookshelf;
import data.IDPair;
import data.IDPairSet;
import data.VirtualBook;
import data.VirtualBookshelf;

/**
 * The LibraryButler class contains a Self-Organizing Map that is
 * trained with a Collection of VirtualBookshelfs to classify books
 * into these shelfs.
 * 
 * @author sjpurol
 *
 */
public class LibraryButler implements Butler {

	private IDPairSet idPairs;
	private ButlerWeights currentWeights;
	private BasicNetwork brain;
	private int numTags;
	private int numShelfs;
	private static String OTHER = "__OTHER__";
	private Bookshelf[] shelfNames;

	/**
	 * Default constructor. Initializes this with a new BasicNetwork brain.
	 */
	public LibraryButler(){
		brain = new BasicNetwork();
	}

	/**
	 * Creates this with an existing ButlerWeights object
	 * @param weights the existing ButlerWeights object.
	 */
	public LibraryButler(ButlerWeights weights){
		brain = new BasicNetwork();
		setMatrixes(weights);
	}

	@Override
	/**
	 * Checks an input book against the trained network.
	 * Returns the index of the shelf that best matches.
	 * If no shelf matches, returns -1.
	 * 
	 * @param b The book to check.
	 */
	public int compareTo(Book b) {

		Iterator<Map.Entry<String, Integer>> tags = b.enumerateTags();
		//Iterator<Map.Entry<String, String>> properties = b.enumerateProperties();

		//InputPairSet inputPairs = new InputPairSet();	// neuron # -> weight

		double[] inputValues = new double[numTags];

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());

			IDPair current = idPairs.get(tag.getKey());
			if (current == null) {
				current = idPairs.get(OTHER);
				inputValues[current.getValue()] += tag.getValue();
			}
			else
				inputValues[current.getValue()] = tag.getValue();

			//inputPairs.add(new InputPair(current.getValue(), (double)tag.getValue()));

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

		System.out.println(b.getProperty("title") + ": " + index);

		return index;
	}

	/**
	 * Exactly the same as compareTo(Book) except that it returns
	 * the exact output vector returned by the network.
	 * 
	 * Note: The value of the boolean does not matter.
	 * It is only there to distinguish between the two
	 * compareTo methods. 
	 * 
	 * @param b Book to examine
	 * @param bool 
	 * @return the output vector from brain
	 */
	public double[] compareTo(Book b, boolean bool){

		Iterator<Map.Entry<String, Integer>> tags = b.enumerateTags();
		//Iterator<Map.Entry<String, String>> properties = b.enumerateProperties();

		//InputPairSet inputPairs = new InputPairSet();	// neuron # -> weight

		double[] inputValues = new double[numTags];

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());
			IDPair current = idPairs.get(tag.getKey());
			if (current == null){
				//current = idPairs.get(OTHER);
				//inputValues[current.getValue()] += tag.getValue();
			}
			else
				inputValues[current.getValue()] = tag.getValue();

			//inputPairs.add(new InputPair(current.getValue(), (double)tag.getValue()));

		}

		NeuralData book = new BasicNeuralData(inputValues);
		final NeuralData output = brain.compute(book);

		double[] rawOutput = output.getData();
		String writeOut = "";

		for (int j = 0; j < output.size(); ++j) {
			writeOut += String.valueOf(rawOutput[j]) + " ";
		}

		System.out.println(b.getProperty("title") + ": " + writeOut);

		return rawOutput;
	}

	public ButlerWeights getWeights(){return currentWeights;}

	/**
	 * Sets the weights within brain to those found in the input
	 * ButlerWeights object.
	 * @param weights the existing weights to copy.
	 */
	public void setMatrixes(ButlerWeights weights){

		numTags = weights.getNumTags();

		brain.addLayer(new ContextLayer(numTags));
		brain.addLayer(new ContextLayer(1));

		brain.getStructure().finalizeStructure();

		Iterator<Synapse> synapses = brain.getStructure().getSynapses().iterator();
		synapses.next().setMatrix(weights.getInputWeights());
		synapses.next().setMatrix(weights.getOutputWeights());
		currentWeights = weights;
	}

	/**
	 * Initializes brain from basis
	 * @param basis books to return 1.0
	 * @throws IllegalArgumentException
	 */
	public void initialize(Collection<VirtualBookshelf> basis) throws IllegalArgumentException {

		idPairs = new IDPairSet();	//tag | property -> neuron #

		if (null == basis)
			throw new IllegalArgumentException("input cannot be null.");

		Iterator<VirtualBookshelf> shelfs = basis.iterator();

		numShelfs = basis.size();

		shelfNames = new Bookshelf[numShelfs];

		//System.out.println("numShelfs: " + String.valueOf(numShelfs));

		int maxTagMag = 0;

		int i = 0;
		int j = 0;

		while (shelfs.hasNext()) {

			VirtualBookshelf current = shelfs.next();
			shelfNames[j] = current;

			Iterator<Book> books = current.enumerate();

			while (books.hasNext()) {
				Book book = books.next();
				Iterator<Map.Entry<String, Integer>> tags = book.enumerateTags(); 

				while (tags.hasNext()) {

					Map.Entry<String, Integer> tag = tags.next();

					maxTagMag = Math.max(maxTagMag, tag.getValue());

					if (!idPairs.contains(tag.getKey())) {
						idPairs.add(new IDPair(tag.getKey(), i));
						++i;
					}
				}
			}

		}

		idPairs.add(new IDPair(OTHER, i));

		numTags = i+1;

		double[][] inputValues = new double[numShelfs][numTags];

		shelfs = basis.iterator();
		i = 0;

		while (shelfs.hasNext()) {
			VirtualBookshelf current = shelfs.next();
			Iterator<Book> books = current.enumerate();

			while (books.hasNext()) {
				Book book = books.next();

				Iterator<Map.Entry<String, Integer>> tags = book.enumerateTags();

				while (tags.hasNext()) {
					Map.Entry<String, Integer> tag = tags.next();
					int index = idPairs.get(tag.getKey()).getValue();

					if (index > numTags) {
						System.out.println("ERROR: INDEX OUT OF BOUNDS!");
						System.out.println("name: " + tag.getKey() + " index: " + index);
						System.out.println("numTags: " + numTags);

					}
					else if (index == numTags) {
						//System.out.println("__OTHER__ detected!");
						inputValues[i][index] += ( (double)tag.getValue() / (double) maxTagMag );
					}
					else {
						inputValues[i][index] = ( (double)tag.getValue() / (double) maxTagMag );

						//System.out.println("Tag #" + String.valueOf(index) + " " + tag.getKey() + ": had a weight of " + String.valueOf(tag.getValue()));
						//System.out.println("It now has a weight of " + String.valueOf(inputValues[i][index]));
					}
				}
			}
			++i;
		}

		String output[] = {"",""};

		int countTags = 0;
		int countShelfs = 0;

		for (int k = 0; k < numShelfs; ++k) {

			++countShelfs;
			for (int m = 0; m < numTags; ++m) {
				output[j] += " " + String.valueOf(inputValues[j][k]);
				++countTags;
			}
		}

		//int realTagNum = countTags / countShelfs;
		//System.out.println("There were " + realTagNum + " tags counted on " + countShelfs + " shelfs.");
		//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");
		//for (int k = 0; k < countShelfs; ++k)
		//	System.out.println(output[k]);
		//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX");

		SOMPattern pattern = new SOMPattern();
		pattern.setInputNeurons(numTags);
		pattern.setOutputNeurons(numShelfs);

		brain = pattern.generate();

		//brain.addLayer(new ContextLayer(numTags));
		//brain.addLayer(new ContextLayer(numShelfs));
		//brain.setLogic(new SOMLogic());

		//brain.getStructure().finalizeStructure();

		NeuralDataSet data = new BasicNeuralDataSet(inputValues, null);

		//final Train train = new CompetitiveTraining(brain, 0.7, data, new NeighborhoodBubble(2));
		final Train train = new CompetitiveTraining(brain, 0.7, data, new NeighborhoodGaussian(new GaussianFunction(0.0, 5.0, 2.0)));
		//Strategy stopTrainStrat = new StopTrainingStrategy();
		Strategy smartLearn = new SmartLearningRate();
		smartLearn.init(train);
		//stopTrainStrat.init(train);
		train.addStrategy(smartLearn);
		//train.addStrategy(stopTrainStrat);

		int epoch = 1;

		// training loop
		do {
			train.iteration();
			//System.out.println("Epoch #" + epoch + " Error:" + train.getError()); // + " MovingAvg:" + movingAvg);
			//lastError = train.getError();
			//movingAvg = (movingAvg*(double)(epoch-1) + lastError*1.001)/(double)epoch;
			//if (((lastError - movingAvg) < 0.01) && lastError > 0.01){
			//	train.setError(lastError * 5.0);
			//	System.out.println("LOCAL MINIMA FOUND!");
			//}
			epoch++;
		} while(train.getError() > 0.01);

		//System.out.println("Number of Synapses: " + brain.getStructure().getSynapses().size());

		currentWeights = new ButlerWeights(brain.getStructure().getSynapses().get(0).getMatrix(), numTags);

		//Iterator<Synapse> currentWeights = brain.getStructure().getSynapses().iterator();
		//Matrix input = currentWeights.next().getMatrix();
		//this.currentWeights = new ButlerWeights(input, numTags);

		// test the neural network
		//System.out.println("Neural Network Results:");
		//String tagNames = "";
		//for(int k = 0; k < idPairs.size()-1; ++k){
		//	tagNames += idPairs.get(k).getKey() + ", ";
		//}
		//System.out.println(tagNames);

		/*for( NeuralDataPair pair : data ) {
			final NeuralData output = brain.compute(pair.getInput());
			String values = String.valueOf(pair.getInput().getData(0));
			for (int k = 1; k < pair.getInput().size(); ++k){
				values += ", " + pair.getInput().getData(k); 
			}

			System.out.println(values + " actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

		}*/

		//System.out.println("numTags: " + numTags);
	}

	public static void main(String[] args){
		//ScriptGenerator sg = new ScriptGenerator("src\\scripts\\en_US.dic");
		//sg.generateLibrary(20, 5);

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

		int[] greenWeights = {760,510,420,370,290,640,710,240,360,40,30,20,20,20,20,
				20,20,20,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,
				10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10};

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

		int[] itWeights = {84,73,26,21,18,13,10,10,9,8,4,3,3,2,2,2,2,2,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1, -100};

		for (int i = 0; i < itTags.length; ++i){
			int num = itWeights[i];
			while (num > 0){
				it.tag(itTags[i]);
				--num;
			}
		}
		VirtualBookshelf shelf = new VirtualBookshelf();
		shelf.insert(it);

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

		int[] salemWeights = {84,74,47,13,12,9,7,3,3,3,3,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,-523};

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
		int[] interviewWeights = {134,67,36,18,10,9,7,5,5,4,3,3,3,3,2,2,2,
				2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1, -5982};

		for (int i = 0; i < interviewTags.length; ++i){
			int num = interviewWeights[i];

			while (num > 0){
				interview.tag(interviewTags[i]);
				--num;
			}
		}

		LibraryButler buddy = new LibraryButler();
		//VirtualFlatShelf flatBS = new VirtualFlatShelf(bs);
		//System.out.println("created flatBS");
		//List<VirtualFlatShelf> flatShelfs = new ArrayList<VirtualFlatShelf>();
		//flatShelfs.add(flatBS);
		VirtualBookshelf horror = new VirtualBookshelf();

		horror.setProperty("name", "horror");
		bs.setProperty("name", "Seussville");

		horror.insert(interview);
		horror.insert(salem);
		horror.insert(it);

		//VirtualFlatShelf flatHorror = new VirtualFlatShelf(horror);
		//flatShelfs.add(flatHorror);

		//buddy.initialize(flatShelfs);
		Set<VirtualBookshelf> allShelfs = new HashSet<VirtualBookshelf>();
		allShelfs.add(horror);
		allShelfs.add(bs);

		buddy.initialize(allShelfs);

		//System.out.println(catBack.getProperty("title") + ": " + 
		buddy.compareTo(catBack);
		//System.out.println("The Cat in the Hat Comes Back results: " + results);
		//System.out.println(salem.getProperty("title") + ": " + 
		buddy.compareTo(salem);
		//System.out.println("Salems' Lot results: " + results);
		//System.out.println(interview.getProperty("title") + ": " + 
		buddy.compareTo(interview);
		//System.out.println("Interview with a Vampire results: " + results);
		//System.out.println(cat.getProperty("title") + ": " + 
		buddy.compareTo(cat);
		//System.out.println("The Cat in the Hat results: " + results);
		//System.out.println(fish.getProperty("title") + ": " + 
		buddy.compareTo(fish);
		//System.out.println("One Fish Two Fish Red Fish Blue Fish results: " + results);
		//buddy.firstTrainingData(sg.p.lib);

	}

}
