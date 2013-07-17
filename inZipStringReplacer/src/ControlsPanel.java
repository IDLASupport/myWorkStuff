package stringReplacer;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;

/**
 * 
 * @author Nicholas McNew
 * @version 0.3_03
 */
public class ControlsPanel extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtFindFile;
	private JTextField txtWordsToFind;
	private JTextField txtWordsThatReplace;
	public JButton btnFindButton;
	public String fileLocation;
	private JLabel lblNewLabel;
	public boolean notSeenTxt1 = true;
	public boolean txt1Mod = false;
	public boolean notSeenTxt2 = true;
	public boolean notSeenTxt3 = true;
	public boolean notSeenTxt4 = true;
	public boolean txt4mod = false;
	private JLabel errorLabel;
	private JCheckBox chckbxOpenFileDirectory;
	private JTextField txtImportIgnoreableList;
	private JButton button;
	private JCheckBox chckbxIgnoreFileActive;
	private FileOptions myOptions;
	public static HashMap<String, Boolean> optionsMap = new HashMap<String, Boolean>();
	private JCheckBox chckbxTxt;
	private JCheckBox chckbxHTML;
	public boolean fileTypeClick = false;
	private JPanel checkPanel;
	private JSeparator separator;
	private JSeparator separator_1;
	private JLabel label;
	private JCheckBox chckbxXml;

	/**
	 * Create the panel that holds all components
	 */
	public ControlsPanel() {
		setForeground(new Color(0, 0, 0));
		setBackground(new Color(255, 255, 255));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 84, 311, 0 };
		gridBagLayout.rowHeights = new int[] { 23, 0, 0, 0, 0, 0, 31, 0, 0, 0, 0, 0,
				0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		setFont(new Font("Arial", Font.PLAIN, 12));

		// sets up the title label
		lblNewLabel = new JLabel("Softchalk Zip Replacer Application");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		// sets up the first text box for File Location
		txtFindFile = new JTextField();
		txtFindFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (notSeenTxt1) {
					txtFindFile.setText("");
					notSeenTxt1 = false;
					txt1Mod = true;
				}
			}
		});
		
				// sets up button to open up file chooser
				JButton btnBrowse = new JButton("Browse");
				btnBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btnBrowse.setBackground(Color.LIGHT_GRAY);
				btnBrowse.setForeground(Color.BLACK);
				btnBrowse.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						fileLocation = txtFindFile.getText();
						JFileChooser chooser = new JFileChooser(fileLocation);
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
								"ZIP Files", "zip");
						chooser.setFileFilter(filter);
						int returnVal = chooser.showOpenDialog(getParent());
						if (returnVal == JFileChooser.APPROVE_OPTION)
							fileLocation = chooser.getSelectedFile().toString();
						txtFindFile.setText(fileLocation);
						txt1Mod = true;
					}
				});
				
						GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
						gbc_btnBrowse.fill = GridBagConstraints.VERTICAL;
						gbc_btnBrowse.insets = new Insets(0, 0, 5, 5);
						gbc_btnBrowse.gridx = 0;
						gbc_btnBrowse.gridy = 1;
						add(btnBrowse, gbc_btnBrowse);
		txtFindFile.setDropTarget(new DropTarget() {
			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					txtFindFile.setText(droppedFiles.get(0).toString());
					txt1Mod = true;
					notSeenTxt1 = false;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		txtFindFile.setText("ZIP Location");
		GridBagConstraints gbc_txtFindFile = new GridBagConstraints();
		gbc_txtFindFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFindFile.insets = new Insets(0, 0, 5, 0);
		gbc_txtFindFile.gridx = 1;
		gbc_txtFindFile.gridy = 1;
		add(txtFindFile, gbc_txtFindFile);
		txtFindFile.setColumns(15);

		// sets up second text box for wordToFind
		txtWordsToFind = new JTextField();
		txtWordsToFind.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (notSeenTxt2) {
					txtWordsToFind.setText("");
					notSeenTxt2 = false;
				}
			}
		});
		txtWordsToFind.setText("Words to Replace");
		GridBagConstraints gbc_txtWordsToFind = new GridBagConstraints();
		gbc_txtWordsToFind.insets = new Insets(0, 0, 5, 0);
		gbc_txtWordsToFind.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWordsToFind.gridx = 1;
		gbc_txtWordsToFind.gridy = 2;
		add(txtWordsToFind, gbc_txtWordsToFind);
		txtWordsToFind.setColumns(10);

		// Sets up third text field to for wordThatReplaces
		txtWordsThatReplace = new JTextField();
		txtWordsThatReplace.addActionListener(this);
		txtWordsThatReplace.setText("Words that replace");
		GridBagConstraints gbc_txtWordsThatReplace = new GridBagConstraints();
		gbc_txtWordsThatReplace.insets = new Insets(0, 0, 5, 0);
		gbc_txtWordsThatReplace.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWordsThatReplace.gridx = 1;
		gbc_txtWordsThatReplace.gridy = 3;
		txtWordsThatReplace.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (notSeenTxt3) {
					txtWordsThatReplace.setText("");
					notSeenTxt3 = false;
				}
			}
		});
		add(txtWordsThatReplace, gbc_txtWordsThatReplace);
		txtWordsThatReplace.setColumns(30);

		// sets up final button to find/replace string
		btnFindButton = new JButton("Find/Replace");
		btnFindButton.setActionCommand("ReplaceString");
		btnFindButton.setForeground(Color.BLACK);
		btnFindButton.setBackground(Color.LIGHT_GRAY);
		btnFindButton.addActionListener(this);
		button = new JButton("Browse");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileLocation = txtImportIgnoreableList.getText();
				JFileChooser chooser = new JFileChooser(fileLocation);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"*.txt Files", "txt");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION)
					fileLocation = chooser.getSelectedFile().toString();
				txtImportIgnoreableList.setText(fileLocation);
				txt4mod = true;
			}
		});
		button.setForeground(Color.BLACK);
		button.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 0;
		gbc_button.gridy = 4;
		add(button, gbc_button);

		txtImportIgnoreableList = new JTextField();

		txtImportIgnoreableList.setText("Choose Ignoreable list");
		GridBagConstraints gbc_txtImportIgnoreableList = new GridBagConstraints();
		gbc_txtImportIgnoreableList.insets = new Insets(0, 0, 5, 0);
		gbc_txtImportIgnoreableList.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtImportIgnoreableList.gridx = 1;
		gbc_txtImportIgnoreableList.gridy = 4;
		add(txtImportIgnoreableList, gbc_txtImportIgnoreableList);
		txtImportIgnoreableList.setColumns(10);
		txtImportIgnoreableList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (notSeenTxt4) {
					txtImportIgnoreableList.setText("");
					notSeenTxt4 = false;
					txt4mod = true;
				}
			}
		});
		txtImportIgnoreableList.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if (notSeenTxt4) {
					txtImportIgnoreableList.setText("");
					notSeenTxt4 = false;
					txt4mod = true;
				}
			}
		});
		txtImportIgnoreableList.setDropTarget(new DropTarget() {
			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					txtImportIgnoreableList.setText(droppedFiles.get(0)
							.toString());
					txt4mod = true;
					notSeenTxt4 = false;

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		separator = new JSeparator();
		separator.setBackground(Color.GRAY);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 1;
		gbc_separator.gridy = 5;
		add(separator, gbc_separator);
		
		label = new JLabel("File Types");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 6;
		add(label, gbc_label);

		checkPanel = new JPanel();
		checkPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_checkPanel = new GridBagConstraints();
		gbc_checkPanel.anchor = GridBagConstraints.WEST;
		gbc_checkPanel.insets = new Insets(0, 0, 5, 0);
		gbc_checkPanel.fill = GridBagConstraints.VERTICAL;
		gbc_checkPanel.gridx = 1;
		gbc_checkPanel.gridy = 6;
		add(checkPanel, gbc_checkPanel);
		GridBagLayout gbl_checkPanel = new GridBagLayout();
		gbl_checkPanel.columnWidths = new int[]{45, 0, 39, 0};
		gbl_checkPanel.rowHeights = new int[]{23, 0};
		gbl_checkPanel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_checkPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		checkPanel.setLayout(gbl_checkPanel);
				
						chckbxHTML = new JCheckBox("html");
						GridBagConstraints gbc_chckbxHTML = new GridBagConstraints();
						gbc_chckbxHTML.anchor = GridBagConstraints.NORTHWEST;
						gbc_chckbxHTML.insets = new Insets(0, 0, 0, 5);
						gbc_chckbxHTML.gridx = 0;
						gbc_chckbxHTML.gridy = 0;
						checkPanel.add(chckbxHTML, gbc_chckbxHTML);
						
								chckbxHTML.setBackground(Color.WHITE);
								chckbxHTML.setSelected(true);
				
				chckbxXml = new JCheckBox("xml");
				chckbxXml.setSelected(true);
				chckbxXml.setBackground(Color.WHITE);
				GridBagConstraints gbc_chckbxXml = new GridBagConstraints();
				gbc_chckbxXml.insets = new Insets(0, 0, 0, 5);
				gbc_chckbxXml.gridx = 1;
				gbc_chckbxXml.gridy = 0;
				checkPanel.add(chckbxXml, gbc_chckbxXml);
		
				chckbxTxt = new JCheckBox("txt");
				GridBagConstraints gbc_chckbxTxt = new GridBagConstraints();
				gbc_chckbxTxt.anchor = GridBagConstraints.NORTHWEST;
				gbc_chckbxTxt.gridx = 2;
				gbc_chckbxTxt.gridy = 0;
				checkPanel.add(chckbxTxt, gbc_chckbxTxt);
				chckbxTxt.setSelected(true);
				chckbxTxt.setBackground(Color.WHITE);
		
		separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 1;
		gbc_separator_1.gridy = 7;
		add(separator_1, gbc_separator_1);

		chckbxOpenFileDirectory = new JCheckBox(
				"Open File Directory after process");
		chckbxOpenFileDirectory.setForeground(Color.BLACK);
		chckbxOpenFileDirectory.setBackground(Color.WHITE);
		chckbxOpenFileDirectory.setSelected(true);
		GridBagConstraints gbc_chckbxOpenFileDirectory = new GridBagConstraints();
		gbc_chckbxOpenFileDirectory.anchor = GridBagConstraints.WEST;
		gbc_chckbxOpenFileDirectory.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxOpenFileDirectory.gridx = 1;
		gbc_chckbxOpenFileDirectory.gridy = 8;
		add(chckbxOpenFileDirectory, gbc_chckbxOpenFileDirectory);

		chckbxIgnoreFileActive = new JCheckBox("Import Ignore File Enabled");
		chckbxIgnoreFileActive.setSelected(true);
		chckbxIgnoreFileActive.setBackground(Color.WHITE);
		GridBagConstraints gbc_chckbxIgnoreFileActive = new GridBagConstraints();
		gbc_chckbxIgnoreFileActive.anchor = GridBagConstraints.WEST;
		gbc_chckbxIgnoreFileActive.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxIgnoreFileActive.gridx = 1;
		gbc_chckbxIgnoreFileActive.gridy = 9;
		add(chckbxIgnoreFileActive, gbc_chckbxIgnoreFileActive);
		chckbxIgnoreFileActive.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (chckbxIgnoreFileActive.isSelected()) {
					txtImportIgnoreableList.setVisible(true);
					button.setVisible(true);
				} else {
					txtImportIgnoreableList.setVisible(false);
					button.setVisible(false);
				}
			}

		});
		GridBagConstraints gbc_btnFindButton = new GridBagConstraints();
		gbc_btnFindButton.gridwidth = 2;
		gbc_btnFindButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnFindButton.gridx = 0;
		gbc_btnFindButton.gridy = 10;
		add(btnFindButton, gbc_btnFindButton);
		errorLabel = new JLabel("----------");
		GridBagConstraints gbc_errorLabel = new GridBagConstraints();
		gbc_errorLabel.gridwidth = 2;
		gbc_errorLabel.insets = new Insets(0, 0, 5, 0);
		gbc_errorLabel.gridx = 0;
		gbc_errorLabel.gridy = 11;
		add(errorLabel, gbc_errorLabel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == txtWordsThatReplace
				| e.getSource() == btnFindButton) {
			try {
				String errorString = "";
				if (!txt1Mod) {
					errorString = "No file selected, please select a file";
				} else if (notSeenTxt2)
					errorString = "No word set to find, please write the word into the second text box";
				else if (notSeenTxt3)
					errorString = "No word set to replace, please write the word into the third text box";
				else if (!txt4mod & chckbxIgnoreFileActive.isSelected())
					errorString = "Please deselect \"Import Ignore File Enabled\", if you are not using a list.";
				else {
					setMap();
					if (chckbxIgnoreFileActive.isSelected()) {
						new FindAndReplaceString(txtFindFile.getText(),
								txtWordsToFind.getText(),
								txtWordsThatReplace.getText(),
								txtImportIgnoreableList.getText());
					} else
						new FindAndReplaceString(txtFindFile.getText(),
								txtWordsToFind.getText(),
								txtWordsThatReplace.getText());
					if (chckbxOpenFileDirectory.isSelected())
						Desktop.getDesktop()
								.open(new File(txtFindFile.getText())
										.getParentFile());
					JOptionPane.showMessageDialog(this,
							"Completed Replacing String");

				}
				errorLabel.setText(errorString);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	public void setMap() {
		if (chckbxHTML.isSelected())
			optionsMap.put("html", true);
		else
			optionsMap.put("html", false);
		if (chckbxTxt.isSelected())
			optionsMap.put("txt", true);
		else
			optionsMap.put("txt", false);
		if (chckbxXml.isSelected())
			optionsMap.put("xml", true);
		else
			optionsMap.put("xml", false);
		
	}
}
