package persistence;


import model.LocalMusicManager;
import model.MasterMusicManager;
import org.junit.jupiter.api.Test;
import ui.threads.Playlist;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFileMaster() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            MasterMusicManager mm = reader.readMaster();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoomMaster() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            MasterMusicManager mm = reader.readMaster();
            assertEquals(0, mm.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoomMaster() {
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

    @Test
    void testReaderNonExistentFileLocal() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            LocalMusicManager lm = reader.readLocal();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoomLocal() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            LocalMusicManager lm = reader.readLocal();
            assertEquals(0, lm.getPlaylist().getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoomLocal() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");
        try {

            LocalMusicManager lm = reader.readLocal();
            Playlist playlist = lm.getPlaylist();
            assertEquals(5, playlist.getSize());
            checkSong("Whistle", "BTS", 0, playlist.getSong(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}