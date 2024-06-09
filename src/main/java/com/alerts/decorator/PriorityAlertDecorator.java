package com.alerts.decorator;

import com.alerts.AlertInterface;

public class PriorityAlertDecorator extends AlertDecorator {
    private String alertPriority;

    public PriorityAlertDecorator(AlertInterface decoratedAlert, String alertPriority) {
        super(decoratedAlert);
        this.alertPriority = alertPriority;
    }

    @Override
    public void notifyAlert() {
        System.out.println("Priority: " + alertPriority);
        super.notifyAlert();
    }
}
