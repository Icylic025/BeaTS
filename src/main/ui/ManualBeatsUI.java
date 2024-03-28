package ui;

import model.LocalMusicManager;
import model.ManualBpmCalc;
import model.MasterMusicManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ManualBeatsUI extends JPanel {
    private MasterMusicManager masterMusicManager;
    private JLabel infoLabel;
    private JButton tapButton;
    private List<Double> tapTimes;
    private static final int MAX_TAPS = 10;
    int bpm;

    public ManualBeatsUI(MasterMusicManager masterMusicManager) {
        this.masterMusicManager = masterMusicManager;
        this.tapTimes = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        infoLabel = new JLabel("Tap the button 10 times to the beat of the music.");
        add(infoLabel, BorderLayout.NORTH);

        tapButton = new JButton("Tap");
        tapButton.addActionListener(this::handleTap);
        add(tapButton, BorderLayout.CENTER);
    }

    private void handleTap(ActionEvent e) {
        if (tapTimes.size() <= MAX_TAPS) {
            tapTimes.add((double) System.currentTimeMillis());
            infoLabel.setText("Taps: " + tapTimes.size() + "/" + MAX_TAPS);
            if (tapTimes.size() == MAX_TAPS) {

                tapButton.setEnabled(false); // Prevent more taps

                processTapsAndDisplaySongs();
            }
        }
    }

    private void processTapsAndDisplaySongs() {
        System.out.println("pro");
        bpm = calculateBpm();
        displaySongsAroundBPM(bpm);
    }

    private void displaySongsAroundBPM(int bpm) {
        LocalMusicManager local = new LocalMusicManager(masterMusicManager.getMasterPlaylist());
        PlaylistGUI playlistGUI = new PlaylistGUI(local.filter(bpm), masterMusicManager);
        playlistGUI.setVisible(true); // Show the playlist GUI

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    private int calculateBpm() {
        if (tapTimes.size() < 2) {
            // Display a message to the user indicating that at least two taps are required
            return 0;
        }

        ManualBpmCalc bpmCalculator = new ManualBpmCalc(tapTimes);
        return bpmCalculator.getBpm();
    }


}
