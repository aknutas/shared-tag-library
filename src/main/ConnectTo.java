package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;

import controller.Controller;
import data.RemoteObjectException;

public class ConnectTo extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField addressField = null;
    private JButton cancelButton = null;
    private JButton connectButton = null;
    private Controller control = null;
    private JTextField ipAlias = null;
    private JPanel jContentPane = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private TreeView tree = null;

    /**
     * @param owner
     * @param treeView
     */
    public ConnectTo(Frame owner, Controller ctl, TreeView treeView) {
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
    private JTextField address() {
	if (addressField == null) {
	    addressField = new JTextField(14);
	}
	return addressField;
    }

    /**
     * This method initializes ipAlias
     * 
     * @return javax.swing.JTextField
     */
    private JTextField alias() {
	if (ipAlias == null) {
	    ipAlias = new JTextField(14);
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
    private JButton connect() {
	if (connectButton == null) {
	    connectButton = new JButton("Connect");
	    connectButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    try {
			control.addConnection(addressField.getText(), ipAlias
				.getText());
			tree.refresh();
		    } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    } catch (RemoteObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    setVisible(false);
		}
	    });
	}
	return connectButton;
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
	    jLabel1 = new JLabel();
	    jLabel1.setText("Alias: ");

	    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
	    gridBagConstraints4.gridx = 2;
	    gridBagConstraints4.gridy = 2;
	    gridBagConstraints4.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
	    gridBagConstraints3.gridx = 1;
	    gridBagConstraints3.gridy = 2;
	    gridBagConstraints3.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	    gridBagConstraints2.gridx = 0;
	    gridBagConstraints2.gridy = 1;
	    gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
	    jLabel = new JLabel();
	    jLabel.setText("IP Address: ");
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints1.gridy = 0;
	    gridBagConstraints1.gridwidth = 2;
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.gridy = 1;
	    gridBagConstraints.gridwidth = 2;
	    gridBagConstraints.gridx = 1;
	    gridBagConstraints.insets = new Insets(2, 2, 2, 2);
	    jContentPane = new JPanel();
	    jContentPane.setLayout(new GridBagLayout());

	    jContentPane.add(alias(), gridBagConstraints1);
	    jContentPane.add(address(), gridBagConstraints);
	    jContentPane.add(jLabel, gridBagConstraints2);
	    jContentPane.add(connect(), gridBagConstraints3);
	    jContentPane.add(cancel(), gridBagConstraints4);
	    jContentPane.add(jLabel1, gridBagConstraints21);
	}
	return jContentPane;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(284, 138);
	this.setContentPane(getJContentPane());
	this.setTitle("Connect To Server");
    }

} // @jve:decl-index=0:visual-constraint="10,10"
