package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.Playlist;
import ui.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistTest {

    private Playlist playlist;
    Song newSong;

    ArrayList<Song> songs;

    @BeforeEach
    void setUp() {
        songs = new ArrayList<>();
        Song song1 = new Song("BTS", "Magic Shop", "./data/Music/Whistle.wav");
        Song song2 = new Song("BTS", "PolarNight", "./data/Music/Whistle.wav");
        Song song3 = new Song("BTS", "Shadow", "./data/Music/Whistle.wav");
        Song song4 = new Song("BTS", "Silver Spoon", "./data/Music/Whistle.wav");
        newSong = new Song("BTS", "Fire", "./data/Music/Whistle.wav");

        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
        songs.add(song4);
        playlist = new Playlist(songs);
    }

    @Test
    void testFilterByBpm() {
        Playlist filteredPlaylist = playlist.filterByBpm(130);
        assertEquals(0, filteredPlaylist.getSize());
    }

    @Test
    public void testFilterByBpmInRange() {
        int targetBpm = 120;
        Playlist filteredPlaylist = playlist.filterByBpm(targetBpm);

        assertEquals(0, filteredPlaylist.getSize()); // 2 same range
   }

    @Test
    public void testFilterByBpmBelowLowerBound() {
        int targetBpm = 100;
        playlist.getSongs().get(0).setBpm(70);
        Playlist filteredPlaylist = playlist.filterByBpm(targetBpm);

        assertEquals(0, filteredPlaylist.getSize());
    }
    @Test
    public void testFilterByBpmInBound() {
        int targetBpm = 100;
        playlist.getSongs().get(0).setBpm(100);
        playlist.getSongs().get(1).setBpm(110);
        playlist.getSongs().get(2).setBpm(90);
        Playlist filteredPlaylist = playlist.filterByBpm(targetBpm);

        assertEquals(3, filteredPlaylist.getSize());
    }

    @Test
    void testPlaySong() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playlist.playSong(0);
    }
    @Test
    public void testFilterByBpmAboveUpperBound() {
        int targetBpm = 140;
        playlist.getSongs().get(0).setBpm(170);
        Playlist filteredPlaylist = playlist.filterByBpm(targetBpm);

        assertEquals(0, filteredPlaylist.getSize());
    }

    @Test
    public void testFilterByBpmEmptyPlaylist() {
        Playlist emptyPlaylist = new Playlist(new ArrayList<>());

        int targetBpm = 120;
        Playlist filteredEmptyPlaylist = emptyPlaylist.filterByBpm(targetBpm);

        assertEquals(0, filteredEmptyPlaylist.getSize());

    }

    @Test
    void testPlayAll() {
        // Since playAll() method waits for each song to finish playing, testing is limited
        // Ensure there are no exceptions thrown
        assertDoesNotThrow(() -> playlist.playAll());
    }

    @Test
    public void testPlayAll_NormalPlayback() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        for (Song song : songs) {
            song.setPlaying(false); // Initialize as not playing
        }

        playlist.playAll();

    }



    @Test
    void testAddSong() {
        playlist.addSong(newSong);
        assertEquals(5, playlist.getSize());
    }

    @Test
    void testDeleteSong() {
        playlist.deleteSong(0);
        assertEquals(3, playlist.getSize());
    }

    @Test
    void testShuffle() {

        Playlist shuffledPlaylist = playlist.shuffle();
        assertNotSame(playlist.getSongs(), shuffledPlaylist.getSongs());
    }

    @Test
    void testAddPlaylist() {
        Playlist additionalPlaylist = new Playlist();
        additionalPlaylist.addSong(newSong);
        playlist.addPlaylist(additionalPlaylist);
        assertEquals(5, playlist.getSize());
    }
}
