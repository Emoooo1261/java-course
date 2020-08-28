package bg.sofia.uni.fmi.mjt.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

    private Socket socket;
    private String name;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private boolean isLoggedIn;

    public ClientHandler(Socket socket, String name, DataInputStream dis, DataOutputStream dos) {
        this.name = name;
        this.socket = socket;
        this.dis = dis;
        this.dos = dos;
        isLoggedIn = true;
    }

    public void setName(String name) {
        if(name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    @Override
    public void run() {
        String received;

        while(true) {
            try {
                received = dis.readUTF();
                System.out.println("==> [" + name + "]: " + received);

                int indexOfFirstWhiteSpace = received.indexOf(" ");

                if (received.equals("disconnect")) {
                    isLoggedIn = false;
                    socket.close();
                    break;
                } else if (received.equals("list-users")) {
                    for (ClientHandler ch : ChatServer.clients) {
                        if (name.equals(ch.name)) {
                            dos.writeUTF("[You] " + ch.name);
                        } else {
                            dos.writeUTF(ch.name);
                        }
                    }
                } else if (indexOfFirstWhiteSpace != -1) { //There are multiple words
                        String firstWord = received.substring(0, indexOfFirstWhiteSpace);
                        if (firstWord.equals("nick")) {
                            String name = received.substring(indexOfFirstWhiteSpace + 1);
                            System.out.println("[" + this.name + "]" + " Changed its name to [" + name + "]");
                            setName(name);
                        } else if (firstWord.equals("send")) {
                            String[] str = received.split(" ", 3); //POMISLI AKO V IMETO IMA " " KAKTO V MOMENTA E USER 1!!!!!!!!
                            for (ClientHandler ch : ChatServer.clients) {
                                // if the recipient is found, write on its
                                // output stream
                                if (ch.name.equals(str[1]) && ch.isLoggedIn) {
                                    ch.dos.writeUTF("[" + this.name + "]: " + str[2]);
                                    break;
                                }
                            }
                        } else if (firstWord.equals("send-all")) {
                            String[] str = received.split(" ", 2);
                            for (ClientHandler ch : ChatServer.clients) {
                                if (ch.isLoggedIn) { //Shouldn't send to himself, check if name is taken when there is request to set it
                                    ch.dos.writeUTF("[" + this.name + "]" + "[Sent all]: " + str[1]);
                                }
                            }
                            break;
                        }
                    } else {
                        dos.writeUTF("Invalid input!\nCoammands are:\n");
                    }


//                // break the string into message and recipient part
//                StringTokenizer st = new StringTokenizer(received, "#");
//                String MsgToSend = st.nextToken();
//                String recipient = st.nextToken();
//
//                // search for the recipient in the connected devices list.
//                // ar is the vector storing client of active users
//                for (ClientHandler ch : ChatServer.clients) {
//                    // if the recipient is found, write on its
//                    // output stream
//                    if (ch.name.equals(recipient) && ch.isLoggedIn) {
//                        ch.dos.writeUTF(this.name + " : " + MsgToSend);
//                        break;
//                    }
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
