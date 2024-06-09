package com.alerts;

public class HeartRateAlert implements AlertInterface {
    private String patientId;
    private String condition;
    private long timestamp;

    public HeartRateAlert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public void notifyAlert() {
        System.out.println("Heart Rate Alert for patientID: " + patientId);
    }
}
