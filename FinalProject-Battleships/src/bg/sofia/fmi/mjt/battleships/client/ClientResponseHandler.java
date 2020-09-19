package bg.sofia.fmi.mjt.battleships.client;

import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Listens for input from the Client.
 * And sends the input to the Server.
 */
public class ClientResponseHandler implements Runnable {
    private PrintWriter writer;

    ClientResponseHandler(final PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String clientInput;
        do {
            if (scanner.hasNextLine()) {
                clientInput = scanner.nextLine();
                writer.println(clientInput);
            }
        } while (!Thread.interrupted());
    }
}
