package com.alerts.factory;

import com.alerts.AlertInterface;
import com.alerts.ECGAlert;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.LinkedList;
import java.util.List;

public class ECGAlertFactory extends AlertFactory {
    private DataStorage dataStorage;

    public ECGAlertFactory(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public AlertInterface generateAlert(String patientId, String condition, long timestamp) {
        List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId), 0, Long.MAX_VALUE);
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
                    return new ECGAlert(patientId, "Abnormal ECG data", timestamp);
                }
            }
        }
        return null;
    }
}