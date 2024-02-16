package ui;

import model.Beats;
import model.Playlist;
import model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        new MainUI();
    }

}
