package com.alerts.factory;

import com.alerts.AlertInterface;
import com.alerts.BloodOxygenAlert;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodOxygenAlertFactory extends AlertFactory {
    private DataStorage dataStorage;

    public BloodOxygenAlertFactory(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public AlertInterface generateAlert(String patientId, String condition, long timestamp) {
        List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId), 0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood saturation")) {
                double currentValue = record.getMeasurementValue();
                if (currentValue < 92) {
                    return new BloodOxygenAlert(patientId, "Low Saturation Alert", timestamp);
                }
            }
        }
        return null;
    }
}