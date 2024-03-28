package ui;

import ui.exceptions.UploadException;
import ui.threads.Song;
import model.MasterMusicManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

/**
 * The UploadSongGUI class provides a graphical user interface (GUI) for uploading songs to the MasterMusicManager.
 * It allows users to input details such as the song title, artist, and select a song file for upload. Upon
 * confirmation, the selected song is uploaded to the master music manager. This class manages the GUI components
 * including text fields, buttons, and status labels to facilitate the upload process. Additionally, it handles
 * user interactions such as selecting a song file, attempting the upload, and displaying status messages. The
 * UploadSongGUI class enhances user experience by providing a user-friendly interface for uploading songs
 * seamlessly within the application.
 */
public class UploadSongGUI extends JFrame {

    private MasterMusicManager masterMusicManager;
    private JTextField titleField;
    private JTextField artistField;
    private JLabel statusLabel;
    private JButton uploadButton;
    private JButton confirmButton;
    private File selectedFile;

    /**
     * Modifies: this (UploadSongGUI object)
     * Effects: Constructs an UploadSongGUI window with a reference to the MasterMusicManager and initializes the
     *          user interface.
     */
    public UploadSongGUI(MasterMusicManager masterMusicManager) {
        this.masterMusicManager = masterMusicManager;
        initializeUI();
    }

    /**
     * Modifies: this (main frame and its components)
     * Effects: Sets up the main frame, initializes fields, and configures buttons and status label.
     */
    private void initializeUI() {
        setupMainFrame();
        initializeFields();
        setupUploadButton();
        setupStatusLabel();
        setupConfirmButton();

    }

    /**
     * Modifies: this (JFrame properties)
     * Effects: Sets up the main frame's properties, such as title, size, close operation, layout, and visibility.
     */
    private void setupMainFrame() {
        setTitle("Upload Song");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));
        setVisible(true);
    }

    /**
     * Modifies: this (adds text fields to the layout)
     * Effects: Initializes and adds text fields for song title and artist to the main frame.
     */
    private void initializeFields() {
        titleField = new JTextField();
        addFormField("Song Title: ", titleField);
        artistField = new JTextField();
        addFormField("Artist: ", artistField);
        setVisible(true);
    }

    /**
     * Modifies: this (adds upload button to the layout)
     * Effects: Sets up the upload button with an action listener for selecting a song file.
     */
    private void setupUploadButton() {
        uploadButton = new JButton("Select Song File");
        uploadButton.addActionListener(e -> selectSongFile());
        add(uploadButton);
        setVisible(true);
    }

    /**
     * Modifies: this (adds status label to the layout)
     * Effects: Sets up the status label to display the current status of file selection.
     */
    private void setupStatusLabel() {
        statusLabel = new JLabel("No file selected");
        add(statusLabel);
        setVisible(true);
    }

    /**
     * Modifies: this (adds confirm button to the layout)
     * Effects: Sets up the confirm button with an action listener to attempt the upload process.
     */
    private void setupConfirmButton() {
        confirmButton = new JButton("Upload Song");
        confirmButton.addActionListener(e -> attemptUpload());
        add(confirmButton);
        setVisible(true);
    }

    /**
     * Modifies: this (selectedFile and statusLabel)
     * Effects: Opens a file chooser dialog, allows the user to select a song file,
     *          and updates the status label with the file name.
     */
    private void selectSongFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("File Selected: " + selectedFile.getName());
        }
        setVisible(true);
    }

    /**
     * Modifies: masterMusicManager (adds a new song), this (closes the window)
     * Effects: Attempts to upload the selected song file to the master
     *          music manager and either closes the window upon success or displays an error message.
     */
    private void attemptUpload() {
        try {
            uploadSong();
            // After successful upload, hide or dispose this upload window
            this.dispose(); // This will close the current window
            // Now, initialize and show the PlaylistGUI
            EventQueue.invokeLater(() -> {
                try {
                    PlaylistGUI playlistGUI = new PlaylistGUI(masterMusicManager);
                    playlistGUI.setVisible(true); // Make the playlist UI visible
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (UploadException uploadException) {
            JOptionPane.showMessageDialog(null, "Upload failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Modifies: this (layout by adding form fields)
     * Effects: Adds a labeled text field to the main frame for user input.
     */
    private void addFormField(String label, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jlabel = new JLabel(label);
        panel.add(jlabel);
        panel.add(textField);
        textField.setColumns(20);
        add(panel);
        setVisible(true);
    }

    /**
     * Requires: selectedFile must exist and be accessible
     * Modifies: masterMusicManager (adds a new song)
     * Effects: Validates and uploads a new song to the master music manager,
     *          displaying a success message or throwing an UploadException on failure.
     */
    private void uploadSong() throws UploadException {
        String title = titleField.getText();
        String artist = artistField.getText();
        if (selectedFile != null && Files.exists(selectedFile.toPath())) {
            String filepath = "./data/Music/" + title + ".wav";
            Song song = new Song(artist, title, filepath);
            masterMusicManager.uploadSongToMaster(song);
            JOptionPane.showMessageDialog(null, title + " by " + artist + " has been successfully uploaded");
        } else {
            throw new UploadException();
        }
    }


}

/*
package ui;

import ui.exceptions.UploadException;
import ui.threads.Song;
import model.MasterMusicManager;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

*/
/**
 * The UploadSongUI class manages the user interface for uploading songs to the MasterMusicManager.
 * It prompts the user to enter details such as the title, artist, and filepath of the song,
 * creates a Song object with the provided details, and uploads it to the MasterMusicManager.
 *//*


public class UploadSongUI extends UploadException {

    */
/**
     * Requires: masterMusicManager to be initialized with a valid instance of MasterMusicManager.
     * Modifies: masterMusicManager
     * Effects: Constructs an UploadSongUI object, prompts the user to input the details of a song,
     *          creates a Song object with the provided details, and uploads it to the master music manager.
     *//*

    public UploadSongUI(MasterMusicManager masterMusicManager) {
        try {
            Song selectedSong = selectSongFromUser();
            masterMusicManager.uploadSongToMaster(selectedSong);
        } catch (UploadException e) {
            System.out.println("Something went wrong with upload");
        }

    }


    */
/**
     * Effects: Prompts the user to enter the title, artist, and filepath of the song to be uploaded,
     *          creates a Song object with the provided details, and returns it.
     *//*

    private Song selectSongFromUser() throws UploadException {
        Scanner scanner = new Scanner(System.in);
        String title;
        String artist;
        String filepath;
        Song song = null;

        System.out.println("Please enter the title of the song to upload: ");
        title = scanner.nextLine();

        System.out.println("Please enter the artist: ");
        artist = scanner.nextLine();


        System.out.println("Please go upload the song as a WAV file "
                + "titled the name of the song to /data/Music of this project: ");
        System.out.println("Press Enter when file is uploaded");
        scanner.nextLine();
        filepath = "./data/Music/" + title + ".wav";



        if (Files.exists(Paths.get(filepath))) {
            song = new Song(artist, title, filepath);
            System.out.println(title + " by " + artist + " has been successfully uploaded");
            return song;
        } else {
            throw new UploadException();
        }

    }

}
*/
