package com.alerts;

import java.util.*;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

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

    public void evaluateAllPatients() {
        for (Patient patient : dataStorage.getAllPatients()) {
            evaluateData(patient);
        }
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
        checkBloodPressureAlerts(patient);
        checkBloodSaturationAlerts(patient);
        checkHypotensiveHypoxemiaAlert(patient);
        checkECGAlerts(patient);
    }

    private void checkBloodPressureAlerts(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        int count = 0;
        double lastValue = 0;
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood Pressure")) {
                double currentValue = record.getMeasurementValue();
                if (count > 0 && Math.abs(currentValue - lastValue) > 10) {
                    count++;
                } else {
                    count = 1;
                }
                lastValue = currentValue;
                if (count == 3) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Blood Pressure Trend Alert", System.currentTimeMillis()));
                    count = 0;
                }
                if (currentValue > 180 || currentValue < 90) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Blood Pressure Critical Threshold Alert", System.currentTimeMillis()));
                }
            }
        }
    }

    private void checkBloodSaturationAlerts(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood Saturation")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 92) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Low Saturation Alert", System.currentTimeMillis()));
                }
            }
        }
    }

    private void checkHypotensiveHypoxemiaAlert(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        boolean lowBloodPressure = false;
        boolean lowSaturation = false;
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood Pressure")) {
                if (record.getMeasurementValue() < 90) {
                    lowBloodPressure = true;
                }
            }
            if (record.getRecordType().equals("Blood Saturation")) {
                if (record.getMeasurementValue() < 92) {
                    lowSaturation = true;
                }
            }
        }
        if (lowBloodPressure && lowSaturation) {
            triggerAlert(new Alert(String.valueOf(patient.getId()), "Hypotensive Hypoxemia Alert", System.currentTimeMillis()));
        }
    }

    private void checkECGAlerts(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        double sum = 0;
        int count = 0;
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                sum += record.getMeasurementValue();
                count++;
            }
        }
        double average = sum / count;
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                if (record.getMeasurementValue() > average * 1.5) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Abnormal ECG Data", System.currentTimeMillis()));
                }
            }
        }
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
        System.out.println("Alert triggered: " + alert.getCondition() + " for patient ID: " + alert.getPatientId());
    }
}
