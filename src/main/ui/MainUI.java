package ui;

import ui.threads.Song;
import model.MasterMusicManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The MainUI class represents the main user interface. It runs
 * at the start of the program and is the only thing Main calls.
 * The MainUI then calls upon other UIs depending on the user
 * inputs and directs the flow of the program.
 */

public class MainUI {

    private Scanner input = new Scanner(System.in);
    private MasterMusicManager masterMusicManager = new MasterMusicManager();

    ArrayList<Song> songs = new ArrayList<Song>();

    /*
    Song cypher4 = new Song("BTS", "Cypher 4", "./data/Music/Cypher 4.wav");
    Song arson = new Song("BTS", "Arson", "./data/Music/Arson.wav");
    Song twentyOne = new Song("BTS", "21st Century Girl", "./data/Music/21st Century Girl.wav");
    Song dontLeaveMe = new Song("BTS", "Don't Leave Me", "./data/Music/Whistle.wav");
    */
    Song whistle = new Song("BTS", "Whistle", "./data/Music/Whistle.wav");


    /**
 * Modifies: masterMusicManager
 * Effects: Constructs a MainUI object, initializes a scanner for user input,
 * and uploads predefined songs to the master music manager.
 */
    public MainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        masterMusicManager.uploadSongToMaster(whistle);
        /*
        masterMusicManager.uploadSongToMaster(cypher4);
        masterMusicManager.uploadSongToMaster(arson);
        masterMusicManager.uploadSongToMaster(twentyOne);
        masterMusicManager.uploadSongToMaster(dontLeaveMe);
        */

        /*
        masterMusicManager.uploadSongToMaster(filmOut);
        masterMusicManager.uploadSongToMaster(fire);
        masterMusicManager.uploadSongToMaster(friends);
        masterMusicManager.uploadSongToMaster(maCity);
        masterMusicManager.uploadSongToMaster(magicShop);
        masterMusicManager.uploadSongToMaster(polarNight);
        masterMusicManager.uploadSongToMaster(shadow);
        masterMusicManager.uploadSongToMaster(silverSpoon);
        masterMusicManager.uploadSongToMaster(soFarAway);
*/


        do {
            runMainUI();
        } while (true);
    }


    /**
     * Effects: Displays the main menu options, prompts the user for input,
     * and executes the selected command.
     */
    private void runMainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String command;

        System.out.println("\nMain Menu:");
        System.out.println("\tPlease select from the following options:");
        System.out.println("\t u -> Upload Song");
        System.out.println("\t v -> View Songs");
        System.out.println("\t m -> Manual Beat Detection");
        System.out.println("\t q -> Quit Application\n");

        command = input.next();
        command = command.toLowerCase();

        implementCommand(command);


    }

    /**
     *  Effects: Executes the command specified by the user,
     *              either uploading a song, viewing songs, performing manual beat detection,
     *              or exiting the application.
     */
    private void implementCommand(String command)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        if (command.equals("q")) {
            System.exit(0);
        } else if (command.equals("u")) {
            new UploadSongUI(masterMusicManager);
        } else if (command.equals("v")) {
            new PlaylistUI(masterMusicManager);
        } else if (command.equals("m")) {
            new ManualBeatsUI(masterMusicManager);
        } else {
            System.out.println("Sorry " + command + " was not a valid input, please select from the menu.");
        }
    }

}
