package persistence;

import ui.threads.Song;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkSong(String title, String artist, int bpm, Song s) {
        assertEquals(title, s.getTitle());
        assertEquals(artist, s.getArtist());
        assertEquals(bpm, s.getBpm());
    }
}
