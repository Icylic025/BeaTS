package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    Song song;
    private MusicPlayer player;

    @BeforeEach
    void setUp() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
      song = new Song("BTS", "Whistle", "./data/Music/Whistle.wav");
      player = new MusicPlayer(song);
    }


    @Test
    public void testPlaySongSuccessfully() {
        try {
            song.playSong();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
            // Fail the test if an exception occurs
            fail("Exception occurred during playback.");
        }
        assertFalse(song.getIsPlaying());

    }

    @Test
    void testPlaySong() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        song.playSong();
    }

    @Test
    public void testPlaySongInterrupted() {
        // fake interruption
        Thread.currentThread().interrupt();

        try {
            song.playSong();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            // Expected exception due to interruption
        }
    }

    @Test
    public void testNotifyPlaybackFinished() {
        song.notifyPlaybackFinished();
    }




}
