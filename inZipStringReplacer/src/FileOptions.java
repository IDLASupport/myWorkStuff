package stringReplacer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class FileOptions extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private HashMap<String, Boolean> myMap = new HashMap<String, Boolean>(3);
	private JCheckBox chckbxtxt;
	private JCheckBox chckbxhtml;
	private JCheckBox chckbxXml;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public FileOptions() {
		getContentPane().setBackground(Color.WHITE);
		setBounds(100, 100, 132, 191);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		{
			chckbxtxt = new JCheckBox("txt");
			chckbxtxt.setBackground(Color.WHITE);
			chckbxtxt.setFont(new Font("Century Gothic", Font.PLAIN, 15));
			GridBagConstraints gbc_chckbxtxt = new GridBagConstraints();
			gbc_chckbxtxt.anchor = GridBagConstraints.WEST;
			gbc_chckbxtxt.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxtxt.gridx = 0;
			gbc_chckbxtxt.gridy = 0;
			contentPanel.add(chckbxtxt, gbc_chckbxtxt);
		}
		{
			JCheckBox chckbxhtml = new JCheckBox("html");
			chckbxhtml.setBackground(Color.WHITE);
			chckbxhtml.setFont(new Font("Century Gothic", Font.PLAIN, 15));
			GridBagConstraints gbc_chckbxhtml = new GridBagConstraints();
			gbc_chckbxhtml.anchor = GridBagConstraints.WEST;
			gbc_chckbxhtml.insets = new Insets(0, 0, 5, 0);
			gbc_chckbxhtml.gridx = 0;
			gbc_chckbxhtml.gridy = 1;
			contentPanel.add(chckbxhtml, gbc_chckbxhtml);
		}
		{
			JCheckBox chckbxXml = new JCheckBox("xml");
			chckbxXml.setBackground(Color.WHITE);
			chckbxXml.setFont(new Font("Century Gothic", Font.PLAIN, 15));
			GridBagConstraints gbc_chckbxXml = new GridBagConstraints();
			gbc_chckbxXml.anchor = GridBagConstraints.WEST;
			gbc_chckbxXml.gridx = 0;
			gbc_chckbxXml.gridy = 2;
			contentPanel.add(chckbxXml, gbc_chckbxXml);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setBackground(Color.LIGHT_GRAY);
				okButton.setForeground(Color.BLACK);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
						if(chckbxtxt.isSelected())
							myMap.put(".txt", true);
						/*if(chckbxhtml.isSelected())
							myMap.put(".html", true);
						if(chckbxXml.isSelected())
							myMap.put(".xml", true);*/	
					}					
				});
			}
		}
	}
	public HashMap<String, Boolean> getMap(){
		return myMap;
	}
}
