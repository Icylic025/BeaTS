package ui;

import persistence.JsonReader;
import persistence.JsonWriter;
import ui.threads.Song;
import model.MasterMusicManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The MainUI class represents the main user interface. It runs
 * at the start of the program and is the only thing Main calls.
 * The MainUI then calls upon other UIs depending on the user
 * inputs and directs the flow of the program.
 */

public class MainUI extends JFrame {

    private static final String JSON_STORE = "./data/masterplaylist.json";
    private MasterMusicManager masterMusicManager = new MasterMusicManager();
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;




    Song whistle = new Song("BTS", "Whistle", "./data/Music/Whistle.wav");
    Song cypher4 = new Song("BTS", "Cypher 4", "./data/Music/Cypher 4.wav");
    Song arson = new Song("BTS", "Arson", "./data/Music/Arson.wav");
    Song twentyOne = new Song("BTS", "21st Century Girls", "./data/Music/21st Century Girl.wav");
    Song dontLeaveMe = new Song("BTS", "Don't Leave Me", "./data/Music/Don't Leave Me.wav");

    /**
    * Modifies: masterMusicManager
    * Effects: Constructs a MainUI object, initializes a scanner for user input,
    * and uploads predefined songs to the master music manager.
    */
    public MainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        super("Music Manager");
        masterMusicManager = new MasterMusicManager();
        try {
            initComponents();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("Error initializing UI: " + e.getMessage());
            e.printStackTrace();
        }


        masterMusicManager.uploadSongToMaster(cypher4);
        masterMusicManager.uploadSongToMaster(arson);
        masterMusicManager.uploadSongToMaster(twentyOne);
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


    }


    private void initComponents() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        masterMusicManager.uploadSongToMaster(new Song("BTS", "Whistle", "./data/Music/Whistle.wav"));
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        getContentPane().add(panel);

        addButton(panel, "Upload Song", this::uploadSong);
        addButton(panel, "View Songs", this::viewSongs);
        addButton(panel, "Manual Beat Detection", this::manualBeatDetection);
        addButton(panel, "Save", this::saveMasterPlaylist);
        addButton(panel, "Load", this::loadMasterPlaylist);
    }



    private void addButton(JPanel panel, String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }

    private void uploadSong(ActionEvent e) {
        System.out.println("upload song pressed");
        new UploadSongGUI(masterMusicManager);
    }

    private void viewSongs(ActionEvent e) {

        SwingUtilities.invokeLater(() -> {
            try {
                PlaylistGUI playlistGUI = new PlaylistGUI(masterMusicManager);
                playlistGUI.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }



    private void saveMasterPlaylist(ActionEvent e) {
        try {
            jsonWriter.open();
            jsonWriter.write(masterMusicManager);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Music has been saved to " + JSON_STORE);

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Unable to save to " + JSON_STORE);

        }
    }

    private void loadMasterPlaylist(ActionEvent e) {
        try {
            masterMusicManager = jsonReader.readMaster();
            JOptionPane.showMessageDialog(null, "Music has been loaded from " + JSON_STORE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE);
        }
    }

    private void manualBeatDetection(ActionEvent e) {
        ManualBeatsUI manualBeatsUI = new ManualBeatsUI(masterMusicManager); // Create an instance of ManualBeatsUI
        JFrame frame = new JFrame("Manual Beat Tap");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the ManualBeatsUI window
        frame.getContentPane().add(manualBeatsUI); // Add manualBeatsUI to the frame's content pane
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}

/* *//**
 * Effects: Displays the main menu options, prompts the user for input,
 * and executes the selected command.
 *//*
    private void runMainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String command;

        System.out.println("\nMain Menu:");
        System.out.println("\tPlease select from the following options:");
        System.out.println("\t u -> Upload Song");
        System.out.println("\t v -> View Songs");
        System.out.println("\t m -> Manual Beat Detection");
        System.out.println("\t s -> Save");
        System.out.println("\t l -> Load");
        System.out.println("\t q -> Quit Application\n");

        command = input.next();
        command = command.toLowerCase();

        implementCommand(command);


    }

    *//**
 *  Effects: Executes the command specified by the user,
 *              either uploading a song, viewing songs, performing manual beat detection,
 *              or exiting the application.
 *//*
    private void implementCommand(String command)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        if (command.equals("q")) {
            System.exit(0);
        } else if (command.equals("u")) {
            new UploadSongUI(masterMusicManager);
            loadMasterPlaylist();
        } else if (command.equals("v")) {
            new PlaylistUI(masterMusicManager);
        } else if (command.equals("m")) {
            new ManualBeatsUI(masterMusicManager);
        } else if (command.equals("s")) {
            saveMasterPlaylist();
        } else if (command.equals("l")) {
            loadMasterPlaylist();
        } else {
            System.out.println("Sorry " + command + " was not a valid input, please select from the menu.");
        }
    }*/
