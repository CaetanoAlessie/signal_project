package com.alerts.factory;

import com.alerts.AlertInterface;
import com.alerts.BloodPressureAlert;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureAlertFactory extends AlertFactory {
    private DataStorage dataStorage;

    public BloodPressureAlertFactory(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public AlertInterface generateAlert(String patientId, String condition, long timestamp) {
        List<PatientRecord> records = dataStorage.getRecords(Integer.parseInt(patientId), 0, Long.MAX_VALUE);
        int countIncreaseSystolic = 0;
        int countDecreaseSystolic = 0;
        int countIncreaseDiastolic = 0;
        int countDecreaseDiastolic = 0;
        double lastSystolic = 0;
        double lastDiastolic = 0;
        boolean firstRecord = true;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Systolic pressure")) {
                double currentValue = record.getMeasurementValue();
                if (!firstRecord) {
                    if (currentValue > lastSystolic + 10) {
                        countIncreaseSystolic++;
                        countDecreaseSystolic = 0;
                    } else if (currentValue < lastSystolic - 10) {
                        countDecreaseSystolic++;
                        countIncreaseSystolic = 0;
                    } else {
                        countIncreaseSystolic = 0;
                        countDecreaseSystolic = 0;
                    }

                    if (countIncreaseSystolic == 2 || countDecreaseSystolic == 2) {
                        return new BloodPressureAlert(patientId, "Blood pressure trend alert", timestamp);
                    }
                } else {
                    firstRecord = false;
                }

                if (currentValue > 180 || currentValue < 90) {
                    return new BloodPressureAlert(patientId, "Blood pressure critical threshold alert", timestamp);
                }

                lastSystolic = currentValue;
            } else if (record.getRecordType().equals("Diastolic pressure")) {
                double currentValue = record.getMeasurementValue();
                if (!firstRecord) {
                    if (currentValue > lastDiastolic + 10) {
                        countIncreaseDiastolic++;
                        countDecreaseDiastolic = 0;
                    } else if (currentValue < lastDiastolic - 10) {
                        countDecreaseDiastolic++;
                        countIncreaseDiastolic = 0;
                    } else {
                        countIncreaseDiastolic = 0;
                        countDecreaseDiastolic = 0;
                    }

                    if (countIncreaseDiastolic == 2 || countDecreaseDiastolic == 2) {
                        return new BloodPressureAlert(patientId, "Blood pressure trend alert", timestamp);
                    }
                } else {
                    firstRecord = false;
                }

                if (currentValue > 120 || currentValue < 60) {
                    return new BloodPressureAlert(patientId, "Blood pressure critical threshold alert", timestamp);
                }

                lastDiastolic = currentValue;
            }
        }

        return null;
    }
}