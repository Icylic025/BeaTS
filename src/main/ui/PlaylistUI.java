package ui;

import model.MusicManager;
import model.Playlist;
import model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlaylistUI {

    Playlist playlist;
    Scanner scanner = new Scanner(System.in);
    MusicManager musicManager;

    public PlaylistUI(MusicManager musicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.musicManager = musicManager;
        playlist = musicManager.getMasterPlaylist();
        runPlaylistUI();
    }


    private void runPlaylistUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String command = "";
        displaySongs();
        command = takeCommand();
        implementCommand(command);
    }

    private void displaySongs() {
        int counter = 1;
        List<Song> songs = playlist.getSongs();
        System.out.println("\nHere is a list of all your currently stored songs: ");
        for (Song s : songs) {
            System.out.println("\t\t" + counter + ".  " + s.getTitle());
            counter++;
        }
    }

    public String takeCommand() {

        String command;

        do {
            System.out.println("Please select from the following options: ");
            System.out.println("\tl -> Listen to a Song");
            System.out.println("\tf -> Filter Song by BPM");
            System.out.println("\ts -> Shuffle Playlist");
            System.out.println("\ta -> Add Song");
            System.out.println("\td -> Delete Song");
            System.out.println("\tp -> Play All");
            System.out.println("\tb -> Back to Main Menu");
            System.out.println("\tq -> Quit Application");

            command = scanner.next();
            command = command.toLowerCase();
            if (command.equals("l") || command.equals("f") || command.equals("s") || command.equals("a")
                    || command.equals("d") || command.equals("p") || command.equals("b") || command.equals("q")) {
                return command;
            }
            System.out.println("Sorry " + command + "is not a valid input, please select from the menu.");

        } while (true);

    }

    public void implementCommand(String command)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        if (command.equals("q")) {
            System.exit(0);
        } else if (command.equals("b")) {
            return;
        } else if (command.equals("f")) {
            filter();
        } else if (command.equals("s")) {
            shuffle();
        } else if (command.equals("a")) {
            add();
        } else if (command.equals("p")) {
            playAll();
        } else if (command.equals("l")) {
            listen();
        } else {
            delete();
        }
    }

    public void filter() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int bpm;
        MusicManager filteredManager;
        System.out.println("Please input the BPM you want: ");
        bpm = Integer.parseInt(scanner.next());
        System.out.println("Here are the songs around this BPM: ");
        filteredManager = new MusicManager(playlist.filterByBpm(bpm));
        new PlaylistUI(filteredManager);
    }

    public void shuffle() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        MusicManager shuffledManager;
        System.out.println("Here is your playlist shuffled: ");
        shuffledManager = new MusicManager(playlist.shuffle());
        new PlaylistUI(shuffledManager);
    }

    public void add() {
        // idk if have time but need to look into upload from already uploaded/not filtered list
        new UploadSongUI(musicManager);
    }

    public void playAll() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playlist.playAll();
    }

    public void listen() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int index;

        while (true) {
            System.out.println("Please enter the number of the song you want to listen:");
            index = Integer.parseInt(scanner.next());
            if (index <= playlist.getSize() && index >= 1) {
                break;
            }
            System.out.println("Sorry " + index + "is not the number of a song. Please select from the list.");

        }
        playlist.playSong(index);
    }

    public void delete() {
        int index;
        while (true) {
            System.out.println("Please enter the number of the song you want to delete:");
            index = Integer.parseInt(scanner.next());
            if (index <= playlist.getSize() && index >= 1) {
                break;
            }
            System.out.println("Sorry " + index + "is not the number of a song. Please select from the list.");

        }
        musicManager.deleteSongFromMasterPlaylist(index - 1);
    }
}
