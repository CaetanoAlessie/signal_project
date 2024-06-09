package com.alerts.strategy;

import com.alerts.AlertInterface;
import com.alerts.BloodPressureAlert;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {
    private DataStorage dataStorage;

    public BloodPressureStrategy(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public AlertInterface checkAlert(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getId(), 0, Long.MAX_VALUE);
        int countIncrease = 0;
        int countDecrease = 0;
        double lastValue = 0;
        boolean firstRecord = true;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Systolic pressure") || record.getRecordType().equals("Diastolic pressure")) {
                double currentValue = record.getMeasurementValue();
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
                        return new BloodPressureAlert(String.valueOf(patient.getId()), "Blood pressure trend alert", System.currentTimeMillis());
                    }
                } else {
                    firstRecord = false;
                }

                if ((record.getRecordType().equals("Systolic pressure") && (currentValue > 180 || currentValue < 90)) ||
                        (record.getRecordType().equals("Diastolic pressure") && (currentValue > 120 || currentValue < 60))) {
                    return new BloodPressureAlert(String.valueOf(patient.getId()), "Blood pressure critical threshold alert", System.currentTimeMillis());
                }

                lastValue = currentValue;
            }
        }
        return null;
    }
}
