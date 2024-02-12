package ui;

import model.MusicManager;
import model.Playlist;
import model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainUI {

    private Scanner input = new Scanner(System.in);
    private MusicManager musicManager = new MusicManager(new Playlist());

    ArrayList<Song> songs = new ArrayList<Song>();
    Song cypher4 = new Song("BTS", "Cypher 4", "D:/Kylie/Bangtan/Music/Cypher 4.wav");
    Song zero = new Song("BTS", "Zero O'Clock", "D:/Kylie/Bangtan/Music/0000.wav");
    Song arson = new Song("BTS", "Arson", "D:/Kylie/Bangtan/Music/Arson.wav");

    Song amygdala = new Song("BTS", "Amygdala", "D:/Kylie/Bangtan/Music/Amygdala.wav");
    Song twentyOne = new Song("BTS", "21st Century Girl", "D:/Kylie/Bangtan/Music/21st Century Girl.wav");
    Song airplane = new Song("BTS", "Airplane pt 2", "D:/Kylie/Bangtan/Music/Airplane 2.wav");
    Song dimple = new Song("BTS", "dimple", "D:/Kylie/Bangtan/Music/Dimple.wav");
    Song dontLeaveMe = new Song("BTS", "Don't Leave Me", "D:/Kylie/Bangtan/Music/Don't Leave Me.wav");
    Song filmOut = new Song("BTS", "filmOut", "D:/Kylie/Bangtan/Music/Film Out.wav");
    Song fire = new Song("BTS", "Fire", "D:/Kylie/Bangtan/Music/Fire.wav");
    Song friends = new Song("BTS", "friends", "D:/Kylie/Bangtan/Music/Friends.wav");
    Song maCity = new Song("BTS", "Ma City ", "D:/Kylie/Bangtan/Music/Ma City.wav");

    Song magicShop = new Song("BTS", " Magic Shop ", "D:/Kylie/Bangtan/Music/Magic Shop.wav");
    Song polarNight = new Song("BTS", " PolarNight ", "D:/Kylie/Bangtan/Music/Polar Night.wav");
    Song shadow = new Song("BTS", " shadow ", "D:/Kylie/Bangtan/Music/Shadow.wav");
    Song  silverSpoon = new Song("BTS", " Silver Spoon ", "D:/Kylie/Bangtan/Music/Silver Spoon.wav");
    Song soFarAway = new Song("BTS", " so far away ", "D:/Kylie/Bangtan/Music/So Far Away.wav");







    public MainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {



        songs.add(cypher4);
        songs.add(zero);
        songs.add(arson);
        songs.add(amygdala);
        songs.add(twentyOne);
        songs.add(airplane);
        songs.add(dimple);
        songs.add(dontLeaveMe);
        songs.add(filmOut);
        songs.add(fire);
        songs.add(friends);
        songs.add(maCity);
        songs.add(magicShop);
        songs.add(polarNight);
        songs.add(shadow);
        songs.add(silverSpoon);
        songs.add(soFarAway);

        musicManager = new MusicManager(new Playlist(songs));

        do {
            runMainUI();
        } while (true);
    }



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

    private void implementCommand(String command)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        if (command.equals("q")) {
            System.exit(0);
        } else if (command.equals("u")) {
            new UploadSongUI(musicManager);
        } else if (command.equals("v")) {
            new PlaylistUI(musicManager);
        } else if (command.equals("m")) {
            new ManualBeatsUI(musicManager);
        } else {
            System.out.println("Sorry " + command + " was not a valid input, please select from the menu.");
        }
    }

}