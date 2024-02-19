package ui;

import ui.threads.Song;
import model.MasterMusicManager;

import java.util.Scanner;

/**
 * The UploadSongUI class manages the user interface for uploading songs to the MasterMusicManager.
 * It prompts the user to enter details such as the title, artist, and filepath of the song,
 * creates a Song object with the provided details, and uploads it to the MasterMusicManager.
 */

public class UploadSongUI {

    /**
     * Requires: masterMusicManager to be initialized with a valid instance of MasterMusicManager.
     * Modifies: masterMusicManager
     * Effects: Constructs an UploadSongUI object, prompts the user to input the details of a song,
     *          creates a Song object with the provided details, and uploads it to the master music manager.
     */
    public UploadSongUI(MasterMusicManager masterMusicManager) {
        Song selectedSong = selectSongFromUser();
        masterMusicManager.uploadSongToMaster(selectedSong);
    }


    /**
     * Effects: Prompts the user to enter the title, artist, and filepath of the song to be uploaded,
     *          creates a Song object with the provided details, and returns it.
     */
    private Song selectSongFromUser() {
        Scanner scanner = new Scanner(System.in);
        String title;
        String artist;
        String filepath;
        String input;

        System.out.println("Please enter the title of the song to upload: ");
        title = scanner.nextLine();

        System.out.println("Please enter the artist: ");
        artist = scanner.nextLine();


        System.out.println("Please go upload the song as a WAV file "
                + "titled the name of the song to /data/Music of this project: ");
        System.out.println("Press Enter when file is uploaded");
        scanner.nextLine();
        filepath = "./data/Music/" + title + ".wav";

        try {
            System.out.println("Song is uploading, please wait (this may take a while)");
            Song upload = new Song(artist, title, filepath);
        } catch (Exception e) {
            System.out.println("Something went wrong with the upload.");
        }

        System.out.println(title + " by " + artist + " has been successfully uploaded");
        return new Song(artist, title, filepath);
    }

}
