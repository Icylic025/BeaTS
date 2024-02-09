package model;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Song {
    private String artist;
    private String title;
    private String filePath;
    private Beats beats;
    private int bpm;
    private boolean isPlaying = false;

    public Song(String artist, String title, String filePath) {
        this.artist = artist;
        this.title = title;
        this.filePath = filePath;
        this.beats = new Beats(filePath);
        this.bpm = beats.getBpm();
    }

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



}
