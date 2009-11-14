package butler;

import java.util.Iterator;
import java.util.Map;

import operations.BookshelfOperations;
import operations.LibraryOperations;

import org.encog.matrix.Matrix;
import org.encog.neural.data.NeuralData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.ContextLayer;
import org.encog.neural.networks.synapse.Synapse;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

import data.Book;
import data.Bookshelf;
import data.IDPair;
import data.IDPairSet;
import data.VirtualBook;
import data.VirtualBookshelf;

public class Butler{

	private IDPairSet idPairs;
	private ButlerWeights currentWeights;
	private BasicNetwork brain;
	private int numTags;
	private static final String OTHER = "___OTHER___";

	/**
	 * Default constructor. Initializes this with a new BasicNetwork brain.
	 */
	public Butler(){
		brain = new BasicNetwork();
	}
	
	/**
	 * Creates this with an existing ButlerWeights object
	 * @param weights the existing ButlerWeights object.
	 */
	public Butler(ButlerWeights weights){
		brain = new BasicNetwork();
		setMatrixes(weights);
	}

	/**
	 * Initializes brain from basis and antiBasis
	 * @param basis books to return 1.0
	 * @param antiBasis books to return -1.0
	 * @throws IllegalArgumentException
	 */
	public void firstTrainingData(Bookshelf basis, Bookshelf antiBasis) throws IllegalArgumentException{

		if (null == basis)
			throw new IllegalArgumentException("basis cannot be null.");
		if (null == antiBasis)
			throw new IllegalArgumentException("antiBasis cannot be null.");

		Iterator<Map.Entry<String, Integer>> tags = BookshelfOperations.enumerateTags(basis);
		int numBooks = LibraryOperations.getNumBooks();
		numTags = LibraryOperations.getNumTags();
		//System.out.println("numTags = " + numTags);
		Iterator<Map.Entry<String, Integer>> antiTags = BookshelfOperations.enumerateTags(antiBasis);
		numBooks += LibraryOperations.getNumBooks();
		numTags += LibraryOperations.getNumTags() + 1;
		//System.out.println("numTags = " + numTags);

		brain.addLayer(new ContextLayer(numTags));
		brain.addLayer(new ContextLayer(1));

		brain.getStructure().finalizeStructure();


		//Iterator<Map.Entry<String, String>> properties = LibraryOperations.getProperties(basis);

		idPairs = new IDPairSet();	//tag | property -> neuron #
		//InputPairSet inputPairs = new InputPairSet();	// neuron # -> weight

		double[][] inputValues = new double[numBooks][numTags];
		double[][] expected = new double[numBooks][1];

		int i = 0;

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();

			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());
			//inputValues[0][i] = (double)tag.getValue();
			
