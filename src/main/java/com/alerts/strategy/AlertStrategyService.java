package com.alerts.strategy;

import com.alerts.AlertInterface;
import com.data_management.Patient;

public class AlertStrategyService {
    private AlertStrategy strategy;

    public AlertStrategyService(AlertStrategy strategy) {
        this.strategy = strategy;
    }

    public void evaluateAndNotify(Patient patient) {
        AlertInterface alert = strategy.checkAlert(patient);
        if (alert != null) {
            alert.notifyAlert();
        }
    }
}
