package model;

import model.MusicPlayer;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MusicPlayerTest {

    private Song song;

    @BeforeEach
    void setUp() {
        // Create a dummy Song object for testing
        song = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");

    }

    @Test
    void testMusicPlayerInitialization() {
        // Test MusicPlayer initialization
        try {
            MusicPlayer musicPlayer = new MusicPlayer(song);
            assertNotNull(musicPlayer);
        } catch (UnsupportedAudioFileException | IOException | javax.sound.sampled.LineUnavailableException e) {
            fail("Exception thrown during MusicPlayer initialization: " + e.getMessage());
        }
    }

    @Test
    void testPlay() {
        // Test play method
        try {
            MusicPlayer musicPlayer = new MusicPlayer(song);
            musicPlayer.play();
            // Since play method waits for the duration of the clip, if no exceptions were thrown, it's considered successful
            assertTrue(true);
        } catch (UnsupportedAudioFileException | IOException | javax.sound.sampled.LineUnavailableException e) {
            fail("Exception thrown during MusicPlayer initialization: " + e.getMessage());
        }
    }
}
