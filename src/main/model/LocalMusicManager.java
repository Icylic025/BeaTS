package model;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicManager {
    private Playlist playlist;
    private List<Song> uploadedSongs;

    public LocalMusicManager(Playlist playlist) {
        this.playlist = playlist;

    }

    public void uploadLocalSong(Song song) {
        playlist.addSong(song);
    }

    public void addPlaylist(Playlist p) {
        for (Song s : p.getSongs()) {
            playlist.addSong(s);
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }



    public void deleteSongFromPlaylist(int index) {
        playlist.deleteSong(index);
    }
}
