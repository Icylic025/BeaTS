package model;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Beats class represents a utility for analyzing beats in audio files.
 * using the TarsosDSP library.The major role of this class is to
 * calculate the beats per minute (BPM) of .wav music files.
 * It utilizes the TarsosDSP library (Library GitHub:
 * https://github.com/JorenSix/TarsosDSP) to detect onsets in audio signals,
 * calculates the beats per minute (BPM), and provides various methods
 * for processing beat data.
 *
 * This class uses these functions to calculate the bpm to:
 * - Detect onset times in a audio file
 * - Calculate BPM from the onset times
 * - Filter beat intervals and onset times that are too close
 * - Adjust BPM values based on mode, doubles, halves, and multiples
 * - Cluster intervals and calculate BPM rate
 * - Divide the onset times into sections and through intervals, removing
 *   outliers, and calculating percentiles.
 */
public class Beats {
    private List<Double> beats = new ArrayList<Double>();
    private int bpm;

    /**
     * Requires: Valid audio file path
     * Modifies: this
     * Effects: Constructs a Beats object and calculates the beats per minute (BPM) from the given audio file.
     */
    public Beats(String path) {
        beats = filterTimeList(calculateBeats(path));
        bpm = calculateBeatsPerMin((ArrayList<Double>) beats);
    }

    public int getBpm() {
        return bpm;
    }

    public List<Double> getBeats() {
        return beats;
    }

    /**
     * Requires: Valid audio file path
     * Effects: Calculates the beats from the given audio file and returns the list of beat times.
     */
    private List<Double> calculateBeats(String path) {
        List<Double> timeList = new ArrayList<>();
        try {
            String audioFilePath = path; // Replace with your audio file path
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
                onsetDetector.setHandler((time, salience) -> handleOnset(time, salience, timeList));
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
        // System.out.println(timeList);
        return timeList; // Ensure this is the correctly populated list
    }

    /**
     * Modifies: timeList
     * Effects: Handles detected onset and adds it to the timeList.
     */
    private void handleOnset(double time, double salience, List<Double> timeList) {
        synchronized (timeList) {
            timeList.add(time);
        }
    }

    /**
     * Effects: Filters the timeList to remove closely occurring beats.
     */
    private List<Double> filterTimeList(List<Double> timeList) {
        double prev = -1; // Initialize to an invalid value
        double minInterval = 0.15; // Minimum interval between beats
        List<Double> filteredList = new ArrayList<>();

        for (Double time : timeList) {
            if (prev < 0 || time - prev >= minInterval) {
                filteredList.add(time);
                prev = time;
            }
        }

        return filteredList;
    }

    /**
     * Requires: Non-empty list of beat times
     * Effects: Calculates the BPM from the given list of beats.
     */
    private int calculateBeatsPerMin(ArrayList<Double> timeList) {
        int finalBPM = 0;

        // Divide the middle beats into 8 segments
        List<List<Double>> dividedSegments = divideMiddleBeats(timeList);

        List<Double> segmentBPMs = new ArrayList<>();

        // Calculate BPM for each segment
        for (List<Double> segment : dividedSegments) {
            double bpm = processBeatsForBPM(segment);
            segmentBPMs.add(bpm);
        }

        // Adjust BPMs for doubles, halves, and multiples
        adjustBPMs(segmentBPMs);

        // Filter out segments that deviate significantly from the expected tempo
        List<Double> filteredBPMs = filterSegments(segmentBPMs);

        // Calculate final BPM as the average of filtered segment BPMs
        finalBPM = calculateAverageBPM(filteredBPMs);

        // Optionally, you can apply further post-processing or adjustments to finalBPM here

        if (finalBPM >= 200) {
            finalBPM /= 2;
        }
        return finalBPM;
    }

    /**
     * Requires: Non-empty list of BPM values
     * Effects: Calculates the average BPM from the given list of BPMs.
     */
    private int calculateAverageBPM(List<Double> bpmList) {
        if (bpmList.isEmpty()) {
            return 0; // Return 0 if the list is empty
        }

        double sum = 0;
        for (double bpm : bpmList) {
            sum += bpm;
        }
        return (int) Math.round(sum / bpmList.size());
    }

    /**
     * Effects: Adjusts the BPM values based on mode, doubles, halves, and multiples.
     */
    private void adjustBPMs(List<Double> bpmList) {
        double mode = findMode((ArrayList<Double>) bpmList);
        for (int i = 0; i < bpmList.size(); i++) {
            double bpm = bpmList.get(i);
            if (bpm == mode) {
                continue; // Skip mode, no adjustment needed
            } else if (Math.abs(bpm - 2 * mode) < 20) {
                bpmList.set(i, 2 * mode); // Adjust doubles
            } else if (Math.abs(bpm - mode / 2) < 20) {
                bpmList.set(i, mode / 2); // Adjust halves
            } else if (bpm < mode / 2 || bpm > 2 * mode) {
                bpmList.remove(i); // Remove completely off segments
                i--; // Adjust index after removal
            }
        }
    }

    /**
     * Effects: Filters the segments that deviate significantly from the expected tempo.
     */
    private List<Double> filterSegments(List<Double> segments) {
        List<Double> filteredBPMs = new ArrayList<>();
        double mode = findMode((ArrayList<Double>) segments);
        double threshold = 20; // Adjust as needed based on expected deviation

        for (double bpm : segments) {
            // Exclude segments that deviate significantly from the mode BPM
            if (Math.abs(bpm - mode) < threshold) {
                filteredBPMs.add(bpm);
            }
        }

        return filteredBPMs;
    }

    /**
     * Effects: Finds the mode value from the given list.
     */
    public static double findMode(ArrayList<Double> list) {
        if (list.isEmpty()) {
            return 0; // Return null if the list is empty
        }

        double mode = list.get(0);
        int maxFrequency = 1; // Starting with the first element

        for (int i = 0; i < list.size(); i++) {
            double currentValue = list.get(i);
            int currentFrequency = 0;
            for (Double value : list) {
                if (value.equals(currentValue)) {
                    currentFrequency++;
                }
            }
            if (currentFrequency > maxFrequency) {
                maxFrequency = currentFrequency;
                mode = currentValue;
            }
        }

        return mode;
    }


    /**
     * Requires: Non-empty list of onset times
     * Effects: Retrieves the first or last 16 beats based on the boolean parameter.
     */
    private static List<Double> getFirstOrLast16Beats(List<Double> onsetTimes, boolean first) {
        if (onsetTimes.size() < 16) {
            return onsetTimes; // Not enough data to differentiate between first and last
        }
        return first ? onsetTimes.subList(0, 16) : onsetTimes.subList(onsetTimes.size() - 16, onsetTimes.size());
    }

    /**
     * Effects: Retrieves the middle beats (not first or last 16 beats) from the onset times.
     */
    private static List<Double> getMiddleBeats(List<Double> onsetTimes) {
        List<Double> middle = new ArrayList<Double>();

        for (int i = 16; i < onsetTimes.size() - 16; i++) {
            middle.add(onsetTimes.get(i));
        }
        return middle;
    }

    /**
     * Requires: Non-empty list of beats
     * Effects: Processes the beats to calculate the BPM.
     */
    private static double processBeatsForBPM(List<Double> beats) {
        if (beats.size() < 2) {
            return 0; // Not enough beats to calculate BPM
        }

        List<Double> intervals = new ArrayList<>();
        for (int i = 1; i < beats.size(); i++) {
            intervals.add(beats.get(i) - beats.get(i - 1));
        }

        List<Double> filteredIntervals = removeOutliers(intervals);
        Map<Double, Integer> intervalClusters = clusterIntervals(filteredIntervals);

        return calculateRateFromClusters(intervalClusters);
    }

    /**
     * Effects: Removes outliers from the list of intervals.
     */
    private static List<Double> removeOutliers(List<Double> intervals) {
        double q1 = getPercentile(intervals, 25);
        double q3 = getPercentile(intervals, 75);
        double iqr = q3 - q1;
        double lowerBound = q1 - 1.5 * iqr; // change for accuracy, play around
        double upperBound = q3 + 1.5 * iqr;


        List<Double> filteredIntervals = new ArrayList<>();
        for (Double interval : intervals) {
            if (interval >= lowerBound && interval <= upperBound) {
                filteredIntervals.add(interval);
            }
        }
        return filteredIntervals;
    }

    /**
     * Requires: Non-empty list of onset times
     * Effects: Processes the middle beats to calculate the BPM.
     */
    private static double processMiddleBeatsForBPM(List<Double> onsetTimes) {
        if (onsetTimes.size() <= 32) { // Not enough beats to differentiate sections
            return 0;
        }

        // Exclude the first and last 16 beats
        List<Double> middleBeats = onsetTimes.subList(16, onsetTimes.size() - 16);

        return processBeatsForBPM(middleBeats);
    }

    /**
     * Effects: Retrieves the percentile value from the list of values.
     */
    private static double getPercentile(List<Double> values, double percentile) {
        List<Double> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size());
        return sortedValues.get(index - 1);
    }

    /**
     * Effects: Clusters the intervals and returns a map of interval clusters.
     */
    private static Map<Double, Integer> clusterIntervals(List<Double> intervals) {
        Map<Double, Integer> intervalClusters = new HashMap<>();
        for (Double interval : intervals) {
            Double roundedInterval = roundToNearest(interval, 0.0075);
            intervalClusters.put(roundedInterval, intervalClusters.getOrDefault(roundedInterval, 0) + 1);
        }
        return intervalClusters;
    }

    /**
     * Effects: Rounds the given value to the nearest precision.
     */
    private static Double roundToNearest(Double value, Double precision) {
        return Math.round(value / precision) * precision;
    }

    /**
     * Effects: Calculates the BPM rate from the interval clusters.
     */
    private static double calculateRateFromClusters(Map<Double, Integer> clusters) {
        if (clusters.isEmpty()) {
            return 0; // No data to calculate BPM
        }

        // Find the interval with the most occurrences
        double mostCommonInterval = Collections.max(clusters.entrySet(), Map.Entry.comparingByValue()).getKey();
        return 60 / mostCommonInterval; // Convert to BPM
    }

    /**
     * Requires: Non-empty list of onset times
     * Effects: Divides the middle beats into segments.
     */
    public static List<List<Double>> divideMiddleBeats(List<Double> onsetTimes) {
        // Exclude the first 16 and last 16 beats
        int totalBeats = onsetTimes.size();
        if (totalBeats <= 32) {
            return new ArrayList<>(); // Return empty list if not enough beats
        }

        List<Double> middleBeats = onsetTimes.subList(16, totalBeats - 16);
        int middleBeatsCount = middleBeats.size();

        // Determine the size of each segment
        int baseSize = middleBeatsCount / 8;
        int remainder = middleBeatsCount % 8;

        List<List<Double>> segments = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < 8; i++) {
            int segmentSize = baseSize + (i < remainder ? 1 : 0); // Add 1 to the segment size for the first 'remainder'
            List<Double> segment = new ArrayList<>(middleBeats.subList(start, start + segmentSize));
            segments.add(segment);
            start += segmentSize;
        }

        return segments;
    }

}
