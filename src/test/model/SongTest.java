package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SongTest {
    Song song;
    @BeforeEach
    void setUp() {
      song = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
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

    @Test
    public void testPlaySong() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException {
        // Mocking the MusicPlayer class to avoid actual audio playback during tests
        MusicPlayer mockedPlayer = mock(MusicPlayer.class);

        // Play the song
        song.playSong();

        // Ensure that MusicPlayer.play() method is called
        verify(mockedPlayer, times(1)).play();

        // Ensure that wait() method is called after playback starts
        verify(song, times(1)).wait();
    }
}
