package main;

import javax.swing.tree.DefaultMutableTreeNode;

import data.Bookshelf;

/**
 * Tree node for bookshelves.
 * 
 * @author patrick
 * 
 */
public class TreeNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = -4933175836804126578L;

    private String name;

    private Bookshelf shelf;

    /**
     * Tree node for bookshelf
     * 
     */
    public TreeNode(Bookshelf b) {
	shelf = b;
	name = shelf.getProperty("name");
    }

    protected String getName() {
	return name;
    }

    protected Bookshelf getShelf() {
	return shelf;
    }

    /**
     * Stores itself for tree retrieval.
     */
    @Override
    public TreeNode getUserObject() {
	return this;
    }

    protected void setName(String name) {
	this.name = name;
    }

    protected void setShelf(Bookshelf shelf) {
	this.shelf = shelf;
    }

    /**
     * Stores its name for display.
     */
    @Override
    public String toString() {
	return "[ " + name + " ]";
    }
}
