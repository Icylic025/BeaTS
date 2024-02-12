package ui;

import model.ManualBpmCalc;
import model.MusicManager;
import model.Playlist;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualBeatsUI {
    private ManualBpmCalc beatCalc;
    private int manualBpm;
    Playlist playlist;
    MusicManager musicManager;


    public ManualBeatsUI(MusicManager musicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.musicManager = musicManager;
        playlist = musicManager.getMasterPlaylist();
        manualBpmDetection();
    }

    public void manualBpmDetection()
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int bpm = 0;
        List<Double> manualBeats = new ArrayList<Double>();
        Scanner scanner = new Scanner(System.in);
        int tapCount = 0;
        final int MAX_TAPS = 10;
        MusicManager filteredManager;

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

    public void displaySongs(ManualBpmCalc beatCalc)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        MusicManager filteredManager = new MusicManager(playlist.filterByBpm(this.beatCalc.getBpm()));

        System.out.println("Here are the songs around this BPM (" + this.beatCalc.getBpm() + "): ");
        new PlaylistUI(filteredManager);
    }
}

