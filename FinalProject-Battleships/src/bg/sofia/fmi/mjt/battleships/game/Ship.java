package bg.sofia.fmi.mjt.battleships.game;

public class Ship {
    private int size;
    private CoordinatesPair startCoordinates;
    private CoordinatesPair endCoordinates;

    public Ship(int size, CoordinatesPair startCoordinates, CoordinatesPair endCoordinates) {
        setSize(size);
        setStartCoordinates(startCoordinates);
        setEndCoordinates(endCoordinates);
    }

    public int getSize() {
        return size;
    }

    public CoordinatesPair getStartCoordinates() {
        return startCoordinates;
    }

    public CoordinatesPair getEndCoordinates() {
        return endCoordinates;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public void setStartCoordinates(final CoordinatesPair startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public void setEndCoordinates(final CoordinatesPair endCoordinates) {
        this.endCoordinates = endCoordinates;
    }
}
