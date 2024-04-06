package ui;

import persistence.JsonReader;
import persistence.JsonWriter;
import ui.threads.Song;
import model.MasterMusicManager;
import model.EventLog;
import model.Event;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * The MainUI class represents the main user interface with GUI. It runs
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
     * Modifies: This constructor modifies the masterMusicManager by initializing it and uploading predefined songs.
     * Effects: Constructs a MainUI object, sets up the initial GUI components, and preloads a list of songs into
     *          the master music manager.
     */
    public MainUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        super("Music Manager");
        masterMusicManager = new MasterMusicManager();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printEvents(); // This will now use your custom Event class
            }
        });
        try {
            initComponents();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.err.println("Error initializing UI: " + e.getMessage());
            e.printStackTrace();
        }

        masterMusicManager.uploadSongToMaster(whistle);
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

    private void printEvents() {
        System.out.println("Application is closing. Here are all logged events:");
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
        }
    }

    /**
     * Modifies: masterMusicManager, jsonWriter, jsonReader, and other UI panels.
     * Effects: Initializes components of the MainUI, including buttons for various functionalities and setting up
     *          JSON utilities for saving and loading.
     */
    private void initComponents() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
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


    /**
     * Effects: Adds a button to the specified panel with the given label and action listener.
     */
    private void addButton(JPanel panel, String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        panel.add(button);
    }

    /**
     * Effects: Opens the UploadSongGUI to allow the user to upload a song.
     */
    private void uploadSong(ActionEvent e) {
        new UploadSongGUI(masterMusicManager);
    }

    /**
     * Effects: Launches a new thread to display the PlaylistGUI, allowing the user to view songs.
     */
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


    /**
     * Modifies: The file located at JSON_STORE by writing the current state of masterMusicManager to it.
     * Effects: Saves the current master playlist to a JSON file, showing a message dialog upon success or failure.
     */
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

    /**
     * Modifies: masterMusicManager by loading the state from the file located at JSON_STORE.
     * Effects: Loads the master playlist from a JSON file, showing a message dialog upon success or failure.
     */
    private void loadMasterPlaylist(ActionEvent e) {
        try {
            masterMusicManager = jsonReader.readMaster();
            JOptionPane.showMessageDialog(null, "Music has been loaded from " + JSON_STORE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to read from file: " + JSON_STORE);
        }
    }

    /**
     * Effects: Opens a new JFrame containing the ManualBeatsUI for the user to manually detect beats of a song.
     */
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
