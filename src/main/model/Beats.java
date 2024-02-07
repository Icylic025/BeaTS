package model;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Beats {
    private List<Double> beats = new ArrayList<Double>();
    private int bpm;

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

    private List<Double> calculateBeats(String path) {
        List<Double> timeList = new ArrayList<>();
        try {
            String audioFilePath = "D:/Kylie/Bangtan/Music/" + path + ".wav"; // Replace with your audio file path
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
        System.out.println(timeList);
        return timeList; // Ensure this is the correctly populated list
    }

    private void handleOnset(double time, double salience, List<Double> timeList) {
        synchronized (timeList) {
            timeList.add(time);
        }
    }

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

    private int calculateBeatsPerMin(ArrayList<Double> timeList) {
        int finalBPM = 0;

        List<List<Double>> dividedSegments = divideMiddleBeats(timeList);

        // Calculate BPM for different sections of the song
        double first16BPM = processBeatsForBPM(getFirstOrLast16Beats(timeList, true));
        double last16BPM = processBeatsForBPM(getFirstOrLast16Beats(timeList, false));
        double middleBPM = processBeatsForBPM(getMiddleBeats(timeList));

        List<Double> rateFragments = new ArrayList<>();

        System.out.println("First 16 Beats BPM: " + first16BPM);
        System.out.println("Last 16 Beats BPM: " + last16BPM);
        System.out.println("Middle Section BPM: " + middleBPM);

        for (int i = 0; i < dividedSegments.size(); i++) {
            double bpm = processBeatsForBPM(dividedSegments.get(i));
            System.out.println("Segment " + (i + 1) + " BPM: " + bpm);
            rateFragments.add(bpm);
        }
        rateFragments.add(first16BPM);
        rateFragments.add(last16BPM);
        rateFragments.add(middleBPM);
        System.out.println(rateFragments);

        finalBPM = proccessFinalBPM((ArrayList<Double>) rateFragments);
        System.out.println("THE FINAL BPM CALCULATED IS: " + finalBPM);

        return finalBPM;
    }


    @SuppressWarnings("methodlength")
    private static int proccessFinalBPM(ArrayList<Double> list) {
        int mode =  (int) Math.round(findMode(list));
        int rangeBelow90 = 0;
        int range90To120 = 0;
        int rangeGreater120 = 0;
        for (Object i : list) {
            if ((double) i <= 90) {
                rangeBelow90++;
            } else if ((double) i <= 120 && (double) i >= 90) {
                range90To120++;
            } else if ((double) i >= 120) {
                rangeGreater120++;
            }

        }

        if (rangeBelow90 > range90To120 && rangeBelow90 > rangeGreater120) {
            while (mode >= 90) {
                mode /= 2;
            }
        } else if (range90To120 >= rangeBelow90 && range90To120 >= rangeGreater120) {
            if (mode >= 120) {
                while (mode >= 120) {
                    mode /= 2;
                }
            } else {
                while (mode <= 120) {
                    mode *= 2;
                }
            }
        } else {
            while (mode <= 120) {
                mode *= 2;
            }

        }


        return mode;
    }

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


    private static List<Double> getFirstOrLast16Beats(List<Double> onsetTimes, boolean first) {
        if (onsetTimes.size() < 16) {
            return onsetTimes; // Not enough data to differentiate between first and last
        }
        return first ? onsetTimes.subList(0, 16) : onsetTimes.subList(onsetTimes.size() - 16, onsetTimes.size());
    }

    private static List<Double> getMiddleBeats(List<Double> onsetTimes) {
        List<Double> middle = new ArrayList<Double>();

        for (int i = 16; i < onsetTimes.size() - 16; i++) {
            middle.add(onsetTimes.get(i));
        }
        return middle;
    }

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

    private static double processMiddleBeatsForBPM(List<Double> onsetTimes) {
        if (onsetTimes.size() <= 32) { // Not enough beats to differentiate sections
            return 0;
        }

        // Exclude the first and last 16 beats
        List<Double> middleBeats = onsetTimes.subList(16, onsetTimes.size() - 16);

        return processBeatsForBPM(middleBeats);
    }

    private static double getPercentile(List<Double> values, double percentile) {
        List<Double> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);
        int index = (int) Math.ceil(percentile / 100.0 * sortedValues.size());
        return sortedValues.get(index - 1);
    }

    private static Map<Double, Integer> clusterIntervals(List<Double> intervals) {
        Map<Double, Integer> intervalClusters = new HashMap<>();
        for (Double interval : intervals) {
            Double roundedInterval = roundToNearest(interval, 0.0075);
            intervalClusters.put(roundedInterval, intervalClusters.getOrDefault(roundedInterval, 0) + 1);
        }
        return intervalClusters;
    }

    private static Double roundToNearest(Double value, Double precision) {
        return Math.round(value / precision) * precision;
    }

    private static double calculateRateFromClusters(Map<Double, Integer> clusters) {
        if (clusters.isEmpty()) {
            return 0; // No data to calculate BPM
        }

        // Find the interval with the most occurrences
        double mostCommonInterval = Collections.max(clusters.entrySet(), Map.Entry.comparingByValue()).getKey();
        return 60 / mostCommonInterval; // Convert to BPM
    }



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
