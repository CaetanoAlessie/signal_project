package com.alerts.strategy;

import com.alerts.AlertInterface;
import com.alerts.HeartRateAlert;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class HeartRateStrategy implements AlertStrategy {
    private DataStorage dataStorage;

    public HeartRateStrategy(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public AlertInterface checkAlert(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);

        final int CRITICAL_HIGH = 120;
        final int CRITICAL_LOW = 50;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Heart rate")) {
                double currentValue = record.getMeasurementValue();

                if (currentValue > CRITICAL_HIGH || currentValue < CRITICAL_LOW) {
                    return new HeartRateAlert(String.valueOf(patient.getId()), "Abnormal heart rate alert", System.currentTimeMillis());
                }
            }
        }

        return null;
    }
}
