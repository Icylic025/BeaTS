package ui;

import model.MusicManager;
import model.Playlist;
import model.Song;

import java.util.Scanner;

public class UploadSongUI {
    Scanner scanner = new Scanner(System.in);

    public UploadSongUI(MusicManager musicManager) {
        System.out.println("Please enter the name of the file you wish to upload: ");
        // make song object from stuff that idk how to do
        // for now we make fake song

        Song upload = new Song("BTS", "Cypher 4", "D:/Kylie/Bangtan/Music/Cypher 4.wav");

        musicManager.uploadSong(upload);
        System.out.println(upload.getTitle() + " has been uploaded successfully");

    }


}
