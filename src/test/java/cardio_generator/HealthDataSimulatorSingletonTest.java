package cardio_generator;

import com.cardio_generator.HealthDataSimulator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HealthDataSimulatorSingletonTest {

    @Test
    public void testSingletonInstance() {
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance3 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance4 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance5 = HealthDataSimulator.getInstance();


        assertSame(instance1, instance2, "Both instances should be the same");
    }
}
