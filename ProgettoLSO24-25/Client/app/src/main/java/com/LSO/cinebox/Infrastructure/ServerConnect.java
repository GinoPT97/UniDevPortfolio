package com.LSO.cinebox.Infrastructure;

import android.net.TrafficStats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnect {

    private static final int UDP_PORT = 5000;
    private static final String DISCOVERY_MESSAGE = "DISCOVER_SERVER";

    private static final String DEFAULT_SERVER_ADDRESS = "server";
    private static final int DEFAULT_SERVER_PORT = 8080;

    private static final int maxRetries = 3;
    private static final int retryDelay = 2000;

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private InetAddress discoveredAddress;
    private int discoveredPort;

    private final Object connectionLock = new Object();
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
                        System.out.println("Riconnessione automatica...");
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
            if (out != null && isConnected()) {
                out.println("PING");
                // Non aspettiamo la risposta PONG per non bloccare
            }
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'heartbeat: " + e.getMessage());
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
                        System.out.println("Riconnessione riuscita");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.err.println("Riconnessione fallita: " + e.getMessage());
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
        } catch (Exception e) {
            System.err.println("Errore durante la riconnessione: " + e.getMessage());
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
                        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
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
                System.err.println("Discovery fallita: " + e.getMessage());
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
            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                try {
                    TrafficStats.setThreadStatsTag(1000);
                    SocketAddress socketAddress = new InetSocketAddress(address, port);
                    socket = new Socket();
                    socket.connect(socketAddress);
                    TrafficStats.tagSocket(socket);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Connessione al server su: " + address.getHostAddress() + ":" + port);
                    callback.onSuccess();
                } catch (IOException e) {
                    System.err.println("Tentativo " + attempt + " fallito: " + e.getMessage());
                    if (attempt == maxRetries) {
                        handleError("Errore durante la connessione al server", e, callback);
                        closeConnection();
                    } else {
                        try {
                            Thread.sleep(retryDelay);
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
                System.out.println("Messaggio di discovery inviato in broadcast.");

                ds.setSoTimeout(3000);
                byte[] recvBuf = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                ds.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Risposta di discovery ricevuta: " + response);

                String[] parts = response.split(":");
                if (parts.length < 2) {
                    throw new IOException("Risposta non valida: " + response);
                }
                int port = Integer.parseInt(parts[1].trim());
                InetAddress address = receivePacket.getAddress();
                System.out.println("Server scoperto: " + address.getHostAddress() + ":" + port);
                callback.onDiscoverySuccess(address, port);
            } catch (Exception e) {
                System.err.println("Errore in discoverServer: " + e.getMessage());
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
                System.out.println("Connessione al server chiusa.");
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura della connessione.");
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println("Errore generico durante la chiusura della connessione: " + e.getMessage());
                e.printStackTrace();
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
            System.out.println("Invio messaggio: " + message);
            out.println(message);
            if (message.startsWith("LIST_USERS") || message.startsWith("GET_NOTIFICATIONS") || message.startsWith("GET_FILMS") || message.startsWith("GET_ACTIVE_RENTALS_BY_USER") || message.startsWith("GET_LAST_5_RENTALS_BY_USER") || message.startsWith("GET_TOP_5_RENTED_FILMS") || message.startsWith("GET_ALL_RENTALS_OVERVIEW")) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("END")) break;
                    response.append(line).append("\n");
                }
                String res = response.toString().trim();
                System.out.println("Risposta ricevuta: " + res);
                callback.onSuccess(res);
            } else {
                String responseLine = in.readLine();
                System.out.println("Risposta ricevuta: " + responseLine);
                callback.onSuccess(responseLine);
            }
        } catch (IOException e) {
            System.err.println("Errore durante l'invio del messaggio: " + e.getMessage());
            e.printStackTrace();
            handleError("Errore durante l'invio del messaggio", e, callback);
        } catch (Exception e) {
            System.err.println("Errore generico durante l'invio del messaggio: " + e.getMessage());
            e.printStackTrace();
            handleError("Errore durante l'invio del messaggio", e, callback);
        }
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

                for (int attempt = 1; attempt <= maxRetries; attempt++) {
                    HttpURLConnection connection = null;
                    try {
                        TrafficStats.setThreadStatsTag(1000);
                        URL url = new URL("http", httpAddress.getHostAddress(), httpPort, "");
                        connection = (HttpURLConnection) url.openConnection();
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
                            System.err.println("Errore: risposta del server " + responseCode);
                        }
                    } catch (Exception e) {
                        System.err.println("Tentativo " + attempt + " fallito: " + e.getMessage());
                        e.printStackTrace();
                        if (attempt < maxRetries) {
                            try {
                                Thread.sleep(retryDelay);
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
                callback.onFailure(new IOException("Failed to fetch data from server after " + maxRetries + " attempts"));
            } catch (UnknownHostException e) {
                callback.onFailure(e);
            }
        });
    }

    public void fetchDataFromServerWithCommand(String command, DataCallback callback) {
        executorService.execute(() -> {
            try {
                synchronized (this) {
                    out.println(command);
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
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
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
        callback.onFailure(e);
    }

    private void handleError(String message, Exception e, MessageCallback callback) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
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