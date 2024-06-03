package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import java.util.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Implementation goes here
        checkBloodPressure(patient);
        checkBloodSaturation(patient);
        checkCombinedAlert(patient);
        checkECGData(patient);
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // Implementation might involve logging the alert or notifying staff
        System.out.println("Alert: " + alert.getCondition() + " for patient ID: " + alert.getPatientId());

    }

    private void checkBloodPressure(Patient patient) {
        List<Integer> systolicBloodPressure = patient.getSystolicBloodPressureReadings();
        List<Integer> diastolicBloodPressure = patient.getDiastolicBloodPressureReadings();

        checkTrendAlert(systolicBloodPressure, patient, "Systolic blood pressure trend");
        checkTrendAlert(diastolicBloodPressure, patient, "Diastolic blood pressure trend");
        checkExceedingThreshold(systolicBloodPressure, 180, 90, patient, "Systolic blood pressure exceeding threshold");
        checkExceedingThreshold(diastolicBloodPressure, 120, 60, patient, "diastolic blood pressure exceeding threshold");
    }

    private void checkTrendAlert(List<Integer> readings, Patient patient, String alertType) {
        if (readings.size() < 3) return;

        for (int i = 2; i < readings.size(); i++) {
            int change1 = readings.get(i) - readings.get(i - 1);
            int change2 = readings.get(i - 1) - readings.get(i - 2);

            if ((change1 > 10 && change2 > 10) || (change1 < -10 && change2 < -10)) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), alertType, System.currentTimeMillis()));
                break;
            }
        }
    }

    private void checkExceedingThreshold(List<Integer> readings, int upperThreshold, int lowerThreshold, Patient patient, String alertType) {
        for (int reading : readings) {
            if (reading > upperThreshold || reading < lowerThreshold) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), alertType, System.currentTimeMillis()));
                break;
            }
        }
    }

    private void checkBloodSaturation(Patient patient) {
        List<Integer> saturation = patient.getBloodOxygenSaturationReadings();

        checkLowSaturationAlert(saturation, patient);
        checkRapidDropAlert(saturation, patient);
    }

    private void checkLowSaturationAlert(List<Integer> readings, Patient patient) {
        for (int reading : readings) {
            if (reading < 92) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Low blood saturation alert", System.currentTimeMillis()));
                break;
            }
        }
    }

    private void checkRapidDropAlert(List<Integer> readings, Patient patient) {
        long currentTime = System.currentTimeMillis();
        int initialSaturation = readings.get(0);
        long initialTime = currentTime - 1000*60*10;
    
        for (int i = 1; i < readings.size(); i++) {
            int currentSaturation = readings.get(i);
            double percentageDrop = ((double) initialSaturation - currentSaturation) / initialSaturation * 100;
            if (percentageDrop >= 5 && currentTime - initialTime <= 1000*60*10) {
            triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Rapid blood saturation drop alert", currentTime));
            break;
            }
        }
    }
    

    private void checkCombinedAlert(Patient patient) {
        List<Integer> systolicReadings = patient.getSystolicBloodPressureReadings();
        List<Integer> saturationReadings = patient.getBloodOxygenSaturationReadings();

        for (int systolic : systolicReadings) {
            if (systolic < 90) {
                for (int saturation : saturationReadings) {
                    if (saturation < 92) {
                        triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "Hypotensive hypoxemia alert", System.currentTimeMillis()));
                        return;
                    }
                }
            }
        }
    }

    private void checkECGData(Patient patient) {
        List<Integer> ecgReadings = patient.getECGDataReadings();
        int window = 5;
        for (int i = window; i < ecgReadings.size(); i++) {
            double sum = 0;
            for (int j = i - window; j < i; j++) {
                sum += ecgReadings.get(j);
            }
            double average = sum / window;
            System.out.println("Average ECG: " + average + ", Current ECG: " + ecgReadings.get(i));
            if (ecgReadings.get(i) > average) {
                triggerAlert(new Alert(String.valueOf(patient.getPatientId()), "ECG alert", System.currentTimeMillis()));
                break;
            }
        }
    }
    
}

