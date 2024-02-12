package model;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Playlist {
    private ArrayList<Song> songs;

    // construct empty playlist
    public Playlist() {
        songs = new ArrayList<Song>();
    }

    // construct playlist with given songs
    public Playlist(ArrayList<Song> list) {
        songs = list;
    }

    // filter list by bpm, return new playlist
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

    public void playAll() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        // Variable to keep track of whether the previous song has finished playing
        boolean previousSongFinished = true;

        for (Song s : songs) {
            // Wait for the previous song to finish playing before starting the next one
            if (!previousSongFinished) {
                try {
                    Thread.sleep(1000); // Adjust sleep time as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Play the current song
            s.playSong();

            // Set the flag to false, indicating that the current song is playing
            previousSongFinished = false;
        }
    }


    public void playSong(int index) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        songs.get(index).playSong();
    }

    public void addSong(Song s) {
        songs.add(s);
    }

    public void deleteSong(int i) {
        songs.remove(i);
    }

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

    public int getSize() {
        return songs.size();
    }
}


