package model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class MusicPlayer {
    private Clip clip;
    private Song song;

    public MusicPlayer(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.song = song;
        String filePath = song.getFilePath();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        // Register a LineListener to be notified when the clip stops playing
        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    System.out.println("Playback stopped");
                    // Notify the song that playback is finished
                    song.notifyPlaybackFinished();
                }
            }
        });
    }

    public void play() {
        clip.start();
        // Add this line to release the lock immediately after starting playback
        synchronized (clip) {
            try {
                clip.wait(clip.getMicrosecondLength() / 1000); // wait for the duration of the clip
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}