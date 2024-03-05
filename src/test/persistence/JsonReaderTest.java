package persistence;


import model.MasterMusicManager;
import org.junit.jupiter.api.Test;
import ui.threads.Playlist;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MasterMusicManager mm = reader.readMaster();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            MasterMusicManager mm = reader.readMaster();
            assertEquals(0, mm.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");
        try {

            MasterMusicManager mm = reader.readMaster();
            Playlist playlist = mm.getMasterPlaylist();
            assertEquals(5, playlist.getSize());
            checkSong("Whistle", "BTS", 0, playlist.getSong(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}