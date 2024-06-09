package data_management;

import com.alerts.AlertGenerator;
import com.cardio_generator.outputs.WebSocketOutputStrategy;
import org.junit.jupiter.api.*;
import com.data_management.MyWebSocketClient;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MyWebSocketClientTest {
    private MyWebSocketClient client;
    private static DataStorage dataStorage;
    private WebSocketOutputStrategy server;
    private boolean serverRunning;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() throws URISyntaxException, InterruptedException {
        dataStorage = new DataStorage();
        server = new WebSocketOutputStrategy(8080);
        serverRunning = true;
        client = new MyWebSocketClient(new URI("ws://localhost:8080"), dataStorage);
        client.connectBlocking();
        waitForCondition(() -> client.isOpen(), 5000);
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        if (client.isOpen()) {
            client.close();
        }
        if (serverRunning) {
            try {
                server.getServer().stop();
                serverRunning = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForCondition(CheckCondition condition, long timeoutMillis) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeoutMillis;
        while (!condition.check() && System.currentTimeMillis() < endTime) {
            Thread.sleep(50);
        }
    }

    private interface CheckCondition {
        boolean check();
    }

    @Test
    public void connectionTest() {
        assertTrue(client.isOpen(), "Client should be connected to the server");
    }

    @Test
    public void testInvalidMessageFormat() {
        String invalidMessage = "1,95,Blood pressure,1714376789052, 1714376789052";
        assertThrows(RuntimeException.class, () -> {
            client.onMessage(invalidMessage);
        });
    }

    @Test
    public void testIntegration() throws InterruptedException {
        client.onMessage("1,95,Blood pressure,1714376789052");
        client.onMessage("1,120.0,Blood pressure,1714376789152");

        waitForCondition(() -> dataStorage.getRecords(1, 1714376789052L, 1714376789152L).size() == 2, 5000);

        List<PatientRecord> records = dataStorage.getRecords(1, 1714376789052L, 1714376789152L);
        assertEquals(2, records.size(), "2 records should've been stored");
        assertEquals(95.0, records.get(0).getMeasurementValue(), "First value should be 95");
        assertEquals(120.0, records.get(1).getMeasurementValue(), "Second value should be 120");

        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        for (Patient patient : dataStorage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }

        assertTrue(outContent.toString().contains("Alert"), "Should print alert message");
    }

    @Test
    public void testOnError() {
        Exception exception = new Exception("Test error");
        assertDoesNotThrow(() -> client.onError(exception));
    }
}