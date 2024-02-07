package model;

import java.util.ArrayList;
import java.util.List;

public class ManualBpmCalc {

    private int bpm;

    public ManualBpmCalc(List<Double> timeList) {
        bpm = calcManualBpm(timeList);
    }

    private int calcManualBpm(List<Double> timeList) {
        if (timeList.size() < 2) {
            return 0; // Need at least two times to calculate distance
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
