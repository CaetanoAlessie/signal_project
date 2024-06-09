package com.data_management;

import com.cardio_generator.outputs.WebSocketOutputStrategy;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MyWebSocketClient extends WebSocketClient implements RealTimeDataReader {

    private DataStorage storage;

    public MyWebSocketClient(URI serverUri, DataStorage storage) throws URISyntaxException {
        super(serverUri);
        this.storage = storage;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to Server");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Server connection ended: " + reason);
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error on Websocket");
        e.printStackTrace();
    }

    @Override
    public void onMessage(String message) {
        try {
            parseWebSocketData(message);
        } catch (Exception e) {
            throw new RuntimeException("Error Parsing: " + e.getMessage());
        }
    }

    public void parseWebSocketData(String message) throws Exception {
        // Parse the message into respective data types
        String[] parts = message.split(",");
        if (parts.length != 4) {
            throw new Exception("Invalid message format: " + message);
        }

        try {
            int patientId = Integer.parseInt(parts[0]);
            double measurementValue = Double.parseDouble(parts[1]);
            String recordType = parts[2];
            long timestamp = Long.parseLong(parts[3]);
            storage.addPatientData(patientId, measurementValue, recordType, timestamp);
        } catch (NumberFormatException e) {
            throw new Exception("Error parsing message: " + message, e);
        }
    }

    @Override
    public void connect(String address) throws IOException {
        try {
            URI uri = new URI(address);
            if (uri.getScheme() == null) {
                throw new URISyntaxException(address, "Missing URI scheme");
            }
            this.uri = uri; // Update the URI in the WebSocketClient
            this.connectBlocking();
        } catch (URISyntaxException e) {
            throw new IOException("Invalid WebSocket server address: " + address, e);
        } catch (InterruptedException e) {
            throw new IOException("Interrupted while connecting to WebSocket server", e);
        }
    }

    @Override
    public void disconnect() {
        if (this.isOpen()) {
            this.close();
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String address = "ws://localhost:8080";
        WebSocketOutputStrategy server = new WebSocketOutputStrategy(8080);
        DataStorage dataStorage = new DataStorage();
        MyWebSocketClient client = new MyWebSocketClient(new URI(address), dataStorage);
        client.connect(address);
    }
}
