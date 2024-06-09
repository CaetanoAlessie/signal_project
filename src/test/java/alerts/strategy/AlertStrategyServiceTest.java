package alerts.strategy;

import com.alerts.strategy.*;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AlertStrategyServiceTest {
    private DataStorage dataStorage;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
    }

    @Test
    public void testBloodPressureAlert() {
        dataStorage.addPatientData(1, 185, "Systolic pressure", System.currentTimeMillis());
        dataStorage.addPatientData(1, 195, "Systolic pressure", System.currentTimeMillis() + 1000);
        dataStorage.addPatientData(1, 205, "Systolic pressure", System.currentTimeMillis() + 2000);

        AlertStrategy strategy = new BloodPressureStrategy(dataStorage);
        AlertStrategyService service = new AlertStrategyService(strategy);

        service.evaluateAndNotify(dataStorage.getPatientById(1));
    }

    @Test
    public void testBloodOxygenAlert() {
        dataStorage.addPatientData(1, 90, "Blood saturation", System.currentTimeMillis());

        AlertStrategy strategy = new OxygenSaturationStrategy(dataStorage);
        AlertStrategyService service = new AlertStrategyService(strategy);

        service.evaluateAndNotify(dataStorage.getPatientById(1));
    }

    @Test
    public void testHeartRateAlert() {
        dataStorage.addPatientData(1, 50, "Heart rate", System.currentTimeMillis());
        dataStorage.addPatientData(1, 55, "Heart rate", System.currentTimeMillis() + 1000);
        dataStorage.addPatientData(1, 130, "Heart rate", System.currentTimeMillis() + 2000);

        AlertStrategy strategy = new HeartRateStrategy(dataStorage);
        AlertStrategyService service = new AlertStrategyService(strategy);

        service.evaluateAndNotify(dataStorage.getPatientById(1));
    }
}
