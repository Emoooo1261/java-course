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
//    private static final int SERVER_PORT = 4444;
//
//    public static void main(String[] args) throws IOException {
//
//        Scanner scn = new Scanner(System.in);
//
//        // getting localhost ip
//        InetAddress ip = InetAddress.getByName("localhost");
//
//        // establish the connection
//        Socket s = new Socket(ip, SERVER_PORT);
//
//        // obtaining input and out streams
//        DataInputStream dis = new DataInputStream(s.getInputStream());
//        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//
//        // sendMessage thread
//        Thread sendMessage = new Thread(new Runnable()
//        {
//            @Override
//            public void run() {
//                while (true) {
//
//                    // read the message to deliver.
//                    String msg = scn.nextLine();
//
//                    try {
//                        // write on the output stream
//                        dos.writeUTF(msg);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        // readMessage thread
//        Thread readMessage = new Thread(new Runnable()
//        {
//            @Override
//            public void run() {
//
//                while (true) {
//                    try {
//                        // read the message sent to this client
//                        String msg = dis.readUTF();
//                        System.out.println(msg);
//                    } catch (IOException e) {
//
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//
//        sendMessage.start();
//        readMessage.start();


//        try (Socket socket = new Socket("localhost", SERVER_PORT);
////             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // autoflush on
////             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             DataInputStream dis = new DataInputStream(socket.getInputStream());
//             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//             Scanner scanner = new Scanner(System.in)) {
//
//            // sendMessage thread
//            Thread sendMessage = new Thread(() -> {
//                while (true) {
//
//                    // read the message to deliver.
//                    String msg = scanner.nextLine();
//                    try {
//                        // write on the output stream
//                        dos.writeUTF(msg);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            // readMessage thread
//            Thread readMessage = new Thread(() -> {
//
//                while (true) {
//                    try {
//                        // read the message sent to this client
//                        String msg = dis.readUTF();
//                        System.out.println(msg);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            sendMessage.start();
//            readMessage.start();

//            System.out.println("Connected to the server.");
//
//            while (true) {
//                System.out.print("Enter message: ");
//                String message = scanner.nextLine(); // read a line from the console
//
//                if ("disconnect".equals(message)) {
//                    break;
//                }
//
//                System.out.println("Sending message <" + message + "> to the server...");
//
//                writer.println(message); // send the message to the server
//
//                String reply = reader.readLine(); // read the response from the server
//                System.out.println("The server replied <" + reply + ">");
//            }
//
//    }
//}
