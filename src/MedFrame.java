import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JFormattedTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;


public class MedFrame {

	private JFrame frame;
	private JTextField numOfMeds;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MedFrame window = new MedFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MedFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(200, 200, 1000, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel frameLabel = new JLabel("Medicine Reminder");
		frameLabel.setBounds(425, 18, 173, 51);
		frame.getContentPane().add(frameLabel);

		JLabel lblNumOfMeds = new JLabel("How many meds are you taking?");
		lblNumOfMeds.setBounds(6, 69, 217, 50);
		frame.getContentPane().add(lblNumOfMeds);

		numOfMeds = new JTextField();
		numOfMeds.setBounds(222, 85, 41, 20);
		frame.getContentPane().add(numOfMeds);
		numOfMeds.setColumns(10);

		JButton numOfMedsBttn = new JButton("Enter");
		numOfMedsBttn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int intNumOfMeds = 0;
				try {
					intNumOfMeds = Integer.parseInt(numOfMeds.getText());
					promtMeds(intNumOfMeds);
					
					frame.revalidate(); // tells the layout manager to reset based on the new component list. This will also trigger a call to repaint.	
					frame.repaint();	// used to tell a component to repaint itself.
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Please enter number only");
				}	
			}
		});
		numOfMedsBttn.setBounds(268, 81, 117, 29);
		frame.getContentPane().add(numOfMedsBttn);

	}
	
	private void promtMeds(int intNumOfMeds) {
		JTextField medName;
		JLabel timesPerDay;
		JLabel startTime;
		JLabel repeat;
		JLabel meds;
		
		if (intNumOfMeds > 0) {
			for (int i = 1; i <= intNumOfMeds; i++) {
				meds = new JLabel("Medication " + i);
				meds.setBounds(6, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
				frame.getContentPane().add(meds);
				
				medName = new JTextField();
				setTextFields(medName, 100, 89 + (i * 30), 100, 20);
				
				timesPerDay = new JLabel("Times per day");
				timesPerDay.setBounds(220, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
				frame.getContentPane().add(timesPerDay);
				
				JTextField timesPerDayTxt = new JTextField();
				setTextFields(timesPerDayTxt, 320, 89 + (i * 30), 50, 20);
				
				startTime = new JLabel("Start time");
				startTime.setBounds(400, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
				frame.getContentPane().add(startTime);
				
				JTextField startTimeTxt = new JTextField();
				setTextFields(startTimeTxt, 480, 89 + (i * 30), 50, 20);
				
				repeat = new JLabel("Repeat?");
				repeat.setBounds(550, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
				frame.getContentPane().add(repeat);
				
				JCheckBox repeatBox = new JCheckBox("repeatBox");
				repeatBox.setBounds(600, 89 + (i * 30), 25, 23);
				frame.getContentPane().add(repeatBox);
			}
		}
	}
	
	private void setTextFields(JTextField textField, int xCord, int yCord, int width, int height) {
		textField.setBounds(xCord, yCord, width, height);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
	}
}