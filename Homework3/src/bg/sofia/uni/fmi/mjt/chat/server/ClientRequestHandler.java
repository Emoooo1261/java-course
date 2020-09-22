package bg.sofia.uni.fmi.mjt.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientRequestHandler implements Runnable {
    private static final int MAX_NAME_LENGTH = 13;
    private static final int MIN_NAME_LENGTH = 2;

    private Socket socket;
    private String clientName;
    private Map<String, Socket> clientsMap;

    public ClientRequestHandler(Socket socket, Map<String, Socket> clientsMap) {
        this.socket = socket;
        this.clientsMap = clientsMap;
        this.clientName = null;
    }

    private void setClientUsername(PrintWriter writer, BufferedReader reader) throws IOException {
        writer.println("Enter your username: ");
        String name = reader.readLine();
        boolean isTakenName = false;
        while (name.length() > MAX_NAME_LENGTH || name.length() < MIN_NAME_LENGTH || (isTakenName = clientsMap.containsKey(name))) {
            if (isTakenName) {
                writer.println("This username is not available, enter another one: ");
            } else {
                writer.println("Enter valid username (between "
                        + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters): ");
            }
            name = reader.readLine();
        }
        clientName = name;
        clientsMap.put(name, socket);
        writer.println("You set your username to [" + clientName + "]");
    }

    private void sendMessageCommand(PrintWriter writer, String input) throws IOException {
        String[] splitInput = input.split(" ", 2);
        if (splitInput.length == 2 && !splitInput[1].isBlank()) {
            if (clientsMap.containsKey(splitInput[0])) {
                Socket receiver = clientsMap.get(splitInput[0]);
                PrintWriter receiverWriter = new PrintWriter(receiver.getOutputStream(), true);
                receiverWriter.println("Message from [" + clientName + "]: " + splitInput[1]);
            } else {
                writer.println("There is no user with this name.");
            }
        } else {
            writer.println("Invalid command!");
        }
    }

    private void sendAllMessageCommand(PrintWriter writer, String input) throws IOException {
        if (!input.isBlank()) {
            if (clientsMap.size() > 1) {
                for (Map.Entry<String, Socket> client : clientsMap.entrySet()) {
                    if (!clientName.equals(client.getKey())) {
                        PrintWriter receiverWriter = new PrintWriter(client.getValue().getOutputStream(), true);
                        receiverWriter.println("Message from [" + clientName + "] to all: " + input);
                    }
                }
            } else {
                writer.println("You are the only user online");
            }
        } else {
            writer.println("Invalid message");
        }
    }

    private void listUsersCommand(PrintWriter writer) {
        writer.println("List of users:");
        int index = 1;
        for (String username : clientsMap.keySet()) {
            writer.println(index++ + ". " + username);
        }
    }

    private void disconnectCommand(PrintWriter writer) {
        clientsMap.remove(clientName);
        writer.println("QUIT");
    }

    private void listCommandsCommand(PrintWriter writer) {
        writer.println("List of commands:\n"
            + "send <username> <message>\n"
                + "send-all <message>\n"
                + "list-users\n"
                + "list-commands\n"
                + "disconnect");
    }

    private void executeCommand(String input, PrintWriter writer) throws IOException {
        if (!input.isBlank()) {
            String[] splitInput = input.split(" ", 2);
            if (splitInput.length == 1) {
                switch (splitInput[0]) {
                    case "list-commands":
                        listCommandsCommand(writer);
                        return;
                    case "list-users":
                        listUsersCommand(writer);
                        return;
                    case "disconnect":
                        disconnectCommand(writer);
                        return;
                }
            } else {
                if (!splitInput[1].isBlank()) {
                    switch (splitInput[0]) {
                        case "send":
                            sendMessageCommand(writer, splitInput[1]);
                            return;
                        case "send-all":
                            sendAllMessageCommand(writer, splitInput[1]);
                            return;
                    }
                }
            }
        }
        writer.println("Invalid command!");
    }

    @Override
    public void run() {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            setClientUsername(writer, reader);
            String input;
            while ((input = reader.readLine()) != null) { //read message from client
                executeCommand(input, writer);
                //System.out.println("Message received from client: " + input);
                //writer.println("Echo> " + input); //send message to client
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
