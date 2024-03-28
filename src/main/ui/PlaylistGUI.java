package ui;

import model.MasterMusicManager;
import model.LocalMusicManager;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.threads.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PlaylistGUI extends JFrame {
    private static final String JSON_STORE = "./data/localplaylist.json";
    private static final String JSON_STORE_M = "./data/masterplaylist.json";

    private MasterMusicManager masterMusicManager;
    private LocalMusicManager localMusicManager;
    private JList<Song> songList;
    private DefaultListModel<Song> songListModel;
    JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    JsonReader jsonReader = new JsonReader(JSON_STORE);
    JsonWriter jsonWriterMaster = new JsonWriter(JSON_STORE_M);
    JsonReader jsonReaderMaster = new JsonReader(JSON_STORE_M);

    public PlaylistGUI(MasterMusicManager masterMusicManager) {
        this.masterMusicManager = masterMusicManager;
        this.localMusicManager = new LocalMusicManager(masterMusicManager.getMasterPlaylist());
        initializeUI("Master Playlist (Here are all your uploaded music:)");
    }

    public PlaylistGUI(LocalMusicManager localMusicManager, MasterMusicManager masterMusicManager) {
        System.out.println("playlist ui");
        this.masterMusicManager = masterMusicManager;
        this.localMusicManager = localMusicManager;
        // updateSongList();
        initializeUI("Current Playlist (You can manipulate your playlist here:)");
        System.out.println("init");
    }

    private void initializeUI(String title) {
        setTitle("Playlist");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        songListModel = new DefaultListModel<>();
        songList = new JList<>(songListModel);
        updateSongList();


        songList.addListSelectionListener(new SongSelectionListener());


        add(new JScrollPane(songList), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center on screen
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton addButton = new JButton("Add Song");
        addButton.addActionListener(this::addSongAction);
        panel.add(addButton);

        JButton deleteButton = new JButton("Delete Song");
        deleteButton.addActionListener(this::deleteSongAction);
        panel.add(deleteButton);

        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(this::shuffleAction);
        panel.add(shuffleButton);

        JButton playAllButton = new JButton("Play All");
        playAllButton.addActionListener(this::playAllAction);
        panel.add(playAllButton);

        JButton filterButton = new JButton("Filter by BPM");
        filterButton.addActionListener(this::filterAction);
        panel.add(filterButton);

        JButton saveButton = new JButton("Save Playlist");
        saveButton.addActionListener(this::saveAction);
        panel.add(saveButton);

        JButton loadButton = new JButton("Load Playlist");
        loadButton.addActionListener(this::loadAction);
        panel.add(loadButton);




        // Add more buttons and actions as needed

        return panel;
    }

    private void addSongAction(ActionEvent event) {
        JFrame masterPlaylistFrame = createMasterPlaylistFrame();
        masterPlaylistFrame.setVisible(true);
    }

    private JFrame createMasterPlaylistFrame() {
        JFrame masterPlaylistFrame = new JFrame("Master Playlist");
        masterPlaylistFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        masterPlaylistFrame.setSize(400, 300);

        DefaultListModel<Song> masterSongListModel = new DefaultListModel<>();
        JList<Song> masterSongList = new JList<>(masterSongListModel);
        JScrollPane scrollPane = new JScrollPane(masterSongList);

        List<Song> masterSongs = masterMusicManager.getMasterPlaylist().getSongs();
        for (Song song : masterSongs) {
            masterSongListModel.addElement(song);
        }

        masterSongList.addListSelectionListener(new MasterSongListSelectionListener(masterSongListModel,
                masterPlaylistFrame));

        masterPlaylistFrame.add(scrollPane);
        return masterPlaylistFrame;
    }

    private class MasterSongListSelectionListener implements ListSelectionListener {
        private DefaultListModel<Song> masterSongListModel;
        private JFrame masterPlaylistFrame;

        public MasterSongListSelectionListener(DefaultListModel<Song> masterSongListModel, JFrame masterPlaylistFrame) {
            this.masterSongListModel = masterSongListModel;
            this.masterPlaylistFrame = masterPlaylistFrame;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList<Song> masterSongList = (JList<Song>) e.getSource();
            int selectedIndex = masterSongList.getSelectedIndex();
            if (selectedIndex != -1) {
                Song selectedSong = masterSongListModel.getElementAt(selectedIndex);
                try {
                    localMusicManager.getPlaylist().addSong(selectedSong);
                    JOptionPane.showMessageDialog(PlaylistGUI.this,
                            selectedSong.getTitle() + " has been added to your local playlist");
                    updateSongList();
                    masterPlaylistFrame.dispose(); // Close the master playlist frame
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PlaylistGUI.this, "Failed to add song to local playlist");
                }
            }
        }
    }

    private void deleteSongAction(ActionEvent event) {
        JFrame deleteSongFrame = createDeleteSongFrame();
        deleteSongFrame.setVisible(true);
    }

    private JFrame createDeleteSongFrame() {
        JFrame deleteSongFrame = new JFrame("Delete Song");
        deleteSongFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteSongFrame.setSize(300, 400);

        JPanel panel = new JPanel(new BorderLayout());
        JList<Song> songList = createSongList();
        JButton deleteButton = createDeleteButton(deleteSongFrame, songList);

        panel.add(new JScrollPane(songList), BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.SOUTH);
        deleteSongFrame.add(panel);
        return deleteSongFrame;
    }

    private JList<Song> createSongList() {
        DefaultListModel<Song> songListModel = new DefaultListModel<>();
        JList<Song> songList = new JList<>(songListModel);
        List<Song> songs = localMusicManager.getPlaylist().getSongs();
        for (Song song : songs) {
            songListModel.addElement(song);
        }
        return songList;
    }

    private JButton createDeleteButton(JFrame deleteSongFrame, JList<Song> songList) {
        JButton deleteButton = new JButton("Delete Song");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex != -1) {
                    int option = JOptionPane.showConfirmDialog(deleteSongFrame,
                            "Are you sure you want to delete this song?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        localMusicManager.getPlaylist().deleteSong(selectedIndex);
                        updateSongList();
                        deleteSongFrame.dispose(); // Close the delete song frame
                    }
                } else {
                    JOptionPane.showMessageDialog(deleteSongFrame, "Please select a song to delete");
                }
            }
        });
        return deleteButton;
    }

    private void shuffleAction(ActionEvent event) {
        try {
            new PlaylistGUI(localMusicManager.shuffle(), masterMusicManager);
            updateSongList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playAllAction(ActionEvent event) {

        try {
            localMusicManager.getPlaylist().playAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(PlaylistGUI.this,
                    "Something went wrong with the music files");

        }
    }

    private void filterAction(ActionEvent event) {
        int bpm;

        String userString = JOptionPane.showInputDialog(null,"Enter the BPM you want to filter to:",
                "Input", JOptionPane.QUESTION_MESSAGE);

        if (userString == null) {
            return;
        }

        try {
            bpm = Integer.parseInt(userString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(PlaylistGUI.this,
                    userString + " is not a valid BPM");
            return;
        }

        try {
            System.out.println(bpm);
            LocalMusicManager newLoc = localMusicManager.filter(bpm); // Filter the playlist in place
            new PlaylistGUI(newLoc, masterMusicManager);
            updateSongList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAction(ActionEvent event) {
        try {
            // Save the master playlist
            jsonWriterMaster.open();
            jsonWriterMaster.write(masterMusicManager);
            jsonWriterMaster.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(PlaylistGUI.this,"Unable to write masterplaylist to file: " + JSON_STORE);
        }

        // Save the local playlist
        localMusicManager = new LocalMusicManager(localMusicManager.getPlaylist());
        try {
            jsonWriter.open();
            jsonWriter.write(localMusicManager);
            jsonWriter.close();
            JOptionPane.showMessageDialog(PlaylistGUI.this,"Your current playlist has been saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(PlaylistGUI.this,"Unable to write current playlist to file: " + JSON_STORE);
        }
        new PlaylistGUI(localMusicManager, masterMusicManager);

    }

    private void loadAction(ActionEvent event) {
        try {
            masterMusicManager = jsonReader.readMaster();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(PlaylistGUI.this, "Unable to read masterplaylist from file: " + JSON_STORE);
        }

        try {
            localMusicManager = jsonReader.readLocal();
            JOptionPane.showMessageDialog(PlaylistGUI.this, "Music has been loaded from " + JSON_STORE);

            // Update the current PlaylistGUI instance with the loaded playlists
            this.masterMusicManager = masterMusicManager;
            this.localMusicManager = localMusicManager;
            updateSongList();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(PlaylistGUI.this, "Unable to read from file: " + JSON_STORE);
        }
    }

    private class SongSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Song selectedSong = songListModel.getElementAt(selectedIndex);
                    // Placeholder for what to do when a song is clicked
                    try {
                        selectedSong.playSong();
                        System.out.println("song played");
                    } catch (UnsupportedAudioFileException ex) {
                        JOptionPane.showMessageDialog(PlaylistGUI.this,
                                "The audio file is unsupported, make sure it is a .wav file");

                    } catch (LineUnavailableException ex) {
                        JOptionPane.showMessageDialog(PlaylistGUI.this,
                                "There is something wrong with the music file.");

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(PlaylistGUI.this,
                                "Something went wrong.");

                    }
                }
            }
        }
    }

    private void updateSongList() {
        songListModel.clear();
        List<Song> songs = localMusicManager.getPlaylist().getSongs();
        for (Song song : songs) {
            songListModel.addElement(song);
        }
    }


}

/*
package ui;

import persistence.JsonReader;
import persistence.JsonWriter;
import ui.threads.Playlist;
import ui.threads.Song;
import model.LocalMusicManager;
import model.MasterMusicManager;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

*/
/**
 * The PlaylistUI class manages the user interface for interacting with playlists. There are two
 * types of PlaylistUIs that is created depending on the constructor, the one with the MasterMusicManager
 * is the masterplaylist that stores all the uploaded songs. The second with the LocalMusicManager where
 * the playlist with customizations (filter, shuffle, add, delete) is displayed but does not affect the
 * masterplaylist. Closing the localplaylist will lose the customizations.
 *
 * It allows users to view, filter, shuffle, add, play, listen to, and delete songs.
 *//*


public class PlaylistUI {
    private static final String JSON_STORE = "./data/localplaylist.json";
    private static final String JSON_STORE_M = "./data/masterplaylist.json";


    Playlist playlist;
    Scanner scanner = new Scanner(System.in);
    LocalMusicManager localMusicManager = new LocalMusicManager(new Playlist());
    MasterMusicManager masterMusicManager;
    JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    JsonReader jsonReader = new JsonReader(JSON_STORE);
    JsonWriter jsonWriterMaster = new JsonWriter(JSON_STORE_M);
    JsonReader jsonReaderMaster = new JsonReader(JSON_STORE_M);

    */
/**
     * Requires: masterMusicManager to be initialized with a valid instance of MasterMusicManager.
     * Effects: Constructs a more permanent PlaylistUI object with all songs in the MasterPlaylist
     *          that users can get back to and runs the playlist user interface.
     *//*

    public PlaylistUI(MasterMusicManager masterMusicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.masterMusicManager = masterMusicManager;
        playlist = masterMusicManager.getMasterPlaylist();


        runPlaylistUI();
    }

    */
/**
     * Requires: localMusicManager and masterMusicManager to be initialized with valid instances.
     * Effects: Constructs a PlaylistUI object with a specified local music manager,
     *          initializes a playlist, and runs the playlist user interface. This Playlist is not
     *          as permanent with user's current customizations
     *//*

    public PlaylistUI(LocalMusicManager localMusicManager,MasterMusicManager masterMusicManager)
            throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playlist = localMusicManager.getPlaylist();
        this.masterMusicManager = masterMusicManager;
        runPlaylistUI();
    }


    */
/**
     * Effects: Starts the PlaylistUI, calls method to takes command and
     *          pass it on to be implemented.
     *//*

    private void runPlaylistUI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        String command = "";
        displaySongs();
        command = takeCommand();
        implementCommand(command);
    }

    */
/**
     * Effects: Displays a list of songs from the playlist.
     *//*

    private void displaySongs() {
        int counter = 1;
        List<Song> songs = playlist.getSongs();
        System.out.println("\nHere is a list of all your currently stored songs: ");
        for (Song s : songs) {
            System.out.println("\t\t" + counter + ".  " + s.getTitle());
            counter++;
        }
    }

    */
/**
     * Effects: Prompts the user for a command and returns it.
     *//*

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
            System.out.println("\tv -> Save current playlist");
            System.out.println("\to -> Load saved playlist");
            System.out.println("\tb -> Back to Main Menu");
            System.out.println("\tq -> Quit Application");

            command = scanner.next();
            command = command.toLowerCase();
            if (command.equals("l") || command.equals("f") || command.equals("s") || command.equals("a")
                    || command.equals("d") || command.equals("p") || command.equals("b") || command.equals("q")
                    || command.equals("v") || command.equals("o")) {
                return command;
            }
            System.out.println("Sorry " + command + "is not a valid input, please select from the menu.");

        } while (true);

    }

    */
/**
     * Effects: Executes the command specified by the user by calling
     *          appropriate methods or completing simple commands.
     *//*

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
        } else if (command.equals("v")) {
            saveLocal(masterMusicManager);
        } else if (command.equals("o")) {
            loadLocal();
        } else {
            delete();
        }
    }

    */
/**
     * Effects: save local playlist into Json and also save the master playlist
     *//*

    private void saveLocal(MasterMusicManager masterMusicManager) throws UnsupportedAudioFileException,
            LineUnavailableException, IOException {
        try {
            // Save the master playlist
            jsonWriterMaster.open();
            jsonWriterMaster.write(masterMusicManager);
            jsonWriterMaster.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write masterplaylist to file: " + JSON_STORE);
        }

        // Save the local playlist
        localMusicManager = new LocalMusicManager(playlist);
        try {
            jsonWriter.open();
            jsonWriter.write(localMusicManager);
            jsonWriter.close();
            System.out.println("Your current playlist has been saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write current playlist to file: " + JSON_STORE);
        }
        new PlaylistUI(localMusicManager, masterMusicManager);
    }


    */
/**
     * Effects: load local playlist from Json
     *//*

    private void loadLocal() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        try {
            masterMusicManager = jsonReader.readMaster();
        } catch (IOException e) {
            System.out.println("Unable to read masterplaylist from file: " + JSON_STORE);
        }

        try {
            localMusicManager = jsonReader.readLocal();
            System.out.println("Music has been loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }

        new PlaylistUI(localMusicManager, masterMusicManager);
    }

    */
/**
     * Effects: Filters songs in the playlist by BPM and displays the filtered songs
     *          with a new temporary PlaylistUI.
     *//*

    public void filter() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        int bpm;
        LocalMusicManager filteredManager;
        System.out.println("Please input the BPM you want: ");
        bpm = Integer.parseInt(scanner.next());
        System.out.println("Here are the songs around this BPM: ");
        filteredManager = new LocalMusicManager(playlist.filterByBpm(bpm));
        new PlaylistUI(filteredManager, masterMusicManager);
    }

    */
/**
     * Effects: Shuffles the playlist and displays the shuffled songs
     *          with a new temporary PlaylistUI
     *//*

    public void shuffle() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        LocalMusicManager shuffledManager;
        System.out.println("Here is your playlist shuffled: ");
        shuffledManager = new LocalMusicManager(playlist.shuffle());
        new PlaylistUI(shuffledManager, masterMusicManager);
    }

    */
/**
     * Requires: masterPlaylist and localMusicManager to be initialized with valid instances.
     * Effects: Adds a song from the master playlist to the local playlist. Displays new
     *          local playlist
     *//*

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

    */
/**
     * Effects: Plays all songs in the current playlist.
     *//*

    public void playAll() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        playlist.playAll();
        new PlaylistUI(new LocalMusicManager(playlist), masterMusicManager);

    }


    */
/**
     * Effects: Plays a selected song from the current playlist.
     *//*

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

    */
/**
     * Effects: Deletes a selected song from the current playlist.
     *//*

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

    */
/**
     * Effects: Displays the songs in the master playlist.
     *//*

    private void displayMasterPlaylist(Playlist playlist) {
        System.out.println("Master Playlist:");
        int index = 1;
        for (Song song : playlist.getSongs()) {
            System.out.println(index++ + ". " + song.getTitle());
        }
    }

}
*/
