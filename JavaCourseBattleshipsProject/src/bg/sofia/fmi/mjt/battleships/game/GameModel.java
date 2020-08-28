package bg.sofia.fmi.mjt.battleships.game;

public class GameModel {
    private String gameName;
    private String creator;
    private String status;
    private String numberOfPlayers;

    public GameModel(String gameName, String creator, String status, String numberOfPlayers) {
        this.gameName = gameName;
        this.creator = creator;
        this.status = status;
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public String getCreator() {
        return creator;
    }

    public String getStatus() {
        return status;
    }

    public String getNumberOfPlayers() {
        return numberOfPlayers;
    }

    @Override
    public String toString() {
        return "Game name: " + gameName + "Creator: " + creator + "Status: " + status + "Number of players: " + numberOfPlayers;
    }
}
