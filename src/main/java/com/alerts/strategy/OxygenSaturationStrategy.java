package com.alerts.strategy;

import com.alerts.AlertInterface;
import com.alerts.BloodOxygenAlert;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class OxygenSaturationStrategy implements AlertStrategy {
    private DataStorage dataStorage;

    public OxygenSaturationStrategy(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public AlertInterface checkAlert(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        double previousValue = -1;
        long previousTimestamp = -1;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Blood saturation")) {
                double currentValue = record.getMeasurementValue();
                long currentTimestamp = record.getTimestamp();

                if (currentValue < 92) {
                    return new BloodOxygenAlert(String.valueOf(patient.getId()), "Low saturation alert", System.currentTimeMillis());
                }

                if (previousValue != -1 && (currentTimestamp - previousTimestamp) <= 10 * 60 * 1000L) {
                    double drop = previousValue - currentValue;
                    if (drop >= 5) {
                        return new BloodOxygenAlert(String.valueOf(patient.getId()), "Rapid drop in saturation alert", System.currentTimeMillis());
                    }
                }

                previousValue = currentValue;
                previousTimestamp = currentTimestamp;
            }
        }

        return null;
    }
}
