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
	}	
	
}
