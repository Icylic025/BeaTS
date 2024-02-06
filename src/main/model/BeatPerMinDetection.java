package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeatPerMinDetection {

    public static void main(String[] args) {
        int finalBPM = 0;
        ArrayList<Double> onsetTimes = new ArrayList<>(); // Populate this with your onset times
        BeatDetection beats = new BeatDetection();
        beats.calculateTime();
        // System.out.println(beatTime);
        beats.filterTimeList();
        onsetTimes = (ArrayList<Double>) beats.getTimeList();

        List<List<Double>> dividedSegments = divideMiddleBeats(onsetTimes);

        // Calculate BPM for different sections of the song
        double first16BPM = processBeatsForBPM(getFirstOrLast16Beats(onsetTimes, true));
        double last16BPM = processBeatsForBPM(getFirstOrLast16Beats(onsetTimes, false));
        double middleBPM = processBeatsForBPM(getMiddleBeats(onsetTimes));

        List bpmSegments = new ArrayList<Double>();

        // System.out.println("First 16 Beats BPM: " + first16BPM);
        // System.out.println("Last 16 Beats BPM: " + last16BPM);
        // System.out.println("Middle Section BPM: " + middleBPM);

        for (int i = 0; i < dividedSegments.size(); i++) {
            double bpm = processBeatsForBPM(dividedSegments.get(i));
            System.out.println("Segment " + (i + 1) + " BPM: " + bpm);
            bpmSegments.add(bpm);
        }

        bpmSegments.add(first16BPM);
        bpmSegments.add(last16BPM);
        bpmSegments.add(middleBPM);
        //  System.out.println(bpmSegments);

        finalBPM = proccessFinalBPM((ArrayList<Double>) bpmSegments);
        System.out.println("THE FINAL BPM CALCULATED IS: " + finalBPM);
    }

    @SuppressWarnings("methodlength")
    private static int proccessFinalBPM(ArrayList<Double> list) {
        int mode =  (int) Math.round(findMode(list));
        int[] numBpm = commonBpm(list);
        int rangeBelow90 = numBpm[0];
        int range90To120 = numBpm[1];
        int rangeGreater120 = numBpm[2];

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

    public static int[] commonBpm(ArrayList<Double> list) {
        int rangeBelow90 = 0;
        int range90To120 = 0;
        int rangeGreater120 = 0;

        int [] arr = new int[3];
        for (Object i : list) {
            if ((double) i <= 90) {
                rangeBelow90++;
            } else if ((double) i <= 120 && (double) i >= 90) {
                range90To120++;
            } else if ((double) i >= 120) {
                rangeGreater120++;
            }

        }
        arr[0] = rangeBelow90;
        arr[1] = range90To120;
        arr[2] = rangeGreater120;

        return arr;
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

        return calculateClustersBpm(intervalClusters);
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

    private static double calculateClustersBpm(Map<Double, Integer> clusters) {
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

            // Add 1 to the segment size for the first 'remainder' segments
            int segmentSize = baseSize + (i < remainder ? 1 : 0);
            List<Double> segment = new ArrayList<>(middleBeats.subList(start, start + segmentSize));
            segments.add(segment);
            start += segmentSize;
        }

        return segments;
    }
    // add this line to test
}
