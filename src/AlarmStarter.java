import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashSet;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class AlarmStarter implements Runnable {
	private int hour;
	private int min;
	private HashSet<String> medNames;

	public AlarmStarter(int hour, int min, HashSet<String> medNames) {
		this.hour = hour;
		this.min = min;
		this.medNames = medNames;
	}

	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1; // months start at 0th index
		int day = cal.get(Calendar.DAY_OF_MONTH);
		DateTime limit = new DateTime(year, month, day, this.hour, this.min, 0, 0);
		Boolean isLate = false;

		System.out.println("alarm set...");
		while(!isLate) {
			DateTime now = DateTime.now();
			isLate = now.isAfter(limit);
		}
		playAlarm();
	}

	@SuppressWarnings("restriction")
	private void playAlarm() {
		System.out.println("alarm going off...");
		// open the sound file as a Java input stream
		try {
			String audioFile = "/Users/ydylan/Documents/wav_files/alarm.au";
			InputStream in = new FileInputStream(audioFile);

			// create an audiostream from the inputstream		 
			AudioStream audioStream = new AudioStream(in);

			// play the audio clip with the audioplayer class
			AudioPlayer.player.start(audioStream);

			for (String medName : this.medNames) {
				JOptionPane.showMessageDialog(null, "Time to take your " + medName + "!");
			}

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Internal audio error (audio file not found).");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}