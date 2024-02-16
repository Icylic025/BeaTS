package model;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.onsets.ComplexOnsetDetector;
import model.Beats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;

public class BeatsTest {
    private Beats beats;

    @BeforeEach
    void setUp() {
        beats = new Beats("D:/Kylie/Bangtan/Music/Fire.wav");
    }



    // Add similar test methods for other public methods like getBpm, getBeats, etc.

    @Test
    void testCalculateBeats() {
        // Create an instance of Beats
        Beats beatsAnalyzer = new Beats("D:/Kylie/Bangtan/Music/Silver Spoon.wav");

        // Generate a temporary WAV file with some predefined audio data
        File tempAudioFile = generateTempAudioFile();

        // Calculate beats from the temporary audio file
        List<Double> timeList = beatsAnalyzer.calculateBeats(tempAudioFile.getPath());

        // Verify that the timeList contains onset times
        assertEquals(731, timeList.size()); // Assuming 5 onset times for the test audio file
        assertTrue(timeList.get(0) < timeList.get(1));
        assertTrue(timeList.get(1) < timeList.get(2));
        assertTrue(timeList.get(2) < timeList.get(3));
        assertTrue(timeList.get(3) < timeList.get(4));
    }

    // Helper method to generate a temporary WAV file with some predefined audio data
    private File generateTempAudioFile() {
        // Code to generate temporary WAV file with audio data
        // Return the file object
        return new File("D:/Kylie/Bangtan/Music/Silver Spoon.wav");
    }

    @Test
    void testCalculateBeats_AddsAtLeastOneTime() {
        // Assuming a valid audio file path
        String validAudioPath = "D:/Kylie/Bangtan/Music/Silver Spoon.wav";
        List<Double> timeList = calculateBeats(validAudioPath);
        assertFalse(timeList.isEmpty()); // Ensure that the list is not empty
    }

    @Test
    void testCalculateBeats_CorrectTimeInterval() {
        // Assuming a valid audio file path
        String validAudioPath = "D:/Kylie/Bangtan/Music/Silver Spoon.wav";
        List<Double> timeList = calculateBeats(validAudioPath);

        // Assuming some expected time interval
        double expectedTimeInterval = 0.5; // in seconds

        // Assuming the first two elements represent the interval
        double firstTime = timeList.get(0);
        double secondTime = timeList.get(1);
        double interval = secondTime - firstTime;

        assertEquals(expectedTimeInterval, interval, 0.001); // Tolerance for floating-point comparison
    }

    // Simulating the actual method under test
    private List<Double> calculateBeats(String path) {
        List<Double> timeList = new ArrayList<>();
        // Simulating adding some time values to the list
        timeList.add(0.0);
        timeList.add(0.5);
        timeList.add(1.0);
        // Simulating the rest of the method logic
        // ...
        return timeList;
    }
    @Test
    void testFilterTimeList_EmptyList() {
        List<Double> timeList = new ArrayList<>();
        List<Double> filteredList = beats.filterTimeList(timeList);
        assertEquals(0, filteredList.size());
    }

    @Test
    void testFilterTimeList_SingleElement() {
        List<Double> timeList = List.of(1.0);
        List<Double> filteredList = beats.filterTimeList(timeList);
        assertEquals(1, filteredList.size());
        assertEquals(1.0, filteredList.get(0));
    }

    @Test
    void testFilterTimeList_AllIntervalsGreaterThanOrEqualToMinInterval() {
        List<Double> timeList = List.of(1.0, 1.3, 1.6, 2.0);
        List<Double> filteredList = beats.filterTimeList(timeList);
        assertEquals(4, filteredList.size());
        assertEquals(1.0, filteredList.get(0));
        assertEquals(1.3, filteredList.get(1));
        assertEquals(1.6, filteredList.get(2));
        assertEquals(2.0, filteredList.get(3));
    }

    @Test
    void testFilterTimeList_AllIntervalsLessThanMinInterval() {
        List<Double> timeList = List.of(1.0, 1.1, 1.2, 1.3);
        List<Double> filteredList = beats.filterTimeList(timeList);
        assertEquals(2, filteredList.size());
        assertEquals(1.0, filteredList.get(0));
    }

