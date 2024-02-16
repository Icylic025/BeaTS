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
    private MusicPlayer player;

    @BeforeEach
    void setUp() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
      song = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
      player = new MusicPlayer(song);
    }


    @Test
    public void testPlaySong() {
        // Call the playSong() method
        try {
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred during playback.");
        }
    }

}
