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
    private List<Alert> alerts; // storing alerts for testing units (not necessary for the other things)

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.alerts = new ArrayList<>();
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
        checkHypotensiveHypoxemiaAlert(patient);
        checkRapidDropAlerts(patient);
        checkBloodSaturationAlerts(patient);
        checkBloodPressureAlerts(patient);
        checkECGAlerts(patient);
    }

    private void checkBloodPressureAlerts(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        int countIncrease = 0;
        int countDecrease = 0;
        double lastValue = 0;
        boolean firstRecord = true;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Systolic pressure") || record.getRecordType().equals("Diastolic pressure")) {
                double currentValue = record.getMeasurementValue();
                System.out.println(record.getRecordType() + ": " + currentValue);
                if (!firstRecord) {
                    if (currentValue > lastValue + 10) {
                        countIncrease++;
                        countDecrease = 0;
                    } else if (currentValue < lastValue - 10) {
                        countDecrease++;
                        countIncrease = 0;
                    } else {
                        countIncrease = 0;
                        countDecrease = 0;
                    }

                    if (countIncrease == 2 || countDecrease == 2) {
                        triggerAlert(new Alert(String.valueOf(patient.getId()), "Blood pressure trend alert", System.currentTimeMillis()));
                        countIncrease = 0;
                        countDecrease = 0;
                    }
                } else {
                    firstRecord = false;
                }

                if ((record.getRecordType().equals("Systolic pressure") && (currentValue > 180 || currentValue < 90)) ||
                        (record.getRecordType().equals("Diastolic pressure") && (currentValue > 120 || currentValue < 60))) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Blood pressure critical threshold alert", System.currentTimeMillis()));
                }

                lastValue = currentValue;
            }
        }
    }


    private void checkBloodSaturationAlerts(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        boolean lowBloodPressure = false;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood pressure") && record.getMeasurementValue() < 90) {
                lowBloodPressure = true;
                break;
            }
        }

        if (!lowBloodPressure) {
            for (PatientRecord record : records) {
                if (record.getRecordType().equals("Blood saturation")) {
                    double currentValue = record.getMeasurementValue();
                    if (currentValue < 92) {
                        triggerAlert(new Alert(String.valueOf(patient.getId()), "Low saturation alert", System.currentTimeMillis()));
                    }
                }
            }
        }
    }

    private void checkRapidDropAlerts(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        double previousValue = -1;
        long previousTimestamp = -1;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood saturation")) {
                double currentValue = record.getMeasurementValue();
                long currentTimestamp = record.getTimestamp();

                if (previousValue != -1 && (currentTimestamp - previousTimestamp) <= 10 * 60 * 1000L) {
                    double drop = previousValue - currentValue;
                    if (drop >= 5) {
                        triggerAlert(new Alert(String.valueOf(patient.getId()), "Rapid drop in saturation alert", System.currentTimeMillis()));
                    }
                }

                previousValue = currentValue;
                previousTimestamp = currentTimestamp;
            }
        }
    }


    private void checkHypotensiveHypoxemiaAlert(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        boolean lowBloodPressure = false;
        boolean lowSaturation = false;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood pressure")) {
                if (record.getMeasurementValue() < 90) {
                    lowBloodPressure = true;
                }
            }
            if (record.getRecordType().equals("Blood saturation")) {
                if (record.getMeasurementValue() < 92) {
                    lowSaturation = true;
                }
            }
            if (lowBloodPressure && lowSaturation) {
                triggerAlert(new Alert(String.valueOf(patient.getId()), "Hypotensive hypoxemia alert", System.currentTimeMillis()));
            }
        }
    }




    private void checkECGAlerts(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        final int slidingWindow = 10;
        double sum = 0;
        int count = 0;
        LinkedList<Double> window = new LinkedList<>();

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                double currentValue = record.getMeasurementValue();
                window.add(currentValue);
                sum += currentValue;
                count++;

                if (count > slidingWindow) {
                    double removedValue = window.removeFirst();
                    sum -= removedValue;
                    count--;
                }

                double average = sum / count;

                if (currentValue > average * 1.5) {
                    triggerAlert(new Alert(String.valueOf(patient.getId()), "Abnormal ECG data", System.currentTimeMillis()));
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
        alerts.add(alert); // Add alert to list for testing
        System.out.println("Alert triggered: " + alert.getCondition() + " for patient ID: " + alert.getPatientId());
    }

    // used for testing
    public List<Alert> getAlerts() {
        return alerts;
    }
}
