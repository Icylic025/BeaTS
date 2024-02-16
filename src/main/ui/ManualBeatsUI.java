package ui;

import threads.Playlist;
import model.ManualBpmCalc;
import model.LocalMusicManager;
import model.MasterMusicManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The ManualBeatsUI class facilitates manual beat detection for songs.
 * It allows users to tap the Enter key to determine the BPM (beats per minute) of a song.
 * Upon initialization, it prompts the user to tap the Enter key 10 times and records the timing of each tap.
 * Using this data, it calculates the BPM and creates a new PlaylistUI displaying the songs around the
 * calculated BPM.
 */


public class ManualBeatsUI {
    private ManualBpmCalc beatCalc;
    private int manualBpm;
    Playlist playlist;
    LocalMusicManager localMusicManager;
    MasterMusicManager masterMusicManager;


    /**
     * Requires: masterMusicManager to be initialized with a valid instance of MasterMusicManager
     * Effects: Constructs a ManualBeatsUI object, initializes manual beat detection process,
     *          and calculates BPM based on user-entered beats.
     */
    public ManualBeatsUI(MasterMusicManager masterMusicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        playlist = masterMusicManager.getMasterPlaylist();
        this.masterMusicManager = masterMusicManager;
        manualBpmDetection();
    }

    /**
     * Effects: Performs manual beat detection by prompting the user to tap Enter key 10 times,
     *          records the timing of each tap, and calculates the BPM.
     */
    public void manualBpmDetection()
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int bpm = 0;
        List<Double> manualBeats = new ArrayList<Double>();
        Scanner scanner = new Scanner(System.in);
        int tapCount = 0;
        final int MAX_TAPS = 10;
        LocalMusicManager filteredManager;

        System.out.println("As consistently as possible, please tap the Enter Button 10 times according to a Beat: ");
        System.out.println("Press Enter to Start... ");
        scanner.nextLine();

        while (tapCount < MAX_TAPS) { // Use < instead of <= to ensure loop runs exactly MAX_TAPS times

            if (scanner.nextLine().equals("")) {
                tapCount++; // Increment tapCount as soon as Enter is detected
                System.out.println("Taps Detected: " + tapCount + "/10");

                double currentTime = System.currentTimeMillis();
                manualBeats.add(currentTime);
            } else {
                System.out.println("Oops, looks like you pressed the wrong button, lets start again.");
                manualBpmDetection();
                return;
            }
        }

        beatCalc = new ManualBpmCalc(manualBeats);

        displaySongs(beatCalc);
    }

    /**
     * Requires: beatCalc to be initialized with a valid instance of ManualBpmCalc
     * Effects: Displays songs around the calculated BPM based on manual beat detection results.
     */
    public void displaySongs(ManualBpmCalc beatCalc)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        LocalMusicManager filteredManager = new LocalMusicManager(playlist.filterByBpm(this.beatCalc.getBpm()));

        System.out.println("Here are the songs around this BPM (" + this.beatCalc.getBpm() + "): ");

        if (filteredManager.getPlaylist().getSize() == 0) {
            System.out.println("Sorry, you don't have any songs around that BPM.");
            return;
        }
        new PlaylistUI(filteredManager, masterMusicManager);

    }
}

