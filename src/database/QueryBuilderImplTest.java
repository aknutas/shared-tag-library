package database;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.Book;
import data.Bookshelf;
import data.VirtualBook;
import data.VirtualBookshelf;

@SuppressWarnings("unused")
public class QueryBuilderImplTest {

    // Introducing variables
    QueryBuilder qb;
    Book book;
    Bookshelf shelf;
    List<Bookshelf> returnlist;
    Bookshelf testshelf;

    @Before
    public void setUp() throws Exception {
	qb = new QueryBuilderImpl();
    }

    @After
    public void tearDown() throws Exception {
	qb.shelfRemove(shelf);
	qb = null;
    }

    @Test
    public void testShelfSearchByBookName() {
	shelf = new VirtualBookshelf("");
	shelf.setProperty("name", "Plop");
	book = new VirtualBook("Diiba", "Daaba");
	shelf.insert(book);
	//Here is where we persist it
	qb.shelfStore(shelf);
	
	// Second test query
	returnlist = qb.shelfSearchByBookName("Diiba");
	testshelf = (Bookshelf) returnlist.get(0);
	assert testshelf.getProperty("name").equals(shelf.getProperty("name"));
	assert returnlist.size() == 1;
    }

    @Test
    public void testShelfSearchByProperty() {
	//Creating new shelf
	shelf = new VirtualBookshelf("");
	shelf.setProperty("name", "Plop");
	book = new VirtualBook("Diiba", "Daaba");
	shelf.insert(book);
	//Here is where we persist it
	qb.shelfStore(shelf);

	//First test query
	returnlist = qb.shelfSearchByProperty("name", "Plop");
	testshelf = (Bookshelf) returnlist.get(0);
	assert testshelf.getProperty("name").equals("Plop");
    }

    @Test
    public void testShelfList() {
	//Creating new shelf
	shelf = new VirtualBookshelf("");
	shelf.setProperty("name", "Plop");
	book = new VirtualBook("Diiba", "Daaba");
	shelf.insert(book);
	//Here is where we persist it
	qb.shelfStore(shelf);
	
	assert testshelf.size() == 1;
    }

    @Test
    public void testShelfStore() {
	//Creating new shelf
	shelf = new VirtualBookshelf("");
	shelf.setProperty("name", "Plop");
	book = new VirtualBook("Diiba", "Daaba");
	shelf.insert(book);
	//Here is where we persist it
	qb.shelfStore(shelf);

	//First test query
	returnlist = qb.shelfSearchByProperty("name", "Plop");
	testshelf = (Bookshelf) returnlist.get(0);
	assert testshelf.getProperty("name").equals("Plop");
    }

    @Test
    public void testShelfRemove() {
	//Creating new shelf
	shelf = new VirtualBookshelf("");
	shelf.setProperty("name", "Plop");
	book = new VirtualBook("Diiba", "Daaba");
	shelf.insert(book);
	//Here is where we persist it
	qb.shelfStore(shelf);
	
	qb.shelfRemove(shelf);
	returnlist = qb.shelfList();
	
	assert testshelf.size() == 0;
    }

}
