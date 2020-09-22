package bg.sofia.uni.fmi.mjt.chat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerRequestSender implements Runnable {
    private Socket socket;

    ServerRequestSender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (Thread.interrupted()) {
                    socket.close();
                    break;
                }
                String message;
                message = scanner.nextLine(); //read line from console
                writer.println(message); //send line to server
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
