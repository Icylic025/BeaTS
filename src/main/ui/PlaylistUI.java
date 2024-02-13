package ui;

import model.LocalMusicManager;
import model.MasterMusicManager;
import model.Playlist;
import model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class PlaylistUI {

    Playlist playlist;
    Scanner scanner = new Scanner(System.in);
    LocalMusicManager localMusicManager = new LocalMusicManager(new Playlist());
    MasterMusicManager masterMusicManager;

    public PlaylistUI(MasterMusicManager masterMusicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.masterMusicManager = masterMusicManager;
        playlist = masterMusicManager.getMasterPlaylist();
        runPlaylistUI();
    }

    public PlaylistUI(LocalMusicManager localMusicManager,MasterMusicManager masterMusicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playlist = localMusicManager.getPlaylist();
        this.masterMusicManager = masterMusicManager;
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
            add(masterMusicManager.getMasterPlaylist(), localMusicManager);
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
        LocalMusicManager filteredManager;
        System.out.println("Please input the BPM you want: ");
        bpm = Integer.parseInt(scanner.next());
        System.out.println("Here are the songs around this BPM: ");
        filteredManager = new LocalMusicManager(playlist.filterByBpm(bpm));
        new PlaylistUI(filteredManager, masterMusicManager);
    }

    public void shuffle() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        LocalMusicManager shuffledManager;
        System.out.println("Here is your playlist shuffled: ");
        shuffledManager = new LocalMusicManager(playlist.shuffle());
        new PlaylistUI(shuffledManager, masterMusicManager);
    }

    public void add(Playlist masterPlaylist, LocalMusicManager localMusicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int songIndex;
        System.out.println("Please select a song to add to your local playlist:");
        displayMasterPlaylist(masterPlaylist);

        // Let the user choose from the master playlist

        while (true) {
            songIndex = Integer.parseInt(scanner.next());
            if (songIndex <= masterPlaylist.getSize() && songIndex >= 1) {
                break;
            }
            System.out.println("Sorry " + songIndex + "is not the number of a song. Please select from the list.");

        }

        Song selectedSong = masterPlaylist.getSongs().get(songIndex - 1);

        // Add selected song to local playlist
        localMusicManager.uploadLocalSong(selectedSong);
        System.out.println(selectedSong.getTitle() + " has been added to your local playlist");

        // Update the local playlist with the new song
        playlist.addSong(selectedSong);

        // Refresh the display
        new PlaylistUI(new LocalMusicManager(playlist), masterMusicManager);
    }

    public void playAll() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playlist.playAll();
        new PlaylistUI(new LocalMusicManager(playlist), masterMusicManager);

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
        playlist.playSong(index - 1);
        new PlaylistUI(new LocalMusicManager(playlist), masterMusicManager);

    }

    public void delete() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int index;
        while (true) {
            System.out.println("Please enter the number of the song you want to delete:");
            index = Integer.parseInt(scanner.next());
            if (index <= playlist.getSize() && index >= 1) {
                break;
            }
            System.out.println("Sorry " + index + " is not the number of a song. Please select from the list.");
        }
       // System.out.println(localMusicManager.getPlaylist());

        // Update the local playlist
        playlist.deleteSong(index - 1);

        // Refresh the display
        new PlaylistUI(new LocalMusicManager(playlist), masterMusicManager);
    }

    private void displayMasterPlaylist(Playlist playlist) {
        System.out.println("Master Playlist:");
        int index = 1;
        for (Song song : playlist.getSongs()) {
            System.out.println(index++ + ". " + song.getTitle());
        }
    }

}
