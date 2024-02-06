package model;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BeatDetection {
    double startTime = System.currentTimeMillis();

    private List<Double> timeList = new ArrayList<>();

    public double getStartTime() {
        return startTime;
    }
    public void calculateTime() {
        try {
            String audioFilePath = "D:/Kylie/Bangtan/Music/So Far Away.wav"; // Replace with your audio file path
            File audioFile = new File(audioFilePath);

            int bufferSize = 2048;
            int overlap = 1024;

            AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(audioFile, bufferSize, overlap);

            double threshold = 0.25;
            ComplexOnsetDetector onsetDetector = new ComplexOnsetDetector(bufferSize, threshold);

            // Create an ExecutorService with a single worker thread
            ExecutorService executor = Executors.newSingleThreadExecutor();

            // Submit the audio processing task to the executor
            Future<?> future = executor.submit(() -> {
                // Set the handler for detected onsets
                onsetDetector.setHandler(this::handleOnset);
                dispatcher.addAudioProcessor(onsetDetector);
                dispatcher.run(); // Start processing the audio
            });

            // Wait for the audio processing task to complete
            future.get();

            // Shutdown the executor
            executor.shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleOnset(double time, double salience) {
        timeList.add(time);
    }

    public List<Double> getTimeList() {
        // System.out.println(timeList);
        return timeList;
    }

    public void filterTimeList() {
        double prev = 0;
        double minInterval = 0.15; // Adjusted from 0.2 to 0.15 for less aggressive filtering
        ArrayList<Double> newList = new ArrayList<>();

        for (Double time : timeList) {
            if (time - prev >= minInterval) {
                newList.add(time);
                prev = time; // Update prev to the current time
            }
        }
        timeList = newList; // Replace the original list
    }

}
