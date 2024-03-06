package model;

import org.json.JSONObject;
import persistence.Writable;
import ui.threads.Playlist;
import ui.threads.Song;

import java.util.List;

/**
 * LocalMusicManager class is a bridge between the UI and all the function backend classes
 * It stores local music that the user creates ie. a filtered playlist, or a shuffled playlist.
 * A local music manager can also be saved and loaded from Json. Saving a local manager also
 * saves the master music manager, however loading a local music manager will not load the master.
 * It might be confusing the difference between this class and MasterMusicManager,
 * here are the key differences:
 *  - More than one LocalMusicManager can exist although only one is shown and the rest
 *    will just stack
 *  - Deleting a song from the LocalMusicManager is not permenant deletion, it still exists
 *    in the program and the master playlist
 *  - Once you close a LocalMusicManager, it is gone unless you have saved it. ie. if you shuffle a playlist
 *    and go back to main menu, the shuffled list is lost.
 *
 */

public class LocalMusicManager implements Writable {
    private Playlist playlist;
    private List<Song> uploadedSongs;

    /**
     * Requires: Valid Playlist object
     * Modifies: this
     * Effects: Constructs a LocalMusicManager object with the provided Playlist.
     *          Initializes the list of uploaded songs.
     */
    public LocalMusicManager(Playlist playlist) {
        this.playlist = playlist;

    }

    /**
     * Requires: Non-null Song object
     * Modifies: playlist
     * Effects: Uploads the given song to the playlist.
     */
    public void uploadLocalSong(Song song) {
        playlist.addSong(song);
    }

    /**
     * Requires: Non-null Playlist object
     * Modifies: playlist
     * Effects: Adds all songs from the given playlist to the current playlist.
     */
    public void addPlaylist(Playlist p) {
        for (Song s : p.getSongs()) {
            playlist.addSong(s);
        }
    }

    /**
     * Effects: Retrieves the playlist managed by the LocalMusicManager.
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    /**
     * Requires: Valid index within the range of the playlist
     * Modifies: playlist
     * Effects: Deletes the song at the specified index from the playlist.
     */
    public void deleteSongFromPlaylist(int index) {
        playlist.deleteSong(index);
    }

    @Override
    public JSONObject toJson() {
        return playlist.toJson();
    }

}
