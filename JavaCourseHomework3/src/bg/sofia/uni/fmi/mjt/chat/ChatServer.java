package bg.sofia.uni.fmi.mjt.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private static final int SERVER_PORT = 4444;
    private static final int MAX_EXECUTOR_THREADS = 10;

    //ArrayList
    static HashSet<ClientHandler> clients = new HashSet<>();

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT);) {

            System.out.println("Server started and listening for connect requests");

            Socket clientSocket;

            while (true) {

                // Calling accept() blocks and waits for connection request by a client
                // When a request comes, accept() returns a socket to communicate with this
                // client
                clientSocket = serverSocket.accept();

                System.out.println("Accepted connection request from client " + clientSocket); // + clientSocket

                // Obtain input and output streams
                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

                int clientsIndex = clients.size();

                // We want each client to be processed in a separate thread
                // to keep the current thread free to accept() requests from new clients
                ClientHandler clientHandler = new ClientHandler(clientSocket, "Client " + clientsIndex, dis, dos);

                // Create a new Thread with this object
                Thread t = new Thread(clientHandler);
                //executor.execute(clientHandler); // use a thread pool to launch a thread

                System.out.println("Adding client #" + clientsIndex + " to active client list");
                // Add this client to active clients list
                clients.add(clientHandler);

                // Start the thread.
                t.start();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
