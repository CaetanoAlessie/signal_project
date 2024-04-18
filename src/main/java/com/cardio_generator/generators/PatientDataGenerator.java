package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * IS a patient data generator. For a single patient, this interface
 * specifies the terms of the data generation and output.
 */

public interface PatientDataGenerator {

    /**
     * Generates and outputs data for a specified patient.
     *
     * @param patientId the unique identifier of the patient whose data will be be generated
     * @param outputStrategy the strategy to use for outputting the generated data
     */

    void generate(int patientId, OutputStrategy outputStrategy);
}
