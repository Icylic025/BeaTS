package persistence;

import model.LocalMusicManager;
import model.MasterMusicManager;
import org.junit.jupiter.api.Test;
import ui.threads.Playlist;
import ui.threads.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.


    // the following tests are for master music manager
    @Test
    void testWriterInvalidFile() {
        try {
            MasterMusicManager mm = new MasterMusicManager();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyMasterMusicManager() {
        try {
            MasterMusicManager wr = new MasterMusicManager();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMasterMusicManager.json");
            writer.open();
            writer.write(wr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMasterMusicManager.json");
            wr = reader.readMaster();
            assertEquals(0, wr.getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralMasterMusicManager() {
        Song song1 = new Song("BTS", "Arson", "./data/Music/Arson.wav", 100);
        try {
            MasterMusicManager wr = new MasterMusicManager();
            wr.uploadSongToMaster(song1);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMasterMusicManager.json");
            writer.open();
            writer.write(wr);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMasterMusicManager.json");
            wr = reader.readMaster();
            Playlist playlist = wr.getMasterPlaylist();
            assertEquals(1, playlist.getSize());
            checkSong("Arson", "BTS", 100, playlist.getSong(0));



        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    // the following tests are for local music manager
    @Test
    void testWriterInvalidFileLocal() {
        try {
            LocalMusicManager lm = new LocalMusicManager(new Playlist());
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLocalMusicManager() {
        try {
            LocalMusicManager lm = new LocalMusicManager(new Playlist());
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyMasterMusicManager.json");
            writer.open();
            writer.write(lm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyMasterMusicManager.json");
            lm = reader.readLocal();
            assertEquals(0, lm.getPlaylist().getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralLocalMusicManager() {
        Song song1 = new Song("BTS", "Arson", "./data/Music/Arson.wav", 100);
        ArrayList<Song> list = new ArrayList<>();
        list.add(song1);
        try {
            LocalMusicManager lm = new LocalMusicManager(new Playlist(list));

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralMasterMusicManager.json");
            writer.open();
            writer.write(lm);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralMasterMusicManager.json");
            lm = reader.readLocal();
            Playlist playlist = lm.getPlaylist();
            assertEquals(1, playlist.getSize());
            checkSong("Arson", "BTS", 100, playlist.getSong(0));



        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}