import java.awt.EventQueue;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import sun.audio.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MedFrame {

	private JFrame frame;
	private JComboBox<String> timeList;
	private final TreeMap<String, ArrayList<String>> medMap;
	private JTextField medName;
	private int medNum;

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
		medMap = new TreeMap<String, ArrayList<String>>();
		medNum = 1;
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

		JLabel lblNumOfMeds = new JLabel("Enter your medications.");
		lblNumOfMeds.setBounds(6, 69, 217, 50);
		frame.getContentPane().add(lblNumOfMeds);

		// resets frame
		JButton resetBtn = new JButton("Reset");
		resetBtn.setBounds(183, 81, 117, 29);
		frame.getContentPane().add(resetBtn);

		/**
		 * starts alarm, converts local time to military time
		 */
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medTreeInsertion();
				for (String time : medMap.keySet()) {
					System.out.println(time);
					int hour = 0;
					int min = 0;

					if (time.equals("12:00AM")) {
						hour = 0;
						min = 0;
					} else if (time.equals("12:30AM")) {
						min = 30;
					} else if (time.contains("AM") || time.substring(0, 2).equals("12")) { // can only be 12pm
						if (time.length() == 7) {
							hour = Integer.parseInt(time.substring(0, 2));	
							min = Integer.parseInt(time.substring(3, 5));
						} else {
							hour = Integer.parseInt(time.substring(0, 1));	
							min = Integer.parseInt(time.substring(2, 4));
						}
					} else { // after 12:59PM...
						if (time.length() == 7) { // 10pm, 11pm, etc...
							hour = Integer.parseInt(time.substring(0, 2)) + 12;
						} else { // 1pm, 2pm, etc...
							hour = Integer.parseInt(time.substring(0, 1)) + 12;
							min = Integer.parseInt(time.substring(2, 4));
						}
					}
					compareTimes(hour, min, medMap.get(time));
				}
				// for testing
				for (String key : medMap.keySet()) {
					System.out.println("key: " + key + " val: " + medMap.get(key));
				}
			}
		});
		startBtn.setBounds(299, 81, 117, 29);
		frame.getContentPane().add(startBtn);

		// adds more meds
		promtMeds(); // initial med prompt
		JButton btnAddMed = new JButton("+");
		btnAddMed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				medTreeInsertion();
				promtMeds();
				frame.revalidate(); // tells the layout manager to reset based on the new component list. This will also trigger a call to repaint.	
				frame.repaint();	// used to tell a component to repaint itself.
				btnAddMed.setBounds(29, 100 + (medNum * 30), 51, 29);
				frame.getContentPane().add(btnAddMed);
			}
		});
		btnAddMed.setBounds(29, 155, 51, 29); // initial + button
		frame.getContentPane().add(btnAddMed);
	}

	private void promtMeds() {
		JLabel lblTimesPerDay;
		JLabel lblStartTime;
		JLabel lblRepeat;
		JLabel lblMeds;

		lblMeds = new JLabel("Medication " + medNum);
		lblMeds.setBounds(6, 100 + (medNum * 30), 217, 20); // spaces out labels by 30 pixels
		frame.getContentPane().add(lblMeds);

		medName = new JTextField();
		setTextFields(medName, 100, 100 + (medNum * 30), 100, 20);

		lblTimesPerDay = new JLabel("Times per day");
		lblTimesPerDay.setBounds(220, 100 + (medNum * 30), 217, 20); // spaces out labels by 30 pixels
		frame.getContentPane().add(lblTimesPerDay);

		JTextField timesPerDayTxt = new JTextField();
		setTextFields(timesPerDayTxt, 320, 100 + (medNum * 30), 50, 20);

		lblStartTime = new JLabel("Start time");
		lblStartTime.setBounds(390, 100 + (medNum * 30), 217, 20); // spaces out labels by 30 pixels
		frame.getContentPane().add(lblStartTime);

		String[] times = startTimes();
		timeList = new JComboBox<String>(times); // add to some data structure
		timeList.setBounds(460, 100 + (medNum * 30), 200, 20);
		frame.getContentPane().add(timeList);

		lblRepeat = new JLabel("Repeat?");
		lblRepeat.setBounds(675, 100 + (medNum * 30), 240, 20); // spaces out labels by 30 pixels
		frame.getContentPane().add(lblRepeat);

		String[] freq = { "never", "hourly", "every two hours", "every three hours", "every four hours", "every five hours", 
				"every six hours", "every seven hours", "every eight hours", "every nine hours",
				"every ten hours", "every eleven hours", "every twelve hours"};
		JComboBox<String> freqList = new JComboBox<String>(freq);
		freqList.setBounds(740, 100 + (medNum * 30), 200, 20);
		frame.getContentPane().add(freqList);
		
		medNum++;
	}
	
	private void medTreeInsertion() {
		String selectedTime = timeList.getSelectedItem().toString();
		if (medMap.containsKey(selectedTime)) {
			medMap.get(selectedTime).add(medName.getText());
		} else {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(medName.getText());
			medMap.put(selectedTime, temp);
		}
	}

	/**
	 * Checks if given time has occurred.
	 * @param hour
	 * @param min
	 */
	private void compareTimes(int hour, int min, ArrayList<String> medNames) {
		AlarmStarter alarmStarter = new AlarmStarter(hour, min, medNames);
		Thread alarmThread = new Thread(alarmStarter);
		alarmThread.start();
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