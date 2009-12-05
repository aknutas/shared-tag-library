package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.RemoteObjectException;
import java.awt.Dimension;

public class Disconnect extends JDialog {

    private static final long serialVersionUID = 1L;
    private JLabel availableConnections = null;
    private JButton cancelButton = null;
    private Controller control = null;
    private JButton disconnectButton = null;
    private JComboBox ipAlias = null;
    private JPanel jContentPane = null;
    private TreeView tree = null;

    /**
     * @param owner
     * @param treeView
     */
    public Disconnect(Frame owner, Controller ctl, TreeView treeView) {
	super(owner);
	tree = treeView;
	control = ctl;
	initialize();
    }

    /**
     * This method initializes ipAlias
     * 
     * @return javax.swing.JTextField
     */
    private JComboBox alias() {
	if (ipAlias == null) {

	    if (control.getConnections() == null) {
		ipAlias = new JComboBox();
		ipAlias.setEnabled(false);
	    } else {
		ipAlias = new JComboBox((String[]) control.getConnections()
			.toArray(new String[0]));

		ipAlias.addKeyListener(new KeyAdapter() {
		    public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ENTER) {
			    disconnectButton.doClick();
			} else if (key == KeyEvent.VK_ESCAPE) {
			    cancelButton.doClick();
			}
		    }
		});
	    }
	}
	return ipAlias;
    }

    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     * 
     */
    private JButton cancel() {
	if (cancelButton == null) {
	    cancelButton = new JButton("Cancel");
	    cancelButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    setVisible(false);

		}
	    });
	}
	return cancelButton;
    }

    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton disconnect() {
	if (disconnectButton == null) {
	    disconnectButton = new JButton("Disconnect");
	    disconnectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    try {
			control.breakConnection((String) ipAlias
				.getSelectedItem());
			tree.refresh();
		    } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    setVisible(false);
		}
	    });
	}
	return disconnectButton;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    try {
		UIManager
			.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
	    } catch (UnsupportedLookAndFeelException ex) {
		System.out
			.println("Cannot set new Theme for Java Look and Feel.");
	    }

	    GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
	    gridBagConstraints21.gridx = 0;
	    gridBagConstraints21.gridy = 0;
	    gridBagConstraints21.anchor = GridBagConstraints.EAST;
	    gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
	    availableConnections = new JLabel();
	    availableConnections.setText("Connections: ");

	    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
	    gridBagConstraints4.gridx = 2;
	    gridBagConstraints4.gridy = 1;
	    gridBagConstraints4.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
	    gridBagConstraints3.gridx = 1;
	    gridBagConstraints3.gridy = 1;
	    gridBagConstraints3.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints1.gridy = 0;
	    gridBagConstraints1.gridwidth = 2;
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new GridBagLayout());

	    jContentPane.add(alias(), gridBagConstraints1);
	    jContentPane.add(disconnect(), gridBagConstraints3);
	    jContentPane.add(cancel(), gridBagConstraints4);
	    jContentPane.add(availableConnections, gridBagConstraints21);
	}
	return jContentPane;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(341, 137);
	this.setContentPane(getJContentPane());
	this.setTitle("Disconnect From Server");
    }

} // @jve:decl-index=0:visual-constraint="10,10"
