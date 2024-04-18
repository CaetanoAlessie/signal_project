package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Creates alert data for patients simulating where alerts might be triggered or resolved.
 */

public class AlertGenerator implements PatientDataGenerator {

    // Use uppercase for static final
    public static final Random RANDOM_GENERATOR = new Random();

    // Use camelCase
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Creates an alert generator for a specified number of patients.
     *
     * @param patientCount the number of patients to manage alerts for
     */

    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Creates and outputs alert status data for a specific patient.
     *
     * @param patientId the patient ID
     * @param outputStrategy the strategy used to output the generated data
     */

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // changed lambda to lowercase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
