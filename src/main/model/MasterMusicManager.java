package model;

import threads.Playlist;
import threads.Song;

/**
 * MasterMusicManager class manages a master playlist of all the songs the user has stored.
 * It is a storage and a bridge between the UI and all the function backend classes
 *
 * It might be confusing the difference between this class and LocalMusicManager,
 * here are the key differences:
 * - Only one MasterMusicManager should exist
 * - Once the MasterMusicManager is created, it should not be deleted
 * - The sole MasterMusicManager is passed through all UI classes
 *   therefore it should not be refreshed and lose progress of storage of user's music
 * - Deleting a song from the master playlist will permanently delete it until it is
 *   uploaded again.
 *
 */

public class MasterMusicManager {
    private Playlist masterPlaylist;

    /**
     * Effects: Constructs a MasterMusicManager object and initializes the master playlist.
     */
    public MasterMusicManager() {
        masterPlaylist = new Playlist();

    }

    /**
     * Requires: Non-null Song object
     * Modifies: masterPlaylist
     * Effects: Uploads the given song to the master playlist.
     */
    public void uploadSongToMaster(Song song) {
        masterPlaylist.addSong(song);
    }


    public Playlist getMasterPlaylist() {
        return masterPlaylist;
    }


    /**
     * Requires: Valid index within the range of the master playlist
     * Modifies: masterPlaylist
     * Effects: Deletes the song at the specified index from the master playlist.
     */
    public void deleteSongFromMasterPlaylist(int index) {
        masterPlaylist.deleteSong(index);
    }

}
