package data_management;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.data_management.DataStorage;

public class DataStorageSingletonTest {

    @Test
    public void testSingletonInstance() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();
        DataStorage instance3 = DataStorage.getInstance();
        DataStorage instance4 = DataStorage.getInstance();
        DataStorage instance5 = DataStorage.getInstance();

        assertSame(instance1, instance2, "Both instances should be the same");
    }
}
