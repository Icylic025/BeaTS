package ui.threads;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Playlist class represents a collection of Song object with various functionalities for managing and playing songs.
 * It allows users to construct playlists, filter songs by BPM, play songs sequentially or individually,
 * add and delete songs, shuffle the playlist, and retrieve information about the playlist.
 */

public class Playlist implements Writable {
    private ArrayList<Song> songs;

    /**
     * Effects: Constructs an empty Playlist object.
     */
    public Playlist() {
        songs = new ArrayList<Song>();
    }

    /**
     * Requires: Non-null list of songs
     * Modifies: this
     * Effects: Constructs a Playlist object with the given list of songs.
     */
    public Playlist(ArrayList<Song> list) {
        songs = list;
    }

    /**
     * Effects: Filters the list of songs by BPM within a specified range and returns a new Playlist object.
     */
    public Playlist filterByBpm(int bpm) {
        int range = 10;
        ArrayList<Song> filteredList = new ArrayList<Song>();
        for (Song s : songs) {
            // find bpm within range
            if (s.getBpm() <= bpm + range && s.getBpm() >= bpm - range) {
                filteredList.add(s);
            }
        }
        return new Playlist(filteredList);
    }

    /**
     * Effects: Plays all songs in the playlist sequentially, waiting for each song to finish before starting the next.
     */
    public void playAll() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        // Variable to keep track of whether the previous song has finished playing
        boolean previousSongFinished = true;

        for (Song s : songs) {
            // Wait for the previous song to finish playing before starting the next one
            if (!previousSongFinished) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            s.playSong();

            // current song is playing, no moving
            previousSongFinished = false;
        }


    }

    /**
     * Requires: Valid index within the range of the playlist
     * Effects: Plays the song at the specified index in the playlist.
     */
    public void playSong(int index) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        songs.get(index).playSong();
    }

    /**
     * Requires: Non-null Song object
     * Modifies: this
     * Effects: Adds the given song to the playlist.
     */
    public void addSong(Song s) {
        songs.add(s);
    }

    /**
     * Requires: Valid index within the range of the playlist
     * Modifies: this
     * Effects: Deletes the song at the specified index from the playlist.
     */
    public void deleteSong(int i) {
        songs.remove(i);
    }

    /**
     * Effects: Shuffles the songs in the playlist and returns a new Playlist object.
     */
    public Playlist shuffle() {
        ArrayList<Song> shuffledList = new ArrayList<Song>();
        Collections.shuffle(songs);

        for (Song s : songs) {
            shuffledList.add(s);
        }

        return new Playlist(shuffledList);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public Song getSong(int i) {
        return songs.get(i);
    }

    public int getSize() {
        return songs.size();
    }



    /**
     * Requires: Non-null Playlist object
     * Modifies: this
     * Effects: Adds all songs from the given playlist to the current playlist.
     */
    public void addPlaylist(Playlist p) {
        for (Song s : p.getSongs()) {
            songs.add(s);
        }
    }

    @Override
    public JSONObject toJson() {

        JSONArray jsonArray = new JSONArray();
        for (Song song : songs) {
            jsonArray.put(song.toJson()); // Assuming toJson() is a method in Song that returns a JSONObject
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("songs", jsonArray); // Directly use "songs" here
        return jsonObject;


    }
}


