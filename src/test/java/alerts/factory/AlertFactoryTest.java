package alerts.factory;

import com.alerts.AlertInterface;
import com.alerts.BloodOxygenAlert;
import com.alerts.BloodPressureAlert;
import com.alerts.ECGAlert;
import com.alerts.factory.*;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertFactoryTest {

    @Test
    public void testBloodPressureAlertFactory() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(1, 150, "Systolic pressure", System.currentTimeMillis());
        dataStorage.addPatientData(1, 165, "Systolic pressure", System.currentTimeMillis() + 1000);
        dataStorage.addPatientData(1, 180, "Systolic pressure", System.currentTimeMillis() + 2000); // Trend alert
        dataStorage.addPatientData(1, 90, "Diastolic pressure", System.currentTimeMillis());
        dataStorage.addPatientData(1, 110, "Diastolic pressure", System.currentTimeMillis() + 1000);
        dataStorage.addPatientData(1, 130, "Diastolic pressure", System.currentTimeMillis() + 2000); // Critical threshold alert

        BloodPressureAlertFactory factory = new BloodPressureAlertFactory(dataStorage);
        AlertInterface alert = factory.generateAlert("1", "Blood pressure trend alert", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);

        alert = factory.generateAlert("1", "Blood pressure critical threshold alert", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof BloodPressureAlert);
    }

    @Test
    public void testBloodOxygenAlertFactory() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(1, 94, "Blood saturation", System.currentTimeMillis());
        dataStorage.addPatientData(1, 93, "Blood saturation", System.currentTimeMillis() + 300000); // 5 minutes later
        dataStorage.addPatientData(1, 88, "Blood saturation", System.currentTimeMillis() + 600000); // 10 minutes later

        BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory(dataStorage);
        AlertInterface alert = factory.generateAlert("1", "Low saturation alert", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof BloodOxygenAlert);
    }

    @Test
    public void testECGAlertFactory() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(1, 1.0, "ECG", System.currentTimeMillis());
        dataStorage.addPatientData(1, 1.1, "ECG", System.currentTimeMillis() + 1000);
        dataStorage.addPatientData(1, 1.2, "ECG", System.currentTimeMillis() + 2000);
        dataStorage.addPatientData(1, 1.3, "ECG", System.currentTimeMillis() + 3000);
        dataStorage.addPatientData(1, 2.0, "ECG", System.currentTimeMillis() + 4000); // Abnormal peak

        ECGAlertFactory factory = new ECGAlertFactory(dataStorage);
        AlertInterface alert = factory.generateAlert("1", "Abnormal ECG data", System.currentTimeMillis());
        assertNotNull(alert);
        assertTrue(alert instanceof ECGAlert);
    }
}
