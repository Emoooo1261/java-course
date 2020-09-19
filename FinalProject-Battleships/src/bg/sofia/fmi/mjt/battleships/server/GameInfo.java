package bg.sofia.fmi.mjt.battleships.server;

import java.io.PrintWriter;

/**
 * Stores all the info needed for starting a game.
 */
public class GameInfo {
    public static final int MAX_GAME_NAME_LENGTH = 11;

    private String gameName;
    private Player host;
    private Player guest;
    private GameStatusEnum gameStatus;
    private int numberOfPlayers;

    public GameInfo(final String gameName, final Player host, final GameStatusEnum gameStatus) {
        this.gameName = gameName;
        this.host = host;
        this.gameStatus = gameStatus;
        this.numberOfPlayers = 1;
        this.guest = null;
    }

    public void setGuest(final Player guest) {
        this.guest = guest;
        if (guest != null) {
            numberOfPlayers += 1;
        } else {
            numberOfPlayers -= 1;
        }
    }

    public void setHost(final Player host) {
        this.host = host;
    }

    public void setGameStatus(final GameStatusEnum gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getGameName() {
        return gameName;
    }

    public Player getHost() {
        return host;
    }

    public Player getGuest() {
        return guest;
    }

    public GameStatusEnum getGameStatus() {
        return gameStatus;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    //TODO fix the constants
    public void printGame(PrintWriter out) {
        String gameNameStr = String.format("%1$-" + MAX_GAME_NAME_LENGTH + "s", gameName);
        String hostNameStr = String.format("%1$-" + Player.MAX_PLAYER_NAME_LENGTH + "s", host.getPlayerName());
        String gameStatusStr = String.format("%1$-" + GameStatusEnum.IN_PROGRESS_GAME.getValue().length() + "s", gameStatus.getValue());
        String playersNum1Str = numberOfPlayers + "/2";
        String playersNum2Str = String.format("%1$7s", playersNum1Str);
        out.println("| " + gameNameStr + " | " + hostNameStr + " | " + gameStatusStr + " | " + playersNum2Str + " |");
    }
}
