package com.data_management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class OutputDataReader implements DataReader {
    private String outputDirectory;

    public OutputDataReader(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File directory = new File(outputDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("Directory is wrong or doesn't exist: " + outputDirectory);
        }

        File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("There are no files in the directory: " + outputDirectory);
        }

        for (File file : files) {
            if (file.isFile() && file.canRead()) {
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] data = line.split(",");
                        if (data.length == 4) {
                            int patientId = Integer.parseInt(data[0]);
                            double measurementValue = Double.parseDouble(data[1]);
                            String recordType = data[2];
                            long timestamp = Long.parseLong(data[3]);
                            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                        }
                    }
                } catch (FileNotFoundException e) {
                    throw new IOException("File doesn't exist: " + file.getAbsolutePath());
                }
            }
        }
    }
}
