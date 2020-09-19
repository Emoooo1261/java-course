package bg.sofia.fmi.mjt.battleships.game;

/**
 * All the possible variables for a field on the board.
 * And their respective string equivalent.
 */
public enum FieldEnum {
    EMPTY_FIELD("_"),
    FIELD_WITH_SHIP("*"),
    HIT_EMPTY_FIELD("O"),
    HIT_FIELD_WITH_SHIP("X");

    private String value;

    FieldEnum(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
