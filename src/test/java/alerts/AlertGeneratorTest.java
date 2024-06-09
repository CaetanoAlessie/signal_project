package alerts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import com.data_management.DataStorage;
import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.Patient;

public class AlertGeneratorTest {
    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGenerator(dataStorage);

        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testEvaluateIncreasingTrendAlert() {
        dataStorage.addPatientData(1, 110, "Systolic pressure", 1627846187000L);
        dataStorage.addPatientData(1, 121, "Systolic pressure", 1627846287000L);
        dataStorage.addPatientData(1, 133, "Systolic pressure", 1627846387000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size(), "Blood pressure trend alert failed");
        assertEquals("Blood pressure trend alert", alerts.get(0).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Blood pressure trend alert for patient ID: 1"));
    }

    @Test
    public void testEvaluateDecreasingTrendAlert() {
        dataStorage.addPatientData(1, 140, "Systolic pressure", 1627846187000L);
        dataStorage.addPatientData(1, 129, "Systolic pressure", 1627846287000L);
        dataStorage.addPatientData(1, 117, "Systolic pressure", 1627846387000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size(), "Blood pressure trend alert not triggered");
        assertEquals("Blood pressure trend alert", alerts.get(0).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Blood pressure trend alert for patient ID: 1"));
    }

    @Test
    public void testEvaluateCriticalThresholdAlert() {
        dataStorage.addPatientData(1, 190, "Systolic pressure", 1627846187000L);
        dataStorage.addPatientData(1, 55, "Diastolic pressure", 1627846287000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(2, alerts.size(), "Critical threshold alerts failed");
        assertEquals("Blood pressure critical threshold alert", alerts.get(0).getCondition());
        assertEquals("Blood pressure critical threshold alert", alerts.get(1).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Blood pressure critical threshold alert for patient ID: 1"));
    }

    @Test
    public void testEvaluateLowSaturationAlert() {
        dataStorage.addPatientData(1, 91, "Blood saturation", 1627846187000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size(), "Low saturation alert failed");
        assertEquals("Low saturation alert", alerts.get(0).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Low saturation alert for patient ID: 1"));
    }

    @Test
    public void testEvaluateRapidDropAlert() {
        long present = System.currentTimeMillis();
        dataStorage.addPatientData(1, 97, "Blood saturation", present);
        dataStorage.addPatientData(1, 91, "Blood saturation", present + 5 * 60 * 1000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(2, alerts.size(), "Rapid drop in saturation alert failed"); //expect 2 because we get low saturation alert and rapid drop
        assertEquals("Rapid drop in saturation alert", alerts.get(0).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Rapid drop in saturation alert for patient ID: 1"));
    }

    @Test
    public void testEvaluateHypotensiveHypoxemiaAlert() {
        dataStorage.addPatientData(1, 89, "Blood pressure", 1627846187000L);
        dataStorage.addPatientData(1, 91, "Blood saturation", 1627846187000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size(), "Hypotensive hypoxemia test failed");
        assertEquals("Hypotensive hypoxemia alert", alerts.get(0).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Hypotensive hypoxemia alert for patient ID: 1"));
    }

    @Test
    public void testEvaluateAbnormalECGData() {
        dataStorage.addPatientData(1, 1.0, "ECG", 1627846187000L);
        dataStorage.addPatientData(1, 1.1, "ECG", 1627846287000L);
        dataStorage.addPatientData(1, 1.2, "ECG", 1627846387000L);
        dataStorage.addPatientData(1, 1.3, "ECG", 1627846487000L);
        dataStorage.addPatientData(1, 1.4, "ECG", 1627846587000L);
        dataStorage.addPatientData(1, 1.5, "ECG", 1627846687000L);
        dataStorage.addPatientData(1, 1.6, "ECG", 1627846787000L);
        dataStorage.addPatientData(1, 1.7, "ECG", 1627846887000L);
        dataStorage.addPatientData(1, 1.8, "ECG", 1627846987000L);
        dataStorage.addPatientData(1, 1.9, "ECG", 1627847087000L);
        dataStorage.addPatientData(1, 5.0, "ECG", 1627847187000L);

        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);

        List<Alert> alerts = alertGenerator.getAlerts();
        assertEquals(1, alerts.size(), "ECG alert failed");
        assertEquals("Abnormal ECG data", alerts.get(0).getCondition());

        assertTrue(outContent.toString().contains("Alert triggered: Abnormal ECG data for patient ID: 1"));
    }
}
