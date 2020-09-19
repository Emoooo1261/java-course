package bg.sofia.fmi.mjt.battleships.game;

/**
 * Class representing individual ship, with its size and starting and ending coordinates.
 * Also the ship's hit points or how many not hit parts it has.
 * And boolean if the ship is fully sunk.
 */
public class Ship {
    private int size;
    private CoordinatesPair startCoordinates;
    private CoordinatesPair endCoordinates;
    private int hitPoints;
    private boolean isShipSunk = false;

    public Ship(int size, CoordinatesPair startCoordinates, CoordinatesPair endCoordinates) {
        setSize(size);
        hitPoints = size;
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

    public boolean getIsShipSunk() {
        return isShipSunk;
    }

    public void hitShip() throws Exception {
        if (isShipSunk) {
            throw new Exception("Tried to hit already sunken ship");
        }
        --hitPoints;
        if (hitPoints == 0) {
            isShipSunk = true;
        }
    }

    public boolean areTheCoordinatesFromThisShip(CoordinatesPair coords) {
        if (startCoordinates.getXCoord() == endCoordinates.getXCoord() && coords.getXCoord() == startCoordinates.getXCoord()) {
            return (coords.getYCoord() >= startCoordinates.getYCoord() && coords.getYCoord() <= endCoordinates.getYCoord())
                    || coords.getYCoord() <= startCoordinates.getYCoord() && coords.getYCoord() >= endCoordinates.getYCoord();
        } else if (startCoordinates.getYCoord() == endCoordinates.getYCoord() && coords.getYCoord() == startCoordinates.getYCoord()) {
            return (coords.getXCoord() >= startCoordinates.getXCoord() && coords.getXCoord() <= endCoordinates.getXCoord())
                    || coords.getXCoord() <= startCoordinates.getXCoord() && coords.getXCoord() >= endCoordinates.getXCoord();
        } else {
            return false;
        }
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
