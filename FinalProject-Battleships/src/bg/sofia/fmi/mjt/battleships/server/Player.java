package bg.sofia.fmi.mjt.battleships.server;

import java.net.Socket;

/**
 * Class for storing all the data of a user.
 */
public class Player {
    public static final int MAX_PLAYER_NAME_LENGTH = 9;

    private String playerName;
    private Socket socket;
    private boolean isPlayerReady;

    public Player(String playerName, Socket socket) {
        this.playerName = playerName;
        this.socket = socket;
        isPlayerReady = false;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerReady(boolean isPlayerReady) {
        this.isPlayerReady = isPlayerReady;
    }

    public Boolean getIsPlayerReady() {
        return isPlayerReady;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Socket getSocket() {
        return socket;
    }


}
