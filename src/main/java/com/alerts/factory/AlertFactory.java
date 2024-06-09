package com.alerts.factory;

import com.alerts.AlertInterface;

public abstract class AlertFactory {
    public abstract AlertInterface generateAlert(String patientId, String condition, long timestamp);
}