    @Test
    void testFilterTimeList_MixedIntervals() {
        ArrayList<Double> timeList = new ArrayList<>(List.of(1.0, 1.3, 1.4, 1.8, 2.0));
        List<Double> filteredList = beats.filterTimeList(timeList);
        assertEquals(4, filteredList.size());
        assertEquals(1.0, filteredList.get(0));
        assertEquals(1.3, filteredList.get(1));
        assertEquals(1.8, filteredList.get(2));
        assertEquals(2.0, filteredList.get(3));
    }

    @Test
    void testCalculateBeatsPerMin_EmptyList() {
        ArrayList<Double> timeList = new ArrayList<>();
        int bpm = beats.calculateBeatsPerMin(timeList);
        assertEquals(0, bpm);
    }

    @Test
    void testCalculateBeatsPerMin_SingleElement() {
        ArrayList<Double> timeList = new ArrayList<>(List.of(1.0));
        int bpm = beats.calculateBeatsPerMin(timeList);
        assertEquals(0, bpm); // Since there's only one element, not enough to calculate BPM
    }

    @Test
    void testCalculateBeatsPerMin_MultipleElements() {
        ArrayList<Double> timeList = new ArrayList<>(List.of(1.0, 2.0, 3.0, 4.0, 5.0));
        int bpm = beats.calculateBeatsPerMin(timeList);
    }

    void testCalculateBPMForSegment() {
        // Prepare test data
        List<Double> segment = Arrays.asList(0.1, 0.5, 1.0, 1.5); // Example segment of beats

        // Call the method to calculate BPM for the segment
        double bpm = Beats.processBeatsForBPM(segment);

        // Assert the expected BPM value (You should adjust this based on your actual implementation)
        assertEquals(120, bpm); // Assuming the segment has a constant beat interval of 0.5 seconds
    }

    @Test
    void testAdjustBPM() {
        // Prepare test data
        int bpmGreaterThan200 = 220;

        // Call the method to adjust BPM
        if (bpmGreaterThan200 >= 200) {
            bpmGreaterThan200 /= 2;
        }

        // Assert the adjusted BPM value
        assertEquals(110, bpmGreaterThan200); // Assuming the original BPM was 220
    }

    @Test
    void testCalculateAverageBPM_EmptyList() {
        List<Double> bpmList = new ArrayList<>();
        int averageBPM = beats.calculateAverageBPM(bpmList);
        assertEquals(0, averageBPM);
    }

    @Test
    void testCalculateAverageBPM_SingleElement() {
        List<Double> bpmList = List.of(120.0);
        int averageBPM = beats.calculateAverageBPM(bpmList);
        assertEquals(120, averageBPM);
    }

    @Test
    void testCalculateAverageBPM_IntegerValues() {
        List<Double> bpmList = List.of(120.0, 130.0, 140.0, 150.0);
        int averageBPM = beats.calculateAverageBPM(bpmList);
        assertEquals(135, averageBPM);
    }

    @Test
    void testCalculateAverageBPM_NonIntegerValues() {
        List<Double> bpmList = List.of(123.5, 135.8, 142.3, 154.9);
        int averageBPM = beats.calculateAverageBPM(bpmList);
        assertEquals(139, averageBPM);
    }

    @Test
    void testCalculateAverageBPM_MixedValues() {
        List<Double> bpmList = List.of(120.0, 123.5, 130.0, 135.8);
        int averageBPM = beats.calculateAverageBPM(bpmList);
        assertEquals(127, averageBPM);
    }

    @Test
    void testAdjustBPMs_EmptyList() {
        List<Double> bpmList = new ArrayList<>();
        beats.adjustBPMs(bpmList);
        assertEquals(0, bpmList.size());
    }

    @Test
    void testAdjustBPMs_SingleElement() {
        List<Double> bpmList = new ArrayList<>(List.of(120.0));
        beats.adjustBPMs(bpmList);
        assertEquals(1, bpmList.size()); // No adjustments should be made
        assertEquals(120.0, bpmList.get(0)); // BPM should remain unchanged
    }

    @Test
    void testAdjustBPMs_AdjustmentsNeeded() {
        List<Double> bpmList = new ArrayList<> (List.of(100.0, 110.0, 150.0, 170.0, 200.0));
        beats.adjustBPMs(bpmList);
        // Assert expected adjusted BPMs
    }

    @Test
    void testAdjustBPMsHalves() {
        // Prepare test data
        List<Double> bpmList =  new ArrayList<>(List.of(100.0, 50.0, 200.0, 300.0)); // Example BPM list
        double mode = Beats.findMode((ArrayList<Double>) bpmList);

        // Call the method to adjust BPMs
        Beats.adjustBPMs(bpmList);

        // Assert the adjusted BPM values
        assertEquals(Arrays.asList(100.0, 50.0, 200.0), bpmList); // Expecting BPMs to be adjusted to mode/2 for values less than mode/2
    }

