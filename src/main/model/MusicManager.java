package model;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    private Playlist masterPlaylist;
    private List<Song> uploadedSongs;

    public MusicManager(Playlist masterPlaylist) {
        this.masterPlaylist = masterPlaylist;
        this.uploadedSongs = new ArrayList<>();
    }

    public void uploadSong(Song song) {
        uploadedSongs.add(song);
        masterPlaylist.addSong(song);
    }

    public List<Song> getUploadedSongs() {
        return uploadedSongs;
    }

    public Playlist getMasterPlaylist() {
        return masterPlaylist;
    }



    public void deleteSongFromMasterPlaylist(int index) {
        masterPlaylist.deleteSong(index);
    }
}
