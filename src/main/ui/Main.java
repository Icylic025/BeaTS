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
        Song whistle = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
        Song whistle2 = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
        Song cypher4 = new Song("BTS", "Cypher 4", "D:/Kylie/Bangtan/Music/Cypher 4.wav");

        ArrayList<Song> songs = new ArrayList<Song>();
        songs.add(whistle);
        songs.add(whistle2);
        songs.add(cypher4);

        Playlist playlist = new Playlist(songs);

        try {
            playlist.playAll();

        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("COMPLETE CODE");
    }

}
