package com.alerts.strategy;

import com.alerts.AlertInterface;
import com.data_management.Patient;

public interface AlertStrategy {
    AlertInterface checkAlert(Patient patient);
}
