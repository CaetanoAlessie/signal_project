package com.alerts.decorator;

import com.alerts.AlertInterface;

public class RepeatedAlertDecorator extends AlertDecorator {
    private int numberOfRepeats;

    public RepeatedAlertDecorator(AlertInterface decoratedAlert, int numberOfRepeats) {
        super(decoratedAlert);
        this.numberOfRepeats = numberOfRepeats;
    }

    @Override
    public void notifyAlert() {
        for (int i = 0; i < numberOfRepeats; i++) {
            super.notifyAlert();
        }
    }
}
