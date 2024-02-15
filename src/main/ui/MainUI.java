package ui;

import model.LocalMusicManager;
import model.MasterMusicManager;
import model.Playlist;
import model.Song;

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
    Song cypher4 = new Song("BTS", "Cypher 4", "D:/Kylie/Bangtan/Music/Cypher 4.wav");
    Song zero = new Song("BTS", "Zero O'Clock", "D:/Kylie/Bangtan/Music/0000.wav");
    Song arson = new Song("BTS", "Arson", "D:/Kylie/Bangtan/Music/Arson.wav");
    Song amygdala = new Song("BTS", "Amygdala", "D:/Kylie/Bangtan/Music/Amygdala.wav");
    Song twentyOne = new Song("BTS", "21st Century Girl", "D:/Kylie/Bangtan/Music/21st Century Girl.wav");
    Song airplane = new Song("BTS", "Airplane pt 2", "D:/Kylie/Bangtan/Music/Airplane 2.wav");
    Song dimple = new Song("BTS", "dimple", "D:/Kylie/Bangtan/Music/Dimple.wav");
    Song dontLeaveMe = new Song("BTS", "Don't Leave Me", "D:/Kylie/Bangtan/Music/Don't Leave Me.wav");

    /*
    Song filmOut = new Song("BTS", "filmOut", "D:/Kylie/Bangtan/Music/Film Out.wav");
    Song fire = new Song("BTS", "Fire", "D:/Kylie/Bangtan/Music/Fire.wav");
    Song friends = new Song("BTS", "friends", "D:/Kylie/Bangtan/Music/Friends.wav");
    Song maCity = new Song("BTS", "Ma City ", "D:/Kylie/Bangtan/Music/Ma City.wav");

    Song magicShop = new Song("BTS", " Magic Shop ", "D:/Kylie/Bangtan/Music/Magic Shop.wav");
    Song polarNight = new Song("BTS", " PolarNight ", "D:/Kylie/Bangtan/Music/Polar Night.wav");
    Song shadow = new Song("BTS", " shadow ", "D:/Kylie/Bangtan/Music/Shadow.wav");
    Song  silverSpoon = new Song("BTS", " Silver Spoon ", "D:/Kylie/Bangtan/Music/Silver Spoon.wav");
    Song soFarAway = new Song("BTS", " so far away ", "D:/Kylie/Bangtan/Music/So Far Away.wav");

*/




/**
 * Modifies: masterMusicManager
 * Effects: Constructs a MainUI object, initializes a scanner for user input,
 * and uploads predefined songs to the master music manager.
 */
    public MainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {


        masterMusicManager.uploadSongToMaster(cypher4);
        masterMusicManager.uploadSongToMaster(zero);
        masterMusicManager.uploadSongToMaster(arson);
        masterMusicManager.uploadSongToMaster(amygdala);
        masterMusicManager.uploadSongToMaster(twentyOne);
        masterMusicManager.uploadSongToMaster(airplane);
        masterMusicManager.uploadSongToMaster(dimple);
        masterMusicManager.uploadSongToMaster(dontLeaveMe);

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
