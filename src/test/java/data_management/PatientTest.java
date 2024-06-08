package data_management;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class PatientTest {

    @Test
    public void testAddRecord() {
        Patient patient = new Patient(1);
        patient.addRecord(120.5, "Blood pressure", 1627846187000L);

        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        assertEquals(1, records.size());
        assertEquals(120.5, records.get(0).getMeasurementValue());
    }

    @Test
    public void testGetRecordsWithinRange() {
        Patient patient = new Patient(1);
        patient.addRecord(120.5, "Blood pressure", 1627846187000L);
        patient.addRecord(121.5, "Blood pressure", 1627846287000L);

        List<PatientRecord> records = patient.getRecords(1627846187000L, 1627846286000L);
        assertEquals(1, records.size());
        assertEquals(120.5, records.get(0).getMeasurementValue());
    }

    @Test
    public void testGetRecordsOutsideRange() {
        Patient patient = new Patient(1);
        patient.addRecord(120.5, "Blood pressure", 1627846187000L);
        patient.addRecord(121.5, "Blood pressure", 1627846287000L);

        List<PatientRecord> records = patient.getRecords(1627846288000L, 1627846387000L);
        assertEquals(0, records.size());
    }

    @Test
    public void testGetRecordsMultipleRecords() {
        Patient patient = new Patient(1);
        patient.addRecord(120.5, "Blood pressure", 1627846187000L);
        patient.addRecord(121.5, "Blood pressure", 1627846287000L);
        patient.addRecord(122.5, "Blood pressure", 1627846387000L);

        List<PatientRecord> records = patient.getRecords(1627846187000L, 1627846387000L);
        assertEquals(3, records.size());
    }
}
