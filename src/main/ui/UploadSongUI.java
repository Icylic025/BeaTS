package ui;

import model.LocalMusicManager;
import model.MasterMusicManager;
import model.Song;

import javax.sound.midi.Soundbank;
import java.util.Scanner;

public class UploadSongUI {
    Scanner scanner = new Scanner(System.in);

    public UploadSongUI(MasterMusicManager masterMusicManager) {
        Song selectedSong = selectSongFromUser();
        masterMusicManager.uploadSongToMaster(selectedSong);
    }


    private Song selectSongFromUser() {
        Scanner scanner = new Scanner(System.in);
        String input;
        String title;
        String artist;
        String filepath;

        System.out.println("Please enter the title of the song to upload: ");
        title = scanner.nextLine();

        System.out.println("Please enter the artist: ");
        artist = scanner.nextLine();


        System.out.println("Please enter the filepath of the song to upload: ");
        filepath = scanner.nextLine();

        System.out.println("Please enter the name of the file you wish to upload: ");

        try {
            Song upload = new Song(artist, title, filepath);
        } catch (Exception e) {
            System.out.println("Something went wrong with the upload.");
        }

        return new Song(artist, title, filepath);
    }

}
