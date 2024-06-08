package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDataReader implements DataReader {
    private String outputDir;

    public FileDataReader(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(outputDir))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int patientId = Integer.parseInt(data[0]);
                String recordType = data[1];
                double measurementValue = Double.parseDouble(data[2]);
                long timestamp = Long.parseLong(data[3]);

                dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
            }
        }
    }
}
