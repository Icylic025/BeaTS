package model;

import threads.MusicPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import threads.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MusicPlayerTest {

    private Song song;
    private MusicPlayer mp;
    @BeforeEach
    void setUp() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        song = new Song("BTS", "Whistle", "./data/Music/Whistle.wav");
        mp = new MusicPlayer(song);
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
        mp.play();
    }


}
