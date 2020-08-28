package bg.sofia.fmi.mjt.battleships.client;

public class Client {
    public static void main(String[] args) {
        ClientResponseListener clientResponseListener = new ClientResponseListener();
        Thread thread = new Thread(clientResponseListener);
        thread.start();
    }
}