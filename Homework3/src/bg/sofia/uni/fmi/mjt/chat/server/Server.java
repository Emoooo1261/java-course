package bg.sofia.uni.fmi.mjt.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 10;

    public static void main(String[] args) {
        Map<String, Socket> clientsMap = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server started and listening for connect requests...");
            Socket clientSocket;
            while (true) {

                // Calling accept() blocks and waits for connection request by a client
                // When a request comes, accept() returns a socket to communicate with this
                // client
                clientSocket = serverSocket.accept();
                System.out.println("Accepted connection request from client " + clientSocket);

                // We want each client to be processed in a separate thread
                // to keep the current thread free to accept() requests from new clients
                ClientRequestHandler clientRequestHandler = new ClientRequestHandler(clientSocket, clientsMap);
                executor.execute(clientRequestHandler); // use a thread pool to launch a thread
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
