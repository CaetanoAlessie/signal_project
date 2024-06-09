package data_management;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.MockDataReader;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {

    private DataStorage dataStorage;

    @BeforeEach
    void setUp() throws IOException {
        DataReader mockReader = new MockDataReader();
        dataStorage = new DataStorage();
        mockReader.readData(dataStorage);
    }

    @Test
    void testAddAndGetRecords() {
        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue());
        assertEquals(200.0, records.get(1).getMeasurementValue());
    }

    @Test
    void testGetRecordsForNonExistentPatient() {
        List<PatientRecord> records = dataStorage.getRecords(999, 0, Long.MAX_VALUE);
        assertEquals(0, records.size());
    }

    @Test
    void testAddPatientData() {
        dataStorage.addPatientData(2, 120.0, "Blood pressure", System.currentTimeMillis());
        List<PatientRecord> records = dataStorage.getRecords(2, 0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(120.0, records.get(0).getMeasurementValue());
    }

    @Test
    void testGetAllPatients() {
        List<Patient> patients = dataStorage.getAllPatients();
        assertEquals(1, patients.size());
        assertEquals(1, patients.get(0).getId());
    }
}
