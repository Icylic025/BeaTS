package model;

import org.json.JSONObject;
import persistence.Writable;
import ui.threads.Playlist;
import ui.threads.Song;

/**
 * MasterMusicManager class manages a master playlist of all the songs the user has stored.
 * It is a storage and a bridge between the UI and all the function backend classes
 * A master music manager can be saved and loaded again. However, saving a master manager
 * does not save any local managers.
 * It might be confusing the difference between this class and LocalMusicManager,
 * here are the key differences:
 * - Only one MasterMusicManager should exist
 * - Once the MasterMusicManager is created, it should not be deleted
 * - The sole MasterMusicManager is passed through all UI classes
 *   therefore it should not be refreshed and lose progress of storage of user's music
 * - Deleting a song from the master playlist will permanently delete it until it is
 *   uploaded again (unless saved in a json).
 *
 */

public class MasterMusicManager implements Writable {
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
        EventLog.getInstance().logEvent(new Event("Song was uploaded to masterplaylist"));
        masterPlaylist.addSong(song);
    }


    public Playlist getMasterPlaylist() {
        return masterPlaylist;
    }

    public int getSize() {
        return masterPlaylist.getSize();
    }


    /**
     * Requires: Valid index within the range of the master playlist
     * Modifies: masterPlaylist
     * Effects: Deletes the song at the specified index from the master playlist.
     */
    public void deleteSongFromMasterPlaylist(int index) {

        EventLog.getInstance().logEvent(new Event("Song was deleted from master playlist"));
        masterPlaylist.deleteSong(index);
    }


    /**
     * Effects: parse itself into json
     */
    @Override
    public JSONObject toJson() {
        EventLog.getInstance().logEvent(new Event("Master playlist was converted to Json"));
        return masterPlaylist.toJson();
    }
}
