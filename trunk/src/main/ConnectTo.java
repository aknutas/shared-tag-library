package main;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

public class ConnectTo extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField addressField = null;
    private JButton cancelButton = null;
    private JButton connectButton = null;
    private JComboBox connectionList = null;
    private Controller control = null;
    private JTextField ipAlias = null;
    private JPanel jContentPane = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel2 = null;
    private Root root = null;
    private TreeView tree = null;

    /**
     * @param owner
     * @param treeView
     */
    public ConnectTo(Frame owner, Controller ctl, TreeView treeView) {
	super(owner);
	root = (Root) owner;
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

	    addressField.addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    int key = e.getKeyCode();
		    if (key == KeyEvent.VK_ENTER) {
			connectButton.doClick();
		    } else if (key == KeyEvent.VK_ESCAPE) {
			cancelButton.doClick();
		    }
		}
	    });
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
			if (addressField.getText().equals("")) {

			    System.out.println((String) connectionList
				    .getSelectedItem());
			    control.connect((String) connectionList
				    .getSelectedItem());
			} else {

			    control.addConnection(ipAlias.getText(), addressField.getText());
			    control.connect(ipAlias.getText());
			}

			if (control.getImportedLibrary(ipAlias.getText()) != null) {

			    ChooseBookshelves dialog = new ChooseBookshelves(
				    root, control, tree, control
					    .getShelveSelection(ipAlias
						    .getText()), ipAlias
						    .getText());
			    dialog.setVisible(true);

			    tree.refresh();
			} else {
			    root.setStatus("Error Connecting");
			}
		    } catch (IllegalArgumentException e) {
			root.setStatus("Error Connecting (Illegal Argument)");
		    } catch (NullPointerException e) {
			root.setStatus("Error Connecting (NULL)");
		    }
		    setVisible(false);
		}
	    });
	}
	return connectButton;
    }

    /**
     * This method initializes jComboBox
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getConnectionList() {
	if (connectionList == null) {
	    connectionList = new JComboBox(new DefaultComboBoxModel(control
		    .getConnections().toArray(new String[0])));
	    connectionList.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {

		    ipAlias.setText("");
		    addressField.setText("");
		}
	    });
	}
	return connectionList;
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
	if (jContentPane == null) {
	    GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
	    gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints41.gridy = 0;
	    gridBagConstraints41.gridwidth = 2;
	    gridBagConstraints41.gridx = 1;
	    gridBagConstraints41.insets = new Insets(2, 2, 2, 2);
	    GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
	    gridBagConstraints31.gridx = 0;
	    gridBagConstraints31.gridy = 0;
	    gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
	    gridBagConstraints31.anchor = GridBagConstraints.EAST;
	    jLabel2 = new JLabel();
	    jLabel2.setText("Past Connections: ");
	    GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
	    gridBagConstraints21.gridx = 0;
	    gridBagConstraints21.gridy = 1;
	    gridBagConstraints21.anchor = GridBagConstraints.EAST;
	    gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
	    jLabel1 = new JLabel();
	    jLabel1.setText("Alias: ");

	    GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
	    gridBagConstraints4.gridx = 2;
	    gridBagConstraints4.gridy = 3;
	    gridBagConstraints4.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
	    gridBagConstraints3.gridx = 1;
	    gridBagConstraints3.gridy = 3;
	    gridBagConstraints3.insets = new Insets(10, 2, 2, 2);
	    GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
	    gridBagConstraints2.gridx = 0;
	    gridBagConstraints2.gridy = 2;
	    gridBagConstraints2.anchor = GridBagConstraints.EAST;
	    gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
	    jLabel = new JLabel();
	    jLabel.setText("IP Address: ");
	    GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
	    gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints1.gridy = 1;
	    gridBagConstraints1.gridwidth = 2;
	    gridBagConstraints1.gridx = 1;
	    gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
	    GridBagConstraints gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
	    gridBagConstraints.gridy = 2;
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
	    jContentPane.add(jLabel2, gridBagConstraints31);
	    jContentPane.add(getConnectionList(), gridBagConstraints41);
	}
	return jContentPane;
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
	this.setSize(381, 173);
	this.setContentPane(getJContentPane());
	this.setTitle("Connect To Server");
    }

} // @jve:decl-index=0:visual-constraint="10,10"
