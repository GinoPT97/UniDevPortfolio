package com.LSO.cinebox.Infrastructure;

import android.net.TrafficStats;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnect {
    private static final String TAG = "ServerConnect";

    private static final int UDP_PORT = 5000;
    private static final String DISCOVERY_MESSAGE = "DISCOVER_SERVER";

    private static final String DEFAULT_SERVER_ADDRESS = "server";
    private static final int DEFAULT_SERVER_PORT = 8080;

    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private InetAddress discoveredAddress;
    private int discoveredPort;

    private final Object connectionLock = new Object();
    private final Object ioLock = new Object();
    private volatile boolean connecting = false;
    private volatile boolean persistentConnection = false;
    private volatile boolean shouldMaintainConnection = false;

    private final Object filmsLock = new Object();
    private volatile boolean fetchingFilms = false;

    public void startPersistentConnection(ConnectionCallback callback) {
        shouldMaintainConnection = true;
        persistentConnection = true;
        openConnection(new ConnectionCallback() {
            @Override
            public void onSuccess() {
                startConnectionKeepAlive();
                callback.onSuccess();
            }

            @Override
            public void onFailure(Exception e) {
                persistentConnection = false;
                shouldMaintainConnection = false;
                callback.onFailure(e);
            }
        });
    }

    public void stopPersistentConnection() {
        shouldMaintainConnection = false;
        persistentConnection = false;
        closeConnection();
    }

    private void startConnectionKeepAlive() {
        executorService.execute(() -> {
            while (shouldMaintainConnection) {
                try {
                    Thread.sleep(30000); // Check every 30 seconds
                    if (shouldMaintainConnection && isConnected()) {
                        // Send heartbeat to test connection
                        sendHeartbeat();
                    } else if (shouldMaintainConnection && !isConnected()) {
                        Log.d(TAG, "Riconnessione automatica...");
                        reconnectPersistent();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void sendHeartbeat() {
        try {
            synchronized (ioLock) {
                if (out != null && in != null && isConnected()) {
                    out.println("PING");
                    String response = in.readLine();
                    if (response == null) {
                        throw new IOException("Heartbeat senza risposta dal server");
                    }
                    if (!"PONG".equalsIgnoreCase(response.trim())) {
                        Log.w(TAG, "Heartbeat inatteso: " + response);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Errore durante l'invio dell'heartbeat: " + e.getMessage(), e);
            if (shouldMaintainConnection) {
                reconnectPersistent();
            }
        }
    }

    private void reconnectPersistent() {
        if (!shouldMaintainConnection) return;
        
        synchronized (connectionLock) {
            if (connecting) return;
            connecting = true;
        }

        try {
            closeConnection();
            Thread.sleep(2000); // Wait before reconnecting
            
            if (shouldMaintainConnection) {
                attemptConnection(new ConnectionCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Riconnessione riuscita");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Riconnessione fallita: " + e.getMessage(), e);
                        // Try again after delay
                        executorService.execute(() -> {
                            try {
                                Thread.sleep(5000);
                                if (shouldMaintainConnection) {
                                    reconnectPersistent();
                                }
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                        });
                    }
                }, discoveredAddress != null ? discoveredAddress : getDefaultAddress(), 
                   discoveredPort != 0 ? discoveredPort : DEFAULT_SERVER_PORT);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.e(TAG, "Riconnessione interrotta", e);
        } catch (Exception e) {
            Log.e(TAG, "Errore durante la riconnessione: " + e.getMessage(), e);
        } finally {
            connecting = false;
        }
    }

    private InetAddress getDefaultAddress() {
        try {
            return InetAddress.getByName(DEFAULT_SERVER_ADDRESS);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public void openConnection(ConnectionCallback callback) {
        synchronized (connectionLock) {
            if (isConnected()) {
                callback.onSuccess();
                return;
            }
            if (connecting) {
                executorService.execute(() -> {
                    while (connecting) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    if (isConnected()) callback.onSuccess();
                    else callback.onFailure(new IOException("Connessione non riuscita"));
                });
                return;
            }
            connecting = true;
        }

        discoverServer(new DiscoveryCallback() {
            @Override
            public void onDiscoverySuccess(InetAddress address, int port) {
                discoveredAddress = address;
                discoveredPort = port;
                attemptConnection(callback, discoveredAddress, discoveredPort);
            }

            @Override
            public void onDiscoveryFailure(Exception e) {
                Log.e(TAG, "Discovery fallita: " + e.getMessage(), e);
                try {
                    attemptConnection(callback, InetAddress.getByName(DEFAULT_SERVER_ADDRESS), DEFAULT_SERVER_PORT);
                } catch (UnknownHostException unknownHostException) {
                    handleError("Errore durante la connessione al server (fallback)", unknownHostException, callback);
                    closeConnection();
                }
            }
        });
    }

    private void attemptConnection(ConnectionCallback callback, InetAddress address, int port) {
        executorService.execute(() -> {
            for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                try {
                    TrafficStats.setThreadStatsTag(1000);
                    SocketAddress socketAddress = new InetSocketAddress(address, port);
                    socket = new Socket();
                    socket.connect(socketAddress);
                    TrafficStats.tagSocket(socket);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    Log.d(TAG, "Connessione al server su: " + address.getHostAddress() + ":" + port);
                    callback.onSuccess();
                } catch (IOException e) {
                    Log.e(TAG, "Tentativo " + attempt + " fallito: " + e.getMessage(), e);
                    if (attempt == MAX_RETRIES) {
                        handleError("Errore durante la connessione al server", e, callback);
                        closeConnection();
                    } else {
                        try {
                            Thread.sleep(RETRY_DELAY_MS);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } finally {
                    TrafficStats.clearThreadStatsTag();
                }
            }
            connecting = false;
        });
    }

    public void discoverServer(DiscoveryCallback callback) {
        executorService.execute(() -> {
            DatagramSocket ds = null;
            try {
                ds = new DatagramSocket();
                ds.setBroadcast(true);

                byte[] sendData = DISCOVERY_MESSAGE.getBytes();
                InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastAddress, UDP_PORT);
                ds.send(sendPacket);
                Log.d(TAG, "Messaggio di discovery inviato in broadcast.");

                ds.setSoTimeout(3000);
                byte[] recvBuf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                ds.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Log.d(TAG, "Risposta di discovery ricevuta: " + response);

                String[] parts = response.split(":");
                if (parts.length < 2) {
                    throw new IOException("Risposta non valida: " + response);
                }
                int port = Integer.parseInt(parts[1].trim());
                InetAddress address = receivePacket.getAddress();
                Log.d(TAG, "Server scoperto: " + address.getHostAddress() + ":" + port);
                callback.onDiscoverySuccess(address, port);
            } catch (Exception e) {
                Log.e(TAG, "Errore in discoverServer: " + e.getMessage(), e);
                callback.onDiscoveryFailure(e);
            } finally {
                if (ds != null) {
                    ds.close();
                }
            }
        });
    }

    public void closeConnection() {
        executorService.execute(() -> {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (out != null) out.close();
                if (in != null) in.close();
                Log.d(TAG, "Connessione al server chiusa.");
            } catch (IOException e) {
                Log.e(TAG, "Errore durante la chiusura della connessione.", e);
            } catch (Exception e) {
                Log.e(TAG, "Errore generico durante la chiusura della connessione: " + e.getMessage(), e);
            } finally {
                socket = null;
                out = null;
                in = null;
            }
        });
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public boolean isPersistentMode() {
        return persistentConnection && shouldMaintainConnection;
    }

    public void sendMessage(String message, MessageCallback callback) {
        if (message.equals("GET_FILMS")) {
            synchronized (filmsLock) {
                if (fetchingFilms) return;
                fetchingFilms = true;
            }
            executorService.execute(() -> {
                try {
                    sendMessageInternal(message, new MessageCallback() {
                        @Override
                        public void onSuccess(String response) {
                            synchronized (filmsLock) { fetchingFilms = false; }
                            callback.onSuccess(response);
                        }
                        @Override
                        public void onFailure(Exception e) {
                            synchronized (filmsLock) { fetchingFilms = false; }
                            callback.onFailure(e);
                        }
                    });
                } catch (Exception e) {
                    synchronized (filmsLock) { fetchingFilms = false; }
                    handleError("Errore durante l'invio del messaggio", e, callback);
                }
            });
        } else {
            executorService.execute(() -> {
                try {
                    if (!isConnected()) {
                        if (persistentConnection && shouldMaintainConnection) {
                            // If persistent connection is enabled but not connected, try to reconnect
                            reconnectPersistent();
                            // Wait a bit for reconnection
                            for (int i = 0; i < 10 && !isConnected(); i++) {
                                Thread.sleep(500);
                            }
                            if (!isConnected()) {
                                callback.onFailure(new IOException("Connessione persistente non disponibile"));
                                return;
                            }
                        } else if (!persistentConnection) {
                            // Only open new connection if not in persistent mode
                            synchronized (connectionLock) {
                                if (!isConnected()) {
                                    openConnection(new ConnectionCallback() {
                                        @Override
                                        public void onSuccess() {
                                            sendMessageInternal(message, callback);
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            handleError("Errore durante l'invio del messaggio", e, callback);
                                        }
                                    });
                                    return;
                                } else {
                                    sendMessageInternal(message, callback);
                                }
                            }
                        } else {
                            callback.onFailure(new IOException("Connessione non disponibile"));
                            return;
                        }
                    }
                    sendMessageInternal(message, callback);
                } catch (Exception e) {
                    handleError("Errore durante l'invio del messaggio", e, callback);
                }
            });
        }
    }

    private void sendMessageInternal(String message, MessageCallback callback) {
        try {
            synchronized (ioLock) {
                    Log.d(TAG, "Invio messaggio: " + message);
                out.println(message);
                if (message.startsWith("LIST_USERS") || message.startsWith("GET_NOTIFICATIONS") || message.startsWith("GET_FILMS") || message.startsWith("GET_ACTIVE_RENTALS_BY_USER") || message.startsWith("GET_LAST_5_RENTALS_BY_USER") || message.startsWith("GET_TOP_5_RENTED_FILMS") || message.startsWith("GET_ALL_RENTALS_OVERVIEW")) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        if ("PONG".equalsIgnoreCase(line.trim())) {
                            continue;
                        }
                        if (line.equals("END")) break;
                        response.append(line).append("\n");
                    }
                    String res = response.toString().trim();
                    Log.d(TAG, "Risposta ricevuta: " + res);
                    callback.onSuccess(res);
                } else {
                    String responseLine = readNextBusinessLine();
                    Log.d(TAG, "Risposta ricevuta: " + responseLine);
                    callback.onSuccess(responseLine);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Errore durante l'invio del messaggio: " + e.getMessage(), e);
            handleError("Errore durante l'invio del messaggio", e, callback);
        } catch (Exception e) {
            Log.e(TAG, "Errore generico durante l'invio del messaggio: " + e.getMessage(), e);
            handleError("Errore durante l'invio del messaggio", e, callback);
        }
    }

    private String readNextBusinessLine() throws IOException {
        String responseLine;
        while ((responseLine = in.readLine()) != null) {
            if (!"PONG".equalsIgnoreCase(responseLine.trim())) {
                return responseLine;
            }
        }
        throw new IOException("Connessione chiusa dal server");
    }

    public void fetchDataFromServer(DataCallback callback) {
        executorService.execute(() -> {
            try {
                StringBuilder response = new StringBuilder();
                InetAddress httpAddress = (discoveredAddress != null)
                        ? discoveredAddress
                        : InetAddress.getByName(DEFAULT_SERVER_ADDRESS);
                int httpPort = (discoveredPort != 0)
                        ? discoveredPort
                        : DEFAULT_SERVER_PORT;

                for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
                    HttpURLConnection connection = null;
                    try {
                        TrafficStats.setThreadStatsTag(1000);
                        URI uri = new URI("http", null, httpAddress.getHostAddress(), httpPort, "/", null, null);
                        connection = (HttpURLConnection) uri.toURL().openConnection();
                        connection.setRequestMethod("GET");

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                            }
                            callback.onSuccess(response.toString());
                            return;
                        } else {
                            Log.e(TAG, "Errore: risposta del server " + responseCode);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Tentativo " + attempt + " fallito: " + e.getMessage(), e);
                        if (attempt < MAX_RETRIES) {
                            try {
                                Thread.sleep(RETRY_DELAY_MS);
                            } catch (InterruptedException ie) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                        TrafficStats.clearThreadStatsTag();
                    }
                }
                callback.onFailure(new IOException("Failed to fetch data from server after " + MAX_RETRIES + " attempts"));
            } catch (UnknownHostException e) {
                callback.onFailure(e);
            }
        });
    }

    public void fetchDataFromServerWithCommand(String command, DataCallback callback) {
        executorService.execute(() -> {
            try {
                synchronized (ioLock) {
                    out.println(command);
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        if ("PONG".equalsIgnoreCase(line.trim())) {
                            continue;
                        }
                        response.append(line).append("\n");
                        if (line.equals("END")) break;
                    }
                    callback.onSuccess(response.toString());
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }

    private void handleError(String message, Exception e, ConnectionCallback callback) {
        Log.e(TAG, message + ": " + e.getMessage(), e);
        callback.onFailure(e);
    }

    private void handleError(String message, Exception e, MessageCallback callback) {
        Log.e(TAG, message + ": " + e.getMessage(), e);
        callback.onFailure(e);
    }

    public interface ConnectionCallback {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface MessageCallback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public interface DataCallback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public interface DiscoveryCallback {
        void onDiscoverySuccess(InetAddress address, int port);
        void onDiscoveryFailure(Exception e);
    }
}