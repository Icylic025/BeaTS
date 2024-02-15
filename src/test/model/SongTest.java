package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class SongTest {
    Song song;
    @BeforeEach
    void setUp() {
      song = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
    }

    @Test
    public void testPlaySong() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // Ensure the song is not playing initially
        assertFalse(song.getIsPlaying());

        // Play the song
        song.playSong();

        // After playing, the song should be marked as playing
        assertFalse(song.getIsPlaying());

        // After playback finishes, the song should not be playing
        song.notifyPlaybackFinished();
        assertFalse(song.getIsPlaying());
    }

    @Test
    public void testSimulateLineUnavailable() {
         // Simulate LineUnavailableException during playback
        song.setSimulateLineUnavailable(true);

        // Ensure that LineUnavailableException is thrown when playing the song
        assertThrows(LineUnavailableException.class, () -> song.playSongForTest());
    }

    @Test
    public void testSimulateIOException() {
        // Simulate IOException during playback
        song.setSimulateIOException(true);

        // Ensure that IOException is thrown when playing the song
        assertThrows(IOException.class, () -> song.playSongForTest());
    }
}
