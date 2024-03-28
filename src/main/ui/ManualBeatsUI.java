package ui;

import model.LocalMusicManager;
import model.ManualBpmCalc;
import model.MasterMusicManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * The ManualBeatsUI class represents a user interface panel for manually detecting the beats per minute (BPM) of music.
 * It allows users to tap along with the beat of the music and records the timing of each tap. Once the user taps the
 * button a specified number of times, the class calculates the BPM based on the time intervals between taps. It then
 * displays songs from the master playlist with a BPM close to the calculated BPM in a new PlaylistGUI window. This class
 * enhances user interaction by providing a simple and intuitive interface for selecting songs based on their BPM,
 * facilitating music management within the application. Additionally, it leverages the ManualBpmCalc class to accurately
 * calculate BPM from recorded tap times, ensuring precise song selection based on user input.
 */
public class ManualBeatsUI extends JPanel {
    private MasterMusicManager masterMusicManager;
    private JLabel infoLabel;
    private JButton tapButton;
    private List<Double> tapTimes;
    private static final int MAX_TAPS = 10;
    int bpm;

    /**
     * Modifies: this (ManualBeatsUI object)
     * Effects: Constructs a ManualBeatsUI panel with a reference to the MasterMusicManager,
     *          initializes the UI components and an empty list for tap times.
     */
    public ManualBeatsUI(MasterMusicManager masterMusicManager) {
        this.masterMusicManager = masterMusicManager;
        this.tapTimes = new ArrayList<>();
        initializeUI();
    }

    /**
     * Modifies: this (UI components and layout)
     * Effects: Initializes the user interface components, including information label and
     *          tap button, and sets the layout.
     */
    private void initializeUI() {
        setLayout(new BorderLayout());

        infoLabel = new JLabel("Tap the button 10 times to the beat of the music.");
        add(infoLabel, BorderLayout.NORTH);

        tapButton = new JButton("Tap");
        tapButton.addActionListener(this::handleTap);
        add(tapButton, BorderLayout.CENTER);
    }

    /**
     * Modifies: tapTimes (list of Double), infoLabel (JLabel), tapButton (JButton)
     * Effects: Handles tap actions by recording tap times, updating the label with the count of taps,
     *          and disables the tap button after reaching the maximum tap count.
     */
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

    /**
     * Modifies: bpm (integer), UI components indirectly by calling other methods
     * Effects: Processes the recorded taps to calculate BPM, and displays songs around that BPM in a new PlaylistGUI.
     */
    private void processTapsAndDisplaySongs() {
        System.out.println("pro");
        bpm = calculateBpm();
        displaySongsAroundBPM(bpm);
    }


    /**
     * Modifies: UI components indirectly by creating a PlaylistGUI
     * Effects: Displays songs within a certain BPM range in a new PlaylistGUI window, and disposes
     *          of the current window.
     */
    private void displaySongsAroundBPM(int bpm) {
        LocalMusicManager local = new LocalMusicManager(masterMusicManager.getMasterPlaylist());
        PlaylistGUI playlistGUI = new PlaylistGUI(local.filter(bpm), masterMusicManager);
        playlistGUI.setVisible(true); // Show the playlist GUI

        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    /**
     * Effects: Calculates and returns the BPM based on the recorded tap times.
     *          Shows a message if not enough taps are recorded.
     */
    private int calculateBpm() {
        if (tapTimes.size() < 2) {
            // Display a message to the user indicating that at least two taps are required
            return 0;
        }

        ManualBpmCalc bpmCalculator = new ManualBpmCalc(tapTimes);
        return bpmCalculator.getBpm();
    }


}
