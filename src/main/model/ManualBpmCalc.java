package model;

import java.util.ArrayList;
import java.util.List;

/**
 * ManualBpmCalc class works together with the ManualBeatsUI to calculate the
 * BPM users taps out.
 *
 * It takes in the timing of user taps given by ManualBeatsUI
 * and carries out the calculations for the BPM (beats per minute).
 * based on a list of time intervals between taps.
 *
 * The calculation assumes that the user is attempting to tap out steadily and not just
 * randomly tapping.
 */

public class ManualBpmCalc {

    private int bpm;

    /**
     * Requires: Non-null and non-empty list of time intervals between taps
     * Modifies: this
     * Effects: Constructs a ManualBpmCalc object and calculates the BPM (beats per minute)
     *          based on the provided list of time intervals between taps.
     */
    public ManualBpmCalc(List<Double> timeList) {
        bpm = calcManualBpm(timeList);
    }

    /**
     * Requires: Non-null and non-empty list of time intervals between taps
     * Modifies: None
     * Effects: Calculates the BPM (beats per minute) based on the provided list of time intervals between taps.
     *          Returns 0 if less than two times are provided.
     */
    private int calcManualBpm(List<Double> timeList) {
        if (timeList.size() < 2) {
            return 0; // Need at least two taps/times
        }

        ArrayList<Double> distances = new ArrayList<>();

        // Calculate distances between consecutive taps
        for (int i = 2; i < timeList.size(); i++) {
            double distance = timeList.get(i) - timeList.get(i - 1);
            distances.add(distance);
        }

        // Calculate average distance
        double sum = 0;
        for (Double distance : distances) {
            sum += distance;
        }
        double averageDistance = sum / distances.size();

        // Convert to BPM
        return (int) Math.round(60000 / averageDistance);

    }

    public int getBpm() {
        return bpm;
    }
}
