package bg.sofia.uni.fmi.mjt.chat.client;

import bg.sofia.uni.fmi.mjt.chat.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static final String SERVER_HOST = "localhost";

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, Server.SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            System.out.println("Connected to the server!");
            ServerRequestSender requestSender = new ServerRequestSender(socket);
            Thread requestSenderThread = new Thread(requestSender);
            requestSenderThread.start();
            while (true) {
                String serverReply = reader.readLine();
                if ("QUIT".equals(serverReply)) {
                    System.out.println("Press any key to continue!");
                    requestSenderThread.interrupt();
                    break;
                }
                System.out.println(serverReply);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}