package bg.sofia.fmi.mjt.battleships.client;

import java.io.PrintWriter;
import java.util.Scanner;

public class ClientRequestSender implements Runnable {
    private PrintWriter writer;

    ClientRequestSender(final PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void run() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            String message = null;
            if (scanner.hasNextLine()) {
                message = scanner.nextLine();
            }
            if (Thread.interrupted()) {
                break;
            }
            if (message.equals(null)) {
                writer.println(message);
            }
        }
    }
}