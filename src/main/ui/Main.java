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
/*
    //Song whistle = new Song("BTS", "Whistle", "D:/Kylie/Bangtan/Music/Whistle.wav");
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






        ArrayList<Song> songs = new ArrayList<Song>();
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

        for (Song s : songs) {
            System.out.println(s.getTitle() + " has a BPM of " + s.getBpm());
        }

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

    }
 */