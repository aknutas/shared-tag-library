package main;

import javax.swing.tree.DefaultMutableTreeNode;

import data.Bookshelf;

public class TreeNode extends DefaultMutableTreeNode {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4933175836804126578L;

    private String name;

    private Bookshelf shelf;

    public TreeNode(Bookshelf b) {
	shelf = b;
	name = shelf.getProperty("name");
    }

    public String getName() {
	return name;
    }

    public Bookshelf getShelf() {
	return shelf;
    }

    @Override
    public TreeNode getUserObject() {
	return this;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setShelf(Bookshelf shelf) {
	this.shelf = shelf;
    }

    @Override
    public String toString() {
	return "[ " + name + " ]";
    }
}
