package model;

import model.MusicPlayer;
import model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MusicPlayerTest {

    private Song song;

    @BeforeEach
    void setUp() {

        song = new Song("BTS", "Whistle", "./data/Music/Whistle.wav");

    }

    @Test
    void testMusicPlayerInitialization() {
        try {
            MusicPlayer musicPlayer = new MusicPlayer(song);
            assertNotNull(musicPlayer);
        } catch (UnsupportedAudioFileException | IOException | javax.sound.sampled.LineUnavailableException e) {
            fail("Exception thrown during MusicPlayer initialization: " + e.getMessage());
        }
    }

    @Test
    void testPlay() {
        try {
            MusicPlayer musicPlayer = new MusicPlayer(song);
            musicPlayer.play();
        } catch (UnsupportedAudioFileException | IOException | javax.sound.sampled.LineUnavailableException e) {
            fail("Exception thrown during MusicPlayer initialization: " + e.getMessage());
        }
    }

    @Test
    void testPlay2() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        MusicPlayer mp = new MusicPlayer(song);
        mp.play();
    }


}