			if (!idPairs.contains(tag.getKey())){
				idPairs.add(new IDPair(tag.getKey(),i));

				//inputPairs.add(new InputPair(i, (double)tag.getValue()));
				i++;
			}
		}
		while (antiTags.hasNext()){
			Map.Entry<String, Integer> antiTag = antiTags.next();

			if (!idPairs.contains(antiTag.getKey())){
				idPairs.add(new IDPair(antiTag.getKey(),i));
				i++;
			}
		}

		idPairs.add(new IDPair(OTHER, i));

		//System.out.println("there are: " + i + " tags.");

		//String[] names = new String[numBooks];

		Iterator<Book> basisBooks = basis.iterator();
		Iterator<Book> antiBasisBooks = antiBasis.iterator();
		int j = 0;
		while (basisBooks.hasNext()){
			Book current = basisBooks.next();
			Iterator<Map.Entry<String, Integer>> currentTags = current.enumerateTags();
			while (currentTags.hasNext()){
				Map.Entry<String, Integer> curTag = currentTags.next();
				int index = idPairs.get(curTag.getKey()).getValue();
				//if (curTag.getValue() != 0)
				if (index > numTags)
					System.out.println("name: " + curTag.getKey() + " index: " + index);
				else
					inputValues[j][index] = curTag.getValue();

				//else if (curTag.getValue() == 0)
				//inputValues[j][index] = -1.0;
			}
			expected[j][0] = 1.0;
			//names[j] = current.getProperty("title");
			++j;

		}
		while (antiBasisBooks.hasNext()){
			Book current = antiBasisBooks.next();
			Iterator<Map.Entry<String, Integer>> currentTags = current.enumerateTags();
			while (currentTags.hasNext()){
				Map.Entry<String, Integer> curTag = currentTags.next();
				int index = idPairs.get(curTag.getKey()).getValue();
				//if (curTag.getValue() != 0)

				inputValues[j][index] = curTag.getValue();

				//else if (curTag.getValue() == 0)
				//inputValues[j][index] = -1.0;
			}
			expected[j][0] = -1.0;
			//names[j] = current.getProperty("title");
			++j;

		}


		NeuralDataSet data = new BasicNeuralDataSet(inputValues, expected);

		final Train train = new ResilientPropagation(brain, data);

		int epoch = 1;
		//double lastError = 1;
		//double movingAvg = 0.0;
		//int counter = 0;
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

		Iterator<Synapse> currentWeights = brain.getStructure().getSynapses().iterator();
		Matrix input = currentWeights.next().getMatrix();
		Matrix output = currentWeights.next().getMatrix();
		this.currentWeights = new ButlerWeights(input, output, numTags);
		
		// test the neural network
		//System.out.println("Neural Network Results:");
		//String tagNames = "";
		//for(int k = 0; k < idPairs.size()-1; ++k){
		//	tagNames += idPairs.get(k).getKey() + ", ";
		//}
		//System.out.println(tagNames);

		//for(NeuralDataPair pair: data ) {
		//	final NeuralData output = brain.compute(pair.getInput());
		//	String values = String.valueOf(pair.getInput().getData(0));
		//	for (int k = 1; k < pair.getInput().size(); ++k){
		//		values += ", " + pair.getInput().getData(k); 
		//	}
		//	
		//	System.out.println(values + " actual=" + output.getData(0) + ",ideal=" + pair.getIdeal().getData(0));

		//}

		//System.out.println("numTags: " + numTags);

	}

	/**
	 * Returns a double between -1.0 and 1.0 indicating how likely the input book
	 * would belong with the learned books.
	 * @param b the book to examine
	 * @return see description
	 */
	
	public double examineBook(Book b){

		Iterator<Map.Entry<String, Integer>> tags = b.enumerateTags();
		//Iterator<Map.Entry<String, String>> properties = b.enumerateProperties();

		//InputPairSet inputPairs = new InputPairSet();	// neuron # -> weight

		double[] inputValues = new double[numTags];

		while (tags.hasNext()){
			Map.Entry<String, Integer> tag = tags.next();
			//System.out.println("tags: " + tag.getKey() + " : " + tag.getValue());
			IDPair current = idPairs.get(tag.getKey());
			if (current == null){
				current = idPairs.get(OTHER);
				inputValues[current.getValue()] += tag.getValue();
			}
			else
				inputValues[current.getValue()] = tag.getValue();

			//inputPairs.add(new InputPair(current.getValue(), (double)tag.getValue()));

		}

		NeuralData book = new BasicNeuralData(inputValues);
		final NeuralData output = brain.compute(book);

		return output.getData(0);

	}
	
	/**
	 * Sets the weights within brain to those found in the input
	 * ButlerWeights object.
	 * @param weights the existing weights to copy.
	 */
	protected void setMatrixes(ButlerWeights weights){
		
		numTags = weights.getNumTags();
		
		brain.addLayer(new ContextLayer(numTags));
		brain.addLayer(new ContextLayer(1));

		brain.getStructure().finalizeStructure();
		
		Iterator<Synapse> synapses = brain.getStructure().getSynapses().iterator();
		synapses.next().setMatrix(weights.getInputWeights());
		synapses.next().setMatrix(weights.getOutputWeights());
	}
	
	public static void main(String[] args){
		//ScriptGenerator sg = new ScriptGenerator("src\\scripts\\en_US.dic");
		//sg.generateLibrary(20, 5);

		Bookshelf bs = new VirtualBookshelf();

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

		int[] greenWeights = {76,51,22,21,19,16,11,6,5,4,3,2,2,2,2,
				2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

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
		int[] catWeights = {6,3,2,2,2,1,1,1,1,1,1,1,1,1};
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
				"urban fantasy","wordy"};

		int[] itWeights = {84,73,26,21,18,13,10,10,9,8,4,3,3,2,2,2,2,2,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1};

		for (int i = 0; i < itTags.length; ++i){
			int num = itWeights[i];
			while (num > 0){
				it.tag(itTags[i]);
				--num;
			}
		}
		Bookshelf shelf = new VirtualBookshelf();
		shelf.insert(it);
		Butler buddy = new Butler();
		buddy.firstTrainingData(bs, shelf);

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
				"vampire books","vamps","writer"};
		
		int[] salemWeights = {84,74,47,13,12,9,7,3,3,3,3,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
		
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
				"women writers"};
		int[] interviewWeights = {134,67,36,18,10,9,7,5,5,4,3,3,3,3,2,2,2,
				2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
				1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

		for (int i = 0; i < interviewTags.length; ++i){
			int num = interviewWeights[i];
			while (num > 0){
				interview.tag(interviewTags[i]);
				--num;
			}
		}
		
		double results = buddy.examineBook(catBack);
		System.out.println("The Cat in the Hat Comes Back results: " + results);
		results = buddy.examineBook(salem);
		System.out.println("Salems' Lot results: " + results);
		results = buddy.examineBook(interview);
		System.out.println("Interview with a Vampire results: " + results);
		//buddy.firstTrainingData(sg.p.lib);

	}

}
