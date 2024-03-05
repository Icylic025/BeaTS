package persistence;

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

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    public MasterMusicManager readMaster() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMaster(jsonObject);
    }

    public LocalMusicManager readLocal() throws IOException {
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

    private MasterMusicManager parseMaster(JSONObject obj) {
        MasterMusicManager mm = new MasterMusicManager();
        JSONArray arr = obj.getJSONArray("songs");

        for (Object o : arr) {
            JSONObject nextSong = (JSONObject) o;
            addSongToMaster(mm, nextSong);
        }
        return mm;
    }

    private LocalMusicManager parseLocal(JSONObject obj) {
        LocalMusicManager lm = new LocalMusicManager(new Playlist());
        JSONArray arr = obj.getJSONArray("songs");

        for (Object o : arr) {
            JSONObject nextSong = (JSONObject) o;
            addSongToLocal(lm, nextSong);
        }
        return lm;
    }

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
