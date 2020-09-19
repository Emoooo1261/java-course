package bg.sofia.fmi.mjt.battleships.client;

/**
 * The main Client class,
 * starts serverResponseHandler,
 * which listens for input from the Server
 */
public class Client {
    public static void main(String[] args) {
        ServerResponseHandler serverResponseHandler = new ServerResponseHandler();
        Thread thread = new Thread(serverResponseHandler);
        thread.start();
    }
}
