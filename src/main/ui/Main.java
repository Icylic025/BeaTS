package ui;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Start the program in this Main class
 */
public class Main {

    public static void main(String[] args) {
        try {
            MainUI mainUI = new MainUI();
            mainUI.setVisible(true);
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

}
