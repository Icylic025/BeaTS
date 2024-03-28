package model;

import org.junit.jupiter.api.*;
import ui.threads.Playlist;
import ui.threads.Song;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LocalMusicManagerTest {
    private LocalMusicManager localMusicManager;
    private ArrayList<Song> songs = new ArrayList<>();
    private Playlist playlist;
    private Song song1;
    private Song song2;
    private Song song3;
    private Song song4;

    @BeforeEach
    void setUp() {
        song1 = new Song("BTS", "Magic Shop", "./data/Music/Whistle.wav");
        song2 = new Song("BTS", "PolarNight", "./data/Music/Whistle.wav");
        song3 = new Song("BTS", "Shadow", "./data/Music/Music/Whistle.wav");
        song4 = new Song("BTS", "Silver Spoon", "./data/Music/Whistle.wav");
        songs.add(song1);
        songs.add(song2);
        playlist = new Playlist(songs);
        localMusicManager = new LocalMusicManager(playlist);
    }

    @Test
    void testUploadLocalSong() {
        Song newSong = new Song("BTS", "Friends", "./data/Music/Whistle.wav");
        localMusicManager.uploadLocalSong(newSong);
        assertEquals(3, localMusicManager.getPlaylist().getSongs().size());
    }

    @Test
    void testAddPlaylist() {
        Playlist newPlaylist = new Playlist();
        newPlaylist.addSong(song3);
        newPlaylist.addSong(song4);
        localMusicManager.addPlaylist(newPlaylist);
        assertEquals(4, localMusicManager.getPlaylist().getSongs().size());
    }

    @Test
    void testDeleteSongFromPlaylist() {
        localMusicManager.deleteSongFromPlaylist(1); // Deleting song2
        assertEquals(1, localMusicManager.getPlaylist().getSongs().size());
        assertEquals("Magic Shop", localMusicManager.getPlaylist().getSongs().get(0).getTitle());
    }

    @Test
    void testShuffle() {
        // Create a sample playlist
        Playlist playlist = new Playlist();
        playlist.addSong(new Song("Song 1"));
        playlist.addSong(new Song("Song 2"));
        playlist.addSong(new Song("Song 3"));

        // Create a LocalMusicManager with the sample playlist
        LocalMusicManager localManager = new LocalMusicManager(playlist);

        // Shuffle the playlist
        localManager.shuffle();

        // Assert that all songs are still present in the shuffled playlist
        assertEquals(3, localManager.getPlaylist().getSongs().size());
    }

    @Test
    void testFilter() {
        // Create a sample playlist
        Playlist playlist = new Playlist();
        playlist.addSong(new Song("Song 1", 120));
        playlist.addSong(new Song("Song 2", 140));
        playlist.addSong(new Song("Song 3", 100));

        // Create a LocalMusicManager with the sample playlist
        LocalMusicManager localManager = new LocalMusicManager(playlist);

        // Filter the playlist by BPM
        localManager.filter(120);

        // Assert that only the song with BPM 120 is present in the filtered playlist
        assertEquals(1, localManager.getPlaylist().getSongs().size());
        assertEquals("Song 1", localManager.getPlaylist().getSongs().get(0).getTitle());
    }
}
