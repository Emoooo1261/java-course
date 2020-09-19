package bg.sofia.fmi.mjt.battleships.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main Server class.
 * Starts listening for connections and
 * executes clientHandler thread for each of them.
 */
public class Server {
    public static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 20;
    private static final String SERVER_LISTENING_MESSAGE =
            "The server has started and is waiting for connect requests!";
    private static final String CLIENT_SOCKET_MESSAGE = "Client socket: ";
    private static final String ACCEPTED_CONNECTION_MESSAGE =
            "Accepted connection request from client ";

    public static void main(String[] args) {
        ConcurrentHashMap<String, GameInfo> games = new ConcurrentHashMap<>();
        Set<String> playersNames = ConcurrentHashMap.newKeySet();
        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println(SERVER_LISTENING_MESSAGE);
            Socket clientSocket;
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println(CLIENT_SOCKET_MESSAGE + clientSocket);
                System.out.println(ACCEPTED_CONNECTION_MESSAGE
                        + "[" + clientSocket.getInetAddress() + "]");
                ClientHandler clientHandler = new ClientHandler(clientSocket, games, playersNames);
                executor.execute(clientHandler);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
