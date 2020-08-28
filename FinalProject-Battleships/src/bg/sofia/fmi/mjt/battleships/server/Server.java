package bg.sofia.fmi.mjt.battleships.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server<ClientRequestHandler> {
    public static final int SERVER_PORT = 4444;
    public static final int MAX_EXECUTOR_THREADS = 20;
    private static final String SERVER_LISTENING_MESSAGE = "The server has started and is waiting for connect requests!";
    private static final String CLIENT_SOCKET_MESSAGE = "Client socket: ";
    private static final String ACCEPTED_CONNECTION_MESSAGE = "Accepted connection request from client ";

    public static void main(String[] args) {


        //Map<String, Game> games = new ConcurrentHashMap<>();
        Set<String> playerNames = Collections.newSetFromMap(new ConcurrentHashMap<>());

        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT))

        {
            System.out.println(SERVER_LISTENING_MESSAGE);

            Socket clientSocket;
            while (true) {

                clientSocket = serverSocket.accept();

                System.out.println(CLIENT_SOCKET_MESSAGE + clientSocket);
                System.out.println(ACCEPTED_CONNECTION_MESSAGE + "[" + clientSocket.getInetAddress() + "]");

                //ClientRequestHandler clientHandler = new ClientRequestHandler(clientSocket, games, playerNames);
                //executor.execute(clientHandler);
            }

        } catch(IOException e)

        {
            System.out.println("Problem with creating server socket occurred.");
        }
    }
}
