package threads;

import threads.Song;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * MusicPlayer when given a correct Song object, can play the song associated
 * with the object.
 *
 * Upon playback completion, the MusicPlayer notifies the associated Song object that playback has finished.
 * This guarantees that no code is executed during play back which interferes with playback.
 * Some part of code comes from: https://www.geeksforgeeks.org/play-audio-file-using-java/
 * but adjusted to fit project.
 */

public class MusicPlayer {
    private Clip clip;
    private Song song;

    /**
     * Requires: Valid Song object with non-null file path
     * Modifies: this
     * Effects: Constructs a MusicPlayer object with the provided Song object.
     *          Opens the audio clip from the file path of the Song object and registers
     *          a LineListener to be notified when the clip stops playing.
     */
    public MusicPlayer(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.song = song;
        String filePath = song.getFilePath();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);

        // Register a LineListener to be notified when the clip stops playing
        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent event) {
                if (event.getType() == LineEvent.Type.STOP) {
                    song.notifyPlaybackFinished();
                }
            }
        });
    }

    /**
     * Effects: Starts playback of the audio clip associated with the MusicPlayer object.
     *          Waits for the duration of the clip and releases the lock immediately after starting playback.
     */
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