package model;

import ui.threads.Playlist;
import ui.threads.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MasterMusicManagerTest {

    private MasterMusicManager masterMusicManager;
    private Song song1;
    private Song song2;
    private Song song3;
    private Song song4;
    private ArrayList<Song> songs;
    private Playlist playlist;
    Playlist masterPlaylist;

    @BeforeEach
    void setUp() {
        song1 = new Song("BTS", "Magic Shop", "./data/Music/Music/Whistle.wav");
        song2 = new Song("BTS", "PolarNight", "./data/Music/Whistle.wav");
        song3 = new Song("BTS", "Shadow", "./data/Music/Whistle.wav");
        song4 = new Song("BTS", "Silver Spoon", "./data/Music/Whistle.wav");
        songs = new ArrayList<>();
        songs.add(song1);
        songs.add(song2);
        songs.add(song3);
        songs.add(song4);
        playlist = new Playlist(songs);
        masterMusicManager = new MasterMusicManager();
    }

    @Test
    void testUploadSongToMaster() {
        masterMusicManager.uploadSongToMaster(song1);
        masterMusicManager.uploadSongToMaster(song2);
        Playlist masterPlaylist = masterMusicManager.getMasterPlaylist();
        assertTrue(masterPlaylist.getSongs().contains(song1));
        assertTrue(masterPlaylist.getSongs().contains(song2));
    }

    @Test
    void testDeleteSongFromMasterPlaylist() {
        masterMusicManager.uploadSongToMaster(song1);
        masterMusicManager.uploadSongToMaster(song2);
        masterMusicManager.uploadSongToMaster(song3);
        masterMusicManager.uploadSongToMaster(song4);
        masterPlaylist = masterMusicManager.getMasterPlaylist();

        assertTrue(masterPlaylist.getSongs().contains(song3));
        masterMusicManager.deleteSongFromMasterPlaylist(1);

        assertEquals(3, masterPlaylist.getSongs().size());
        assertTrue(masterPlaylist.getSongs().contains(song1));
        assertTrue(masterPlaylist.getSongs().contains(song3));
        assertTrue(masterPlaylist.getSongs().contains(song4));
    }
}
