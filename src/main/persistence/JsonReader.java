package persistence;

import model.Event;
import model.EventLog;
import model.LocalMusicManager;
import model.MasterMusicManager;
import org.json.JSONArray;
import org.json.JSONObject;
import ui.threads.Playlist;
import ui.threads.Song;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class JsonReader {

    private String source;

    /**
     * This is a class that works together with JsonReader to support data persistance in this project. It works
     *  It is in charge of reading stored Json files created by JsonWriter and parsing them into Local or Master
     *  Music Managers
     */

    /**
     * EFFECTS: constructs reader to read from source file
     */
    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * Effects: reads the json data for a MasterMusicManager and
     * parses it into a MasterMusicManager, will throw IOException
     * if there is a problem with reading in the json file
     */
    public MasterMusicManager readMaster() throws IOException {
        EventLog.getInstance().logEvent(new Event("A saved Master Playlist has been uploaded"));
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMaster(jsonObject);
    }

    /**
     * Effects: reads the json data for a LocalMusicManager and
     * parses it into a LocalMusicManager, will throw IOException
     * if there is a problem with reading in the json file
     */
    public LocalMusicManager readLocal() throws IOException {
        EventLog.getInstance().logEvent(new Event("A saved Local Playlist has been uploaded"));

        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLocal(jsonObject);
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    /**
     * Effects: returns a MasterMusicManager from a Json object by going through
     * through each song
     */
    private MasterMusicManager parseMaster(JSONObject obj) {
        MasterMusicManager mm = new MasterMusicManager();
        JSONArray arr = obj.getJSONArray("songs");

        for (Object o : arr) {
            JSONObject nextSong = (JSONObject) o;
            addSongToMaster(mm, nextSong);
        }
        return mm;
    }

    /**
     * Effects: returns a LocalMusicManager from a Json object by going through
     * through each song
     */
    private LocalMusicManager parseLocal(JSONObject obj) {
        LocalMusicManager lm = new LocalMusicManager(new Playlist());
        JSONArray arr = obj.getJSONArray("songs");

        for (Object o : arr) {
            JSONObject nextSong = (JSONObject) o;
            addSongToLocal(lm, nextSong);
        }
        return lm;
    }

    /**
     * Effects: form a song object from a json object and adds it to the mastermusic manager
     * */
    private void addSongToMaster(MasterMusicManager m, JSONObject s) {
        String title = s.getString("title");
        String artist = s.getString("artist");
        String filepath = s.getString("filepath");
        int bpm = s.getInt("bpm");

        Song song = new Song(artist, title, filepath, bpm);
        m.uploadSongToMaster(song);

    }


    private void addSongToLocal(LocalMusicManager l, JSONObject s) {
        String title = s.getString("title");
        String artist = s.getString("artist");
        String filepath = s.getString("filepath");
        int bpm = s.getInt("bpm");

        Song song = new Song(artist, title, filepath, bpm);
        l.uploadLocalSong(song);

    }
}
