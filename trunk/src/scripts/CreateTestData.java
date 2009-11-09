package scripts;

import data.*;

public class CreateTestData {

	public static void main(String []args) {
		database.QueryBuilder database = new database.QueryBuilderImpl();
		Library library = new PersistentLibrary(database);
		
		VirtualBookshelf shelf1 = new VirtualBookshelf("Computer Science");
		shelf1.insert(new VirtualBook("Objects Have Class", "Popslowski"));
		shelf1.insert(new VirtualBook("Data Structures in Java", "Smith"));
		shelf1.insert(new VirtualBook("Discrete Structures", "Jones"));
		shelf1.insert(new VirtualBook("Programming in C++", "Stroustrap"));
		shelf1.insert(new VirtualBook("Languages and Machines", "Winston"));
		shelf1.insert(new VirtualBook("Algorithm Design", "Smith"));
		shelf1.insert(new VirtualBook("Parallel Programming", "Davidson"));
		shelf1.insert(new VirtualBook("Computer Architecture", "Jones"));
		shelf1.insert(new VirtualBook("Practical Lisp", "Stalman"));
		shelf1.insert(new VirtualBook("Software Engineering Techniques", "Winston"));
		shelf1.insert(new VirtualBook("Windows 95 for Dummy's", "Gates"));
		((PersistentLibrary)library).saveBookshelf(shelf1);
		
		VirtualBookshelf shelf2 = new VirtualBookshelf("Classic Cannon");
		shelf2.insert(new VirtualBook("The Old Man and the Sea", "Hemingway"));
		shelf2.insert(new VirtualBook("Catcher in the Rye", "Salinger"));
		shelf2.insert(new VirtualBook("Grapes of Wrath", "Steinbeck"));
		shelf2.insert(new VirtualBook("The Adventures of Huckleberry Fin", "Twain"));
		shelf2.insert(new VirtualBook("The Great Gatsby", "Fitzgeralrd"));
		shelf2.insert(new VirtualBook("Lord of the Flies", "Golding"));
		shelf2.insert(new VirtualBook("Moby Dick", "Melville"));
		shelf2.insert(new VirtualBook("Jane Eyre", "Bront"));
		shelf2.insert(new VirtualBook("For Whom the Bell Tolls", "Hemingway"));
		((PersistentLibrary)library).saveBookshelf(shelf2);
		
		VirtualBookshelf shelf3 = new VirtualBookshelf("Seussville");
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

		shelf3.insert(green);

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

		shelf3.insert(cat);

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

		shelf3.insert(fish);

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

		for (int i = 0; i < placesTags.length; ++i){
			int num = placesWeights[i];
			while (num > 0){
				places.tag(placesTags[i]);
				--num;
			}

		}

		shelf3.insert(places);
		((PersistentLibrary)library).saveBookshelf(shelf3);
		
	}	
	
}
