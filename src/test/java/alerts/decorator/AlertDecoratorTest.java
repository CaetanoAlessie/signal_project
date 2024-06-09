package alerts.decorator;

import com.alerts.*;
import com.alerts.decorator.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class AlertDecoratorTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testRepeatedAlertDecorator() {
        AlertInterface alert = new BloodPressureAlert("1", "High systolic pressure", System.currentTimeMillis());
        AlertInterface repeatedAlert = new RepeatedAlertDecorator(alert, 3);

        repeatedAlert.notifyAlert();

        String expectedOutput = "Blood Pressure Alert for patientID: 1\n".repeat(3);
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testPriorityAlertDecorator() {
        AlertInterface alert = new BloodPressureAlert("1", "Low systolic pressure", System.currentTimeMillis());
        AlertInterface priorityAlert = new PriorityAlertDecorator(alert, "High");

        priorityAlert.notifyAlert();

        String expectedOutput = "Priority: High\nBlood Pressure Alert for patientID: 1\n";
        assertEquals(expectedOutput, outContent.toString());
    }
}
