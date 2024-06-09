package com.alerts;

import com.alerts.AlertInterface;

public class ECGAlert implements AlertInterface {
    private String patientId;
    private String condition;
    private long timestamp;

    public ECGAlert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public void notifyAlert() {

        System.out.println("ECG Alert for patientID: " + patientId);
    }
}
