package bg.sofia.fmi.mjt.battleships.server;

/**
 * All game status possibilities.
 * And their String equivalents.
 */
public enum GameStatusEnum {
    PENDING_GAME("Pending game"),
    IN_PROGRESS_GAME("Game in progress"),
    FINISHED_GAME("Finished game");

    private String value;

    GameStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
