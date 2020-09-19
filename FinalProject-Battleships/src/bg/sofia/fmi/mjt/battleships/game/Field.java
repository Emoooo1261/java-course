package bg.sofia.fmi.mjt.battleships.game;

/**
 * Simple class storing FieldEnum variable.
 */
public class Field {
    private FieldEnum field;

    public Field() {
        field = FieldEnum.EMPTY_FIELD;
    }

    public FieldEnum getField() {
        return field;
    }

    public void setField(final FieldEnum field) {
        this.field = field;
    }
}