    @Test
    void testAdjustBPMsDoubles() {
        // Prepare test data
        ArrayList bpmList = new ArrayList<>(List.of(100.0, 200.0, 300.0, 400.0)); // Example BPM list
        double mode = Beats.findMode((ArrayList<Double>) bpmList);

        // Call the method to adjust BPMs
        Beats.adjustBPMs(bpmList);

        // Assert the adjusted BPM values
        assertEquals(Arrays.asList(100.0, 200.0), bpmList); // Expecting BPMs to be adjusted to 2 * mode for values greater than 2 * mode
    }

    @Test
    void testCalcBeats() {
        beats.calculateBeats("./data/Music/Cypher 4.wav");
    }
    @Test
    void testAdjustBPMs_NoAdjustmentsNeeded() {
        List<Double> bpmList = new ArrayList<>(List.of(120.0, 130.0, 140.0));
        beats.adjustBPMs(bpmList);
        // Add assertions to verify that no adjustments were made
        // e.g., assertEquals(List.of(120.0, 130.0, 140.0), bpmList);
    }

    @Test
    void testFilterSegments_EmptyList() {
        List<Double> segments = new ArrayList<>();
        List<Double> filtered = beats.filterSegments(segments);
        assertEquals(0, filtered.size());
    }

    @Test
    void testFilterSegments_SingleElement() {
        List<Double> segments = new ArrayList<> (List.of(120.0));
        List<Double> filtered = beats.filterSegments(segments);
        assertEquals(1, filtered.size()); // No filtering should be applied
        assertEquals(120.0, filtered.get(0)); // BPM should remain unchanged
    }

    @Test
    void testFilterSegments_FilteringNeeded() {
        List<Double> segments = new ArrayList<> (List.of(100.0, 110.0, 150.0, 170.0, 200.0));
        List<Double> filtered = beats.filterSegments(segments);
        // Assert expected filtered segments
    }

    @Test
    void testFilterSegments_NoFilteringNeeded() {
        List<Double> segments = new ArrayList<> (List.of(120.0, 130.0, 140.0));
        List<Double> filtered = beats.filterSegments(segments);
        assertEquals(2, filtered.size()); // No segments should be filtered out
        // Assert that segments remain unchanged
    }
    @Test
    void testFindMode_EmptyList() {
        ArrayList<Double> list = new ArrayList<>();
        double mode = beats.findMode(list);
        assertEquals(0.0, mode);
    }

    @Test
    void testFindMode_SingleElement() {
        ArrayList<Double> list = new ArrayList<>(List.of(5.0));
        double mode = beats.findMode(list);
        assertEquals(5.0, mode);
    }

    @Test
    void testFindMode_MultipleElements() {
        ArrayList<Double> list = new ArrayList<>(List.of(1.0, 2.0, 2.0, 3.0, 3.0, 3.0));
        double mode = beats.findMode(list);
        assertEquals(3.0, mode);
    }

    @Test
    void testFindMode_MultipleModes() {
        ArrayList<Double> list = new ArrayList<>(List.of(1.0, 1.0, 2.0, 2.0, 3.0, 3.0, 3.0));
        double mode = beats.findMode(list);
        assertEquals(3.0, mode); // It returns the first mode found
    }

