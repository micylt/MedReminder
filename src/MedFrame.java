import java.awt.EventQueue;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import org.joda.time.DateTime;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import sun.audio.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MedFrame {

	private JFrame frame;
	private JTextField numOfMeds;
	private JComboBox<String> timeList;

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

		JButton numOfMedsBtn = new JButton("Enter");
		numOfMedsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				System.out.println( sdf.format(cal.getTime()) );

				playAlarm();
				int intNumOfMeds = 0;
				try {
					intNumOfMeds = Integer.parseInt(numOfMeds.getText());
					promtMeds(intNumOfMeds);

					frame.revalidate(); // tells the layout manager to reset based on the new component list. This will also trigger a call to repaint.	
					frame.repaint();	// used to tell a component to repaint itself.
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Please enter numbers only");
				}	
			}
		});
		numOfMedsBtn.setBounds(268, 81, 117, 29);
		frame.getContentPane().add(numOfMedsBtn);

		// resets frame
		JButton resetBtn = new JButton("Reset");
		resetBtn.setBounds(383, 81, 117, 29);
		frame.getContentPane().add(resetBtn);

		/**
		 * starts alarm, converts local time to military time
		 */
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String value = timeList.getSelectedItem().toString();
				Calendar cal = Calendar.getInstance();
				
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				int day = cal.get(Calendar.DAY_OF_MONTH);
				int hour = 0;
				int min = 0;

				if (value.equals("12:00AM")) {
					hour = 0;
					min = 0;
				} else if (value.equals("12:30AM")) {
					min = 30;
				} else if (value.contains("AM") || value.substring(0, 2).equals("12")) { // can only be 12pm
					if (value.length() == 7) {
						hour = Integer.parseInt(value.substring(0, 2));	
						min = Integer.parseInt(value.substring(3, 5));
					} else {
						hour = Integer.parseInt(value.substring(0, 1));	
						min = Integer.parseInt(value.substring(2, 4));
					}
				} else { // after 12:59PM...
					if (value.length() == 7) { // 10pm, 11pm, etc...
						hour = Integer.parseInt(value.substring(0, 2)) + 12;
					} else { // 1pm, 2pm, etc...
						hour = Integer.parseInt(value.substring(0, 1)) + 12;
						min = Integer.parseInt(value.substring(2, 4));
					}
				}
				
				DateTime now = DateTime.now();
				DateTime limit = new DateTime(year, month, day, hour, min, 0, 0);
				System.out.println("limit = " + limit);
				Boolean isLate = now.isAfter(limit);
				
				if (isLate) {
					System.out.println("before current time...");
				} else {
					System.out.println("after current time...");
				}

				System.out.println("now = " + now);
//
//				System.out.println(value);
			}
		});
		startBtn.setBounds(499, 81, 117, 29);
		frame.getContentPane().add(startBtn);
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
			timeList = new JComboBox<String>(times);
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

	@SuppressWarnings("restriction")
	private void playAlarm() {
		// open the sound file as a Java input stream
		try {
			String audioFile = "/Users/ydylan/Documents/wav_files/alarm.au";
			InputStream in = new FileInputStream(audioFile);

			// create an audiostream from the inputstream		 
			AudioStream audioStream = new AudioStream(in);

			// play the audio clip with the audioplayer class
			AudioPlayer.player.start(audioStream);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}