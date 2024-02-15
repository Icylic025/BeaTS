package model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManualBpmCalcTest {

    @Test
    void testCalcManualBpmWithEmptyList() {
        List<Double> timeList = new ArrayList<>();
        ManualBpmCalc bpmCalc = new ManualBpmCalc(timeList);
        assertEquals(0, bpmCalc.getBpm());
    }

    @Test
    void testCalcManualBpmWithSingleEntry() {
        List<Double> timeList = Arrays.asList(0.0);
        ManualBpmCalc bpmCalc = new ManualBpmCalc(timeList);
        assertEquals(0, bpmCalc.getBpm());
    }

    @Test
    void testCalcManualBpmWithTwoEntries() {
        List<Double> timeList = Arrays.asList(0.0, 1000.0);
        ManualBpmCalc bpmCalc = new ManualBpmCalc(timeList);
        assertEquals(0, bpmCalc.getBpm());
    }

    @Test
    void testCalcManualBpmWithMultipleEntries() {
        List<Double> timeList = Arrays.asList(0.0, 1000.0, 2000.0, 3000.0, 4000.0, 5000.0, 6000.0, 7000.0, 8000.0, 9000.0);
        ManualBpmCalc bpmCalc = new ManualBpmCalc(timeList);
        assertEquals(60, bpmCalc.getBpm());
    }
}
