package com.alerts;

import com.alerts.AlertInterface;

public class BloodPressureAlert implements AlertInterface {
    private String patientId;
    private String condition;
    private long timestamp;

    public BloodPressureAlert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public void notifyAlert() {

        System.out.println("Blood Pressure Alert for patientID: " + patientId);
    }
}
