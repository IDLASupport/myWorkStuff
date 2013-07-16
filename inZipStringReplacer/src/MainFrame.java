package stringReplacer;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
/**
 * 
 * @author Nicholas McNew
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final String VERSION_NAME = "0.5_01";
	private JPanel contentPane;
	private ControlsPanel myPanel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame for which the ControlsPanel is placed
	 */
	public MainFrame() {
		setResizable(false);
		setFont(new Font("Arial", Font.PLAIN, 12));
		setTitle("Softchalk Zip Replacer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 583,359);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		myPanel = new ControlsPanel();
		myPanel.setBorder(new EmptyBorder(0, 5, 0, 5));
		setContentPane(myPanel);
		
		JLabel lblVersion = new JLabel("Version "+ VERSION_NAME);
		lblVersion.setForeground(Color.GRAY);
		GridBagConstraints gbc_lblVersion = new GridBagConstraints();
		gbc_lblVersion.gridx = 0;
		gbc_lblVersion.gridy = 12;
		myPanel.add(lblVersion, gbc_lblVersion);
	}

}
