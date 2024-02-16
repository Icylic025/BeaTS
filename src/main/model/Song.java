package model;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Song class represents a song.
 * It stores the artist, title, file path, and BPM (retrieved from Beats Class) of the song.
 * It also facilitates the play back of the stored song and works together with the
 * MusicPlayer class to make sure the code is paused until the song finishes playing
 */

public class Song {
    private String artist;
    private String title;
    private String filePath;
    private Beats beats;
    private int bpm;
    private boolean isPlaying = false;

    /**
     * Requires: Valid artist, title, and file path strings
     * Modifies: this
     * Effects: Constructs a Song object with the provided artist, title, and file path.
     *          Initializes the Beats object for the song to calculate BPM.
     */


    public Song(String artist, String title, String filePath) {
        this.artist = artist;
        this.title = title;
        this.filePath = filePath;
        this.beats = new Beats(filePath);
        this.bpm = beats.getBpm();
    }



    /**
     * Modifies: this, isPlaying
     * Effects: Plays the song using a MusicPlayer instance and waits until playback finishes.
     */
    public synchronized void playSong() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        MusicPlayer player = new MusicPlayer(this);
        player.play();

        // Wait until playback finishes
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Modifies: isPlaying
     * Effects: Notifies that the playback of the song has finished.
     */
    public synchronized void notifyPlaybackFinished() {
        isPlaying = false;
        notify(); // Notify the waiting thread (playSong method) that playback is finished
    }

    public int getBpm() {
        return bpm;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }


}



