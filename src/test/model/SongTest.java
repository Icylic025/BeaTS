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
      song = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
      player = new MusicPlayer(song);
    }


    @Test
    public void testPlaySongSuccessfully() {
        // Call the playSong() method
        try {
            song.playSong();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
            // Fail the test if an exception occurs during playback
            assert false : "Exception occurred during playback.";
        }


    }

    @Test
    public void testPlaySongInterrupted() {
        // Simulate interruption during playback (e.g., by interrupting the thread)
        Thread.currentThread().interrupt();

        // Call the playSong() method
        try {
            song.playSong();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            // Expected exception due to interruption
        }
    }

    @Test
    public void testNotifyPlaybackFinished() {
        // Set up the initial state (e.g., isPlaying = true)
        song.setPlaying(true);

        // Call the notifyPlaybackFinished() method
        song.notifyPlaybackFinished();


        assertFalse(song.getIsPlaying()); // Check if isPlaying is set to false
    }

    @Test public void testGetSetMethods() {
        assertEquals("BTS", song.getArtist());

        assertFalse(song.getIsPlaying());
        song.setPlaying(true);
        assertTrue(song.getIsPlaying());
    }
}
