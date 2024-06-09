package com.alerts.decorator;

import com.alerts.AlertInterface;

public abstract class AlertDecorator implements AlertInterface {
    protected AlertInterface decoratedAlert;

    public AlertDecorator(AlertInterface decoratedAlert) {
        this.decoratedAlert = decoratedAlert;
    }

    @Override
    public void notifyAlert() {
        decoratedAlert.notifyAlert();
    }
}
