package bg.sofia.fmi.mjt.battleships.client;

import bg.sofia.fmi.mjt.battleships.game.Game;
import bg.sofia.fmi.mjt.battleships.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Listens for input from the Server
 * and starts clientResponseHandler, which listens for input from the Client.
 * Also starts the Game when it gets the right input from the Server
 */
public class ServerResponseHandler implements Runnable {

    public static final String DISCONNECTED_FROM_THE_SERVER_MSG = "Disconnected from the server!";
    public static final String GAME_STARTED_MSG = "Game started!";
    public static final String LOCALHOST = "localhost";

    @Override
    public void run() {
        try (Socket socket = new Socket(LOCALHOST, Server.SERVER_PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            ClientResponseHandler clientResponseHandler = new ClientResponseHandler(writer);
            Thread clientResponseThread = new Thread(clientResponseHandler);
            clientResponseThread.start();
            String serverReply;
            while (true) {
                serverReply = reader.readLine();
                System.out.println(serverReply);
                if (GAME_STARTED_MSG.equals(serverReply)) {
                    clientResponseThread.interrupt();
                    clientResponseThread.join();
                    Game game = new Game(reader, writer);
                    Thread gameThread = new Thread(game);
                    gameThread.start();
                    gameThread.join();
                    clientResponseThread = new Thread(clientResponseHandler);
                    clientResponseThread.start();
                } else if (DISCONNECTED_FROM_THE_SERVER_MSG.equals(serverReply)) {
                    clientResponseThread.interrupt();
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
