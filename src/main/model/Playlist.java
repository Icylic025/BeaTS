package model;

import java.util.ArrayList;

public class Playlist {
    private ArrayList<Song> songs;

    // construct empty playlist
    public Playlist() {
        songs = new ArrayList<Song>();
    }

    // contruct playlist with given songs
    public Playlist(ArrayList<Song> list) {
        songs = list;
    }
}


