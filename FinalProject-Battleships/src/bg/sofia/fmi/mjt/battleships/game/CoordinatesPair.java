package bg.sofia.fmi.mjt.battleships.game;

import java.util.Objects;

/**
 * Simple class for storing the two integer coordinates (x and y).
 */
public class CoordinatesPair {
    private int xCoord;
    private int yCoord;

    CoordinatesPair() {
        setXCoord(0);
        setYCoord(0);
    }

    CoordinatesPair(final int xCoord, final int yCoord) {
        setXCoord(xCoord);
        setYCoord(yCoord);
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setXCoord(final int xCoord) {
        this.xCoord = xCoord;
    }

    public void setYCoord(final int yCoord) {
        this.yCoord = yCoord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoordinatesPair that = (CoordinatesPair) o;
        return xCoord == that.xCoord
                && yCoord == that.yCoord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoord, yCoord);
    }
}
