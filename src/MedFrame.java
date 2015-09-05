import java.awt.EventQueue;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
		frame.setBounds(200, 200, 1200, 500);
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
		JLabel lblTimesPerDay;
		JLabel lblStartTime;
		JLabel lblRepeat;
		JLabel lblMeds;

		for (int i = 1; i <= intNumOfMeds; i++) {
			lblMeds = new JLabel("Medication " + i);
			lblMeds.setBounds(6, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
			frame.getContentPane().add(lblMeds);

			medName = new JTextField();
			setTextFields(medName, 100, 89 + (i * 30), 100, 20);

			lblTimesPerDay = new JLabel("Times per day");
			lblTimesPerDay.setBounds(220, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
			frame.getContentPane().add(lblTimesPerDay);

			JTextField timesPerDayTxt = new JTextField();
			setTextFields(timesPerDayTxt, 320, 89 + (i * 30), 50, 20);

			lblStartTime = new JLabel("Start time");
			lblStartTime.setBounds(390, 89 + (i * 30), 217, 20); // spaces out labels by 30 pixels
			frame.getContentPane().add(lblStartTime);

			String[] times = startTimes();
			JComboBox<String> timeList = new JComboBox<String>(times);
			timeList.setBounds(460, 89 + (i * 30), 200, 20);
			frame.getContentPane().add(timeList);

			lblRepeat = new JLabel("Repeat?");
			lblRepeat.setBounds(675, 89 + (i * 30), 240, 20); // spaces out labels by 30 pixels
			frame.getContentPane().add(lblRepeat);

			String[] freq = { "never", "hourly", "every two hours", "every three hours", "every four hours", "every five hours", 
					"every six hours", "every seven hours", "every eight hours", "every nine hours",
					"every ten hours", "every eleven hours", "every twelve hours"};
			JComboBox<String> freqList = new JComboBox<String>(freq);
			freqList.setBounds(740, 89 + (i * 30), 200, 20);
			frame.getContentPane().add(freqList);
		}
	}

	private void setTextFields(JTextField textField, int xCord, int yCord, int width, int height) {
		textField.setBounds(xCord, yCord, width, height);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
	}

	private String[] startTimes() {
		String[] times = new String[48];
		times[0] = "12:00AM";
		times[1] = "12:30AM";
		times[24] = "12:00PM";
		times[25] = "12:30PM";

		for (int i = 1, time = 1; time < 12; i+= 2, time++) {
			times[i + 1] = time + ":00AM";
			times[i + 2] = time + ":30AM";
		}
		for (int i = 1, time = 1; time < 12; i+= 2, time++) {
			times[i + 25] = time + ":00PM";
			times[i + 26] = time + ":30PM";
		}
		return times;
	}
}