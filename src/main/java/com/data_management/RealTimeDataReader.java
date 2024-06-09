package com.data_management;

import java.io.IOException;

public interface RealTimeDataReader {
    void connect(String address) throws IOException;

    void disconnect();
}

