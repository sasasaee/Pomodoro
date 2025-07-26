package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Pomodorocontroller {

	@FXML
	private Label sessionLabel;
	@FXML
	private Label timerLabel;
	@FXML
	private ProgressBar progressBar;
	@FXML
	private Button startBtn;
	@FXML
	private Button pauseBtn;
	@FXML
	private Button resetBtn;
	@FXML
	private TextField workInput;
	@FXML
	private TextField breakInput;
	@FXML
	private TextField longBreakInput;


	private Timeline timeline;
	private int workDuration = 25 * 60;
	private int shortBreak = 5 * 60;
	private int longBreak = 20 * 60;
	private int timeRemaining;
	private int sessionCount = 0;
	private boolean isWorkmode = true;

	public void initialize() {
		timeRemaining = workDuration;
		timerLabel.setText(formatTime(timeRemaining));
		sessionLabel.setText("Work");
	}

	@FXML
	private void startTimer() {
		if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING)
			return;

		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			timeRemaining--;
			updateTimer();
			if (timeRemaining <= 0) {
				playSound();
				switchSession();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	@FXML
	private void pauseTimer() {
		if (timeline != null) {
			timeline.pause();
		}
	}

	@FXML
	private void resetTimer() {
		if (timeline != null) {
			timeline.stop();
		}
		timeRemaining = isWorkmode ? workDuration : (sessionCount % 4 == 0 ? longBreak : shortBreak);
		updateTimer();
	}

	
	@FXML private void applySettings() {
			try {
			int w = Integer.parseInt(workInput.getText());
			int b = Integer.parseInt(breakInput.getText());
			int l = Integer.parseInt(longBreakInput.getText());
			workDuration = w * 60; shortBreak = b * 60; longBreak = l * 60;
			resetTimer();
			} catch (NumberFormatException e) {
				System.out.println("Invalid input");
				}
		}
		
	private void switchSession() {
		if (isWorkmode)
			sessionCount++;

		isWorkmode = !isWorkmode;

		if (!isWorkmode && sessionCount % 4 == 0) {
			timeRemaining = longBreak;
			sessionLabel.setText("Long Break");
		} else if (isWorkmode) {
			timeRemaining = workDuration;
			sessionLabel.setText("Work");
		} else {
			timeRemaining = shortBreak;
			sessionLabel.setText("Short Break");
		}

		updateTimer();
	}

	private void updateTimer() {
		timerLabel.setText(formatTime(timeRemaining));
		int fullDuration = isWorkmode ? workDuration : (sessionCount % 4 == 0 ? longBreak : shortBreak);
		progressBar.setProgress((double) (fullDuration - timeRemaining) / fullDuration);
	}

	private String formatTime(int seconds) {
		int minutes = seconds / 60;
		int secs = seconds % 60;
		return String.format("%02d:%02d", minutes, secs);
	}
	private void playSound() {
        try {
            Media sound = new Media(getClass().getResource("alarm-2-375697.mp3").toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Sound file not found or can't play.");
        }
    }

}
