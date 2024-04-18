package com.cardio_generator.outputs;

/**
 * Specifies a method for processing and displaying patient data. This interface is a foundation 
 * for various implementations that dictate how patient data is presented.
 */

public interface OutputStrategy {

    /**
     * Sends or records data related to a specific patient event.
     * 
     * @param patientId the patient ID
     * @param timestamp the moment the data was recorded
     * @param label a descriptor of the data type
     * @param data the content of the data to be handled
     */

    void output(int patientId, long timestamp, String label, String data);
}
