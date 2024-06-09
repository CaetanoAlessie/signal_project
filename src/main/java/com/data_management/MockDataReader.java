package com.data_management;

public class MockDataReader implements DataReader {
    @Override
    public void readData(DataStorage dataStorage) {
        dataStorage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        dataStorage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);
    }
}