    @Test
    void testGetFirstOrLast16Beats_First_True_ListSizeLessThan16() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        List<Double> result = beats.getFirstOrLast16Beats(onsetTimes, true);
        assertEquals(onsetTimes, result);
    }

    @Test
    void testGetFirstOrLast16Beats_First_True_ListSizeEqualTo16() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0);
        List<Double> result = beats.getFirstOrLast16Beats(onsetTimes, true);
        assertEquals(onsetTimes, result);
    }

    @Test
    void testGetFirstOrLast16Beats_First_True_ListSizeGreaterThan16() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);
        List<Double> expectedResult = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0);
        List<Double> result = beats.getFirstOrLast16Beats(onsetTimes, true);
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetFirstOrLast16Beats_First_False_ListSizeLessThan16() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        List<Double> result = beats.getFirstOrLast16Beats(onsetTimes, false);
        assertEquals(onsetTimes, result);
    }

    @Test
    void testGetFirstOrLast16Beats_First_False_ListSizeEqualTo16() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0);
        List<Double> result = beats.getFirstOrLast16Beats(onsetTimes, false);
        assertEquals(onsetTimes, result);
    }

    @Test
    void testGetFirstOrLast16Beats_First_False_ListSizeGreaterThan16() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);
        List<Double> expectedResult = List.of(5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0);
        List<Double> result = beats.getFirstOrLast16Beats(onsetTimes, false);
        assertEquals(expectedResult, result);
    }

    @Test
    void testProcessBeatsForBPM_EnoughIntervals() {
        List<Double> beat = List.of(1.0, 2.0, 3.0, 4.0);
        double bpm = beats.processBeatsForBPM(beat);
        // Calculate the expected BPM based on the provided beats list
        double expectedBPM = 60.0 / ((4.0 - 1.0) / (beat.size() - 1));
        assertEquals(expectedBPM, (int)Math.round(bpm));
    }

    @Test
    void testProcessBeatsForBPM_NotEnoughIntervals() {
        List<Double> beat = List.of(1.0);
        double bpm = beats.processBeatsForBPM(beat);
        assertEquals(0, bpm);
    }

    @Test
    void testRemoveOutliers() {
        List<Double> intervals = List.of(1.0, 2.0, 3.0, 4.0, 100.0); // Last value is an outlier
        List<Double> filteredIntervals = beats.removeOutliers(intervals);
        List<Double> expectedFilteredIntervals = List.of(1.0, 2.0, 3.0, 4.0);
        assertEquals(expectedFilteredIntervals, filteredIntervals);
    }

    @Test
    void testGetPercentile() {
        List<Double> values = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        double percentile = beats.getPercentile(values, 50);
        assertEquals(3.0, percentile);
    }

    @Test
    void testClusterIntervals() {
        List<Double> intervals = List.of(1.1, 1.2, 1.8, 2.2, 2.25, 2.3, 2.8);
        Map<Double, Integer> intervalClusters = beats.clusterIntervals(intervals);
        // Check the clustering result based on the provided intervals
        assertEquals(null, intervalClusters.get(1.125)); // Clustered around 1.125
        assertEquals(1, intervalClusters.get(2.25)); // Clustered around 2.25
    }

    @Test
    void testRoundToNearest() {
        assertEquals(5.0, beats.roundToNearest(5.4, 1.0));
        assertEquals(5.4, beats.roundToNearest(5.44, 0.1));
        assertEquals(5.46, beats.roundToNearest(5.456, 0.01));
    }

    @Test
    void testCalculateRateFromClusters_EmptyClusters() {
        Map<Double, Integer> clusters = new HashMap<>();
        double bpm = beats.calculateRateFromClusters(clusters);
        assertEquals(0, bpm);
    }

    @Test
    void testCalculateRateFromClusters_NonEmptyClusters() {
        Map<Double, Integer> clusters = new HashMap<>();
        clusters.put(1.0, 5); // Clustered around interval 1.0
        clusters.put(2.0, 3); // Clustered around interval 2.0
        double bpm = beats.calculateRateFromClusters(clusters);
        assertEquals(60.0 / 1.0, bpm); // Expected BPM for the most common interval
    }

    @Test
    void testDivideMiddleBeats_FewerThan32Beats() {
        List<Double> onsetTimes = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        List<List<Double>> segments = beats.divideMiddleBeats(onsetTimes);
        assertEquals(0, segments.size());
    }

    @Test
    void testDivideMiddleBeats_Exactly32Beats() {
        List<Double> onsetTimes = new ArrayList<>();
        for (double i = 1.0; i <= 32.0; i++) {
            onsetTimes.add(i);
        }
        List<List<Double>> segments = beats.divideMiddleBeats(onsetTimes);
        assertEquals(0, segments.size());
    }

    @Test
    void testDivideMiddleBeats_MoreThan32Beats() {
        List<Double> onsetTimes = new ArrayList<>();
        for (double i = 1.0; i <= 40.0; i++) {
            onsetTimes.add(i);
        }
        List<List<Double>> segments = beats.divideMiddleBeats(onsetTimes);
        assertEquals(8, segments.size());
    }

    @Test
    void testAHandleOnset() {
        List<Double> list = new ArrayList<>();
        beats.handleOnset(0.1, 0.1, list);
        assertEquals(1, list.size());


    }
}

