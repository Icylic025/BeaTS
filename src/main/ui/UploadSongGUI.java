package ui;

import ui.exceptions.UploadException;
import ui.threads.Song;
import model.MasterMusicManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;

public class UploadSongGUI extends JFrame {

    private MasterMusicManager masterMusicManager;
    private JTextField titleField;
    private JTextField artistField;
    private JLabel statusLabel;
    private JButton uploadButton;
    private JButton confirmButton;
    private File selectedFile;

    public UploadSongGUI(MasterMusicManager masterMusicManager) {
        this.masterMusicManager = masterMusicManager;
        initializeUI();
    }

    private void initializeUI() {
        setupMainFrame();
        initializeFields();
        setupUploadButton();
        setupStatusLabel();
        setupConfirmButton();

    }

    private void setupMainFrame() {
        setTitle("Upload Song");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));
        setVisible(true);
    }

    private void initializeFields() {
        titleField = new JTextField();
        addFormField("Song Title: ", titleField);
        artistField = new JTextField();
        addFormField("Artist: ", artistField);
        setVisible(true);
    }

    private void setupUploadButton() {
        uploadButton = new JButton("Select Song File");
        uploadButton.addActionListener(e -> selectSongFile());
        add(uploadButton);
        setVisible(true);
    }

    private void setupStatusLabel() {
        statusLabel = new JLabel("No file selected");
        add(statusLabel);
        setVisible(true);
    }

    private void setupConfirmButton() {
        confirmButton = new JButton("Upload Song");
        confirmButton.addActionListener(e -> attemptUpload());
        add(confirmButton);
        setVisible(true);
    }

    private void selectSongFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            statusLabel.setText("File Selected: " + selectedFile.getName());
        }
        setVisible(true);
    }

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

    private void addFormField(String label, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout());
        JLabel jlabel = new JLabel(label);
        panel.add(jlabel);
        panel.add(textField);
        textField.setColumns(20);
        add(panel);
        setVisible(true);
    }

    private void uploadSong() throws UploadException {
        String title = titleField.getText();
        String artist = artistField.getText();
        if (selectedFile != null && Files.exists(selectedFile.toPath())) {
            String filepath = "./data/Music/" + title + ".wav";
            // Ideally, you should copy the selected file to the desired location.
            // For simplicity, we're just going to simulate the upload.
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
