package data_management;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import com.data_management.FileDataReader;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileDataReaderTest {

    @TempDir
    Path tempDir;

    private DataStorage dataStorage;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
    }

    @Test
    public void testReadData() throws IOException {
        File tempFile = tempDir.resolve("test_data.csv").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("1,Blood pressure,120.5,1627846187000\n");
            writer.write("1,Blood pressure,121.5,1627846287000\n");
        }

        FileDataReader reader = new FileDataReader(tempFile.getAbsolutePath());
        reader.readData(dataStorage);

        List<PatientRecord> records = dataStorage.getRecords(1, 0, Long.MAX_VALUE);
        assertEquals(2, records.size());
        assertEquals(120.5, records.get(0).getMeasurementValue());
    }
}
