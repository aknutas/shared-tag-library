package scripts;

import data.*;

public class CreateTestData {

    public static void main(String[] args) {
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
	shelf1.insert(new VirtualBook("Software Engineering Techniques",
		"Winston"));
	shelf1.insert(new VirtualBook("Windows 95 for Dummy's", "Gates"));
	((PersistentLibrary) library).saveBookshelf(shelf1);

	VirtualBookshelf shelf2 = new VirtualBookshelf("Classic Cannon");
	VirtualBook oldMan = new VirtualBook("The Old Man and the Sea",
		"Hemingway");

	// tagging Old Man and the Sea
	String[] oldManTags = { "classic literature", "ernest hemingway",
		"american literature", "classics", "hemingway", "fiction",
		"classic", "book", "literature", "novel",
		"20th century american fiction", "fishing", "20th century",
		"marlin", "united states", "1950s",
		"20th century american literature", "20th century lit",
		"american", "american fiction", "assigned reading",
		"author suicides", "awesome", "bait", "book recommendations",
		"bookshop", "caribbean coctail", "carlos castaneda",
		"classic fiction", "classic lit", "dead souls and dark alleys",
		"deep-sea fishing", "defeat", "determination",
		"dont look forward", "english", "faith", "favorite books",
		"fear", "fiction classics", "fish story", "fisherman",
		"fishing line", "fishy story", "gift for sister",
		"gift from jeanne", "great books", "harpoon", "history",
		"hummingbird god", "juvenile writing",
		"literature nobel prize winners", "american literature",
		"my books", "nature writing", "nautical",
		"nobel prize in literature", "novella", "old west",
		"overrated classics", "philosophy", "picaresque",
		"pile of crap", "pippi longstocking", "pretentious garbage",
		"pulitzer - fiction", "school books", "sharks",
		"short fiction", "spain - portugal - latin america",
		"stars i like it", "timeless literature", "trips and journeys",
		"veronica diaz-reinhagen", "veronica martino" };

	int[] oldManWeights = { 51, 50, 34, 29, 25, 24, 23, 12, 12, 6, 5, 3, 2,
		2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

	for (int i = 0; i < oldManTags.length; ++i) {
	    int num = oldManWeights[i];
	    while (num > 0) {
		oldMan.tag(oldManTags[i]);
		--num;
	    }
	}

	shelf2.insert(oldMan);

	VirtualBook rye = new VirtualBook("Catcher in the Rye", "Salinger");

	String[] ryeTags = { "coming of age (98)", "classic literature (93)",
		"jd salinger (66)", "classic fiction (56)",
		"catcher in the rye (47)", "teen (47)", "classic (44)",
		"holden caulfield (20)", "fiction (17)",
		"20th century american fiction (15)", "literature (13)",
		"classics (6)", "salinger (6)", "9 99boycott (4)",
		"the lost episodes of beatie scareli (4)",
		"american literature (3)", "book (3)",
		"book recommendations (3)", "ginnetta correli (3)",
		"black comedy (2)", "holden (2)", "literary fiction (2)",
		"novel (2)", "outsider writers (2)", "rye (2)",
		"the catcher in the rye (2)", "waste of time (2)",
		"0316769177 (1)", "9780316769174 (1)", "a space between (1)",
		"ackley (1)", "add (1)", "angst (1)", "anxiety (1)",
		"	audrey michelle (1)", "bazhe (1)", "best (1)",
		"bildungsroman (1)", "carl (1)", "carol rose (1)",
		"catche (1)", "catcher (1)", "catcher rye (1)",
		"classic american novel (1)", "classic lit (1)",
		"classic novel (1)", "come out of the cave (1)",
		"communication (1)", "controversial (1)", "cynicism (1)",
		"depression (1)", "disillusionment (1)", "ed roberts (1)",
		"emo catcher in the rye teenager (1)",
		"fiction that reads like a biography (1)", "flit (1)",
		"generational issues (1)", "glenn thater (1)", "grief (1)",
		"historical fiction (1)", "holden caufield (1)",
		"holden caufiled salingner (1)", "homeschooling (1)",
		"horrible literary masterpieces (1)", "humor (1)", "indie (1)",
		"innocence (1)", "j d salinger (1)", "jane (1)", "jd (1)",
		"john lennon (1)", "john lennon murderer (1)",
		"john lennon s murderer (1)", "late writer (1)",
		"lit american (1)", "literary (1)", "masculinity (1)",
		"mental illness (1)", "must reads (1)", "narrator (1)",
		"new age (1)", "new york city (1)", "new york stories (1)",
		"nick harris (1)", "over-rated (1)", "own book (1)",
		"period piece (1)", "personal psychoanalysis by holden (1)",
		"phoebe (1)", "picaresque (1)", "piedmont reading club (1)",
		"posttraumatic stress disorder (1)", "pulitzer - drama (1)",
		"really do (1)", "rebellion (1)", "red book (1)",
		"school books (1)", "stradlater (1)", "style (1)",
		"suspense (1)", "swear word in every sentence (1)",
		"teen angst (1)", "teen literature (1)",
		"teenage drinking (1)", "the bathsheba deadline (1)",
		"the catcher in the rye taiwanese view (1)",
		"the days of the bitter end (1)", "the fallen angle (1)",
		"the gateway (1)", "the last panther (1)",
		"too much profanity in a book (1)", "ugh (1)", "useful (1)",
		"voice (1)", "ya fiction (1)", "young boy (1)" };

	int[] ryeWeights = { 98, 93, 66, 56, 47, 47, 44, 20, 17, 15, 13, 6, 6,
		4, 4, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

	for (int i = 0; i < ryeTags.length; ++i) {
	    int num = ryeWeights[i];
	    while (num > 0) {
		oldMan.tag(ryeTags[i]);
		--num;
	    }
	}

	shelf2.insert(rye);

	shelf2.insert(new VirtualBook("Grapes of Wrath", "Steinbeck"));
	shelf2.insert(new VirtualBook("The Adventures of Huckleberry Fin",
		"Twain"));
	shelf2.insert(new VirtualBook("The Great Gatsby", "Fitzgeralrd"));
	shelf2.insert(new VirtualBook("Lord of the Flies", "Golding"));
	shelf2.insert(new VirtualBook("Moby Dick", "Melville"));
	shelf2.insert(new VirtualBook("Jane Eyre", "Bront"));
	shelf2.insert(new VirtualBook("For Whom the Bell Tolls", "Hemingway"));

	((PersistentLibrary) library).saveBookshelf(shelf2);

	VirtualBookshelf shelf3 = new VirtualBookshelf("Seussville");
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

	int[] greenWeights = { 76, 51, 22, 21, 19, 16, 11, 6, 5, 4, 3, 2, 2, 2,
		2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
		1, 1 };

	for (int i = 0; i < greenTags.length; ++i) {
	    int num = greenWeights[i];
	    while (num > 0) {
		green.tag(greenTags[i]);
		--num;
	    }
	}

	shelf3.insert(green);

	Book cat = new VirtualBook("The Cat in the Hat", "Dr. Seuss");
	String[] catTags = { "childrens books", "early reader",
		"childrens literature", "kids", "young child books",
		"accelerated reader", "bedtime story",
		"caldecott winning illustrator", "childrens illustration",
		"dr seuss", "elementary", "elementary books", "picture book",
		"seuss" };
	int[] catWeights = { 6, 3, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
	for (int i = 0; i < catTags.length; ++i) {
	    int num = catWeights[i];
	    while (num > 0) {
		cat.tag(catTags[i]);
		--num;
	    }
	}

	shelf3.insert(cat);

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

	shelf3.insert(fish);

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

	for (int i = 0; i < placesTags.length; ++i) {
	    int num = placesWeights[i];
	    while (num > 0) {
		places.tag(placesTags[i]);
		--num;
	    }

	}

	shelf3.insert(places);
	((PersistentLibrary) library).saveBookshelf(shelf3);

    }

}
