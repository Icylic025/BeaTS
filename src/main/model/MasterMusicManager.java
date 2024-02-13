package model;

import java.util.ArrayList;
import java.util.List;

public class MasterMusicManager {
    private Playlist masterPlaylist;

    public MasterMusicManager() {
        masterPlaylist = new Playlist();

    }

    public void uploadSongToMaster(Song song) {
        masterPlaylist.addSong(song);
    }


    public Playlist getMasterPlaylist() {
        return masterPlaylist;
    }



    public void deleteSongFromMasterPlaylist(int index) {
        masterPlaylist.deleteSong(index);
    }

}
