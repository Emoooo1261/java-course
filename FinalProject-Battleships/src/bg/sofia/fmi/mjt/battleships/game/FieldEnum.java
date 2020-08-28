package bg.sofia.fmi.mjt.battleships.game;

public enum FieldEnum {
    EMPTY_FIELD("_"),
    FIELD_WITH_SHIP("*"),
    HIT_EMPTY_FIELD("O"),
    HIT_FIELD_WITH_SHIP("X");

    private String value;

    FieldEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
