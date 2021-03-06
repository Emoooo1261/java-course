package bg.sofia.fmi.mjt.battleships.game;

import java.util.ArrayList;

/**
 * Represents the board with the ships for the game.
 * Also validates placement for the ships on the board
 * and prints the board with the respective fields
 * (hit field, field with ship, empty field or hit field with ship).
 */
public class Board {
    public static final int BOARD_SIZE = 10;
    public static final int BIGGEST_SHIP_SIZE = 5;
    public static final int SMALLEST_SHIP_SIZE = 2;
    public static final int MAX_NUMBER_OF_SHIPS = BIGGEST_SHIP_SIZE - SMALLEST_SHIP_SIZE + 1;
    public static final int MAX_COORDINATES_INPUT_STRING_LENGTH = 3;
    public static final int MIN_COORDINATES_INPUT_STRING_LENGTH = 2;
    public static final String X = "   1 2 3 4 5 6 7 8 9 10";
    public static final String X1 = "   _ _ _ _ _ _ _ _ _ _";
    public static final String REGEX_ONLY_INTEGER = "-?(0|[1-9]\\d*)";

    private ArrayList<ArrayList<Field>> gameBoard;
    private Ship[] ships;
    private int shipIndex = 0;

    public Board() {
        gameBoard = new ArrayList<>(BOARD_SIZE);
        ships = new Ship[MAX_NUMBER_OF_SHIPS];
        for (int i = 0; i < BOARD_SIZE; ++i) {
            gameBoard.add(new ArrayList<>());
            for (int j = 0; j < BOARD_SIZE; ++j) {
                gameBoard.get(i).add(new Field());
            }
        }
    }

    public ArrayList<ArrayList<Field>> getGameBoard() {
        return gameBoard;
    }

    public Ship[] getShips() {
        return ships;
    }

    public int getShipIndex() {
        return shipIndex;
    }

    public boolean areAllShipsSunken() {
        for (Ship ship : ships) {
            if (!ship.getIsShipSunk()) {
                return false;
            }
        }
        return true;
    }

    public boolean updateBoard(final CoordinatesPair coordinates) throws Exception {
        boolean hit = false;
        FieldEnum hitField = gameBoard.get(coordinates.getXCoord()).get(coordinates.getYCoord()).getField();
        if (hitField == (FieldEnum.FIELD_WITH_SHIP)) {
            gameBoard.get(coordinates.getXCoord()).get(coordinates.getYCoord()).setField(FieldEnum.HIT_FIELD_WITH_SHIP);
            for (int i = 0; i < shipIndex; ++i) {
                if (!ships[i].getIsShipSunk()) {
                    if (ships[i].areTheCoordinatesFromThisShip(coordinates)) {
                        ships[i].hitShip();
                        hit = true;
                    }
                }
            }
        } else if (hitField== FieldEnum.EMPTY_FIELD) {
            gameBoard.get(coordinates.getXCoord()).get(coordinates.getYCoord()).setField(FieldEnum.HIT_EMPTY_FIELD);
        } //else you already have hit this field
        return hit;
    }

    public void setHitShipField(final CoordinatesPair coordinates) {
        gameBoard.get(coordinates.getXCoord()).get(coordinates.getYCoord()).setField(FieldEnum.HIT_FIELD_WITH_SHIP);
    }

    public void setHitEmptyField(final CoordinatesPair coordinates) {
        gameBoard.get(coordinates.getXCoord()).get(coordinates.getYCoord()).setField(FieldEnum.HIT_EMPTY_FIELD);
    }

    public void printBoard() {
        System.out.println(X);
        System.out.println(X1);
        char letter = 'A';
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.out.print(letter++ + " |");
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.print(gameBoard.get(i).get(j).getField().getValue() + "|");

            }
            System.out.println();
        }
    }

    public boolean convertStringCoordinatesToCoordinatesPair(
            final String coords, final CoordinatesPair numCoords) {
        //A10 are the longest coords, A1 shortest
        if (coords.length() > MAX_COORDINATES_INPUT_STRING_LENGTH
                || coords.length() < MIN_COORDINATES_INPUT_STRING_LENGTH) {
            return false;
        }
        //The first symbol should be A-J and after it, number 1-10
        String[] splitCoords = coords.split("", 2);
        char xCoord = splitCoords[0].charAt(0);
        int xNumCoord;
        if (xCoord >= 'A' && xCoord < 'A' + BOARD_SIZE) {
            xNumCoord = xCoord - 'A';
        } else if (xCoord >= 'a' && xCoord < 'a' + BOARD_SIZE) {
            xNumCoord = xCoord - 'a';
        } else {
            return false;
        }
        if (!splitCoords[1].matches(REGEX_ONLY_INTEGER)) {
            return false;
        }
        int yNumCoord = Integer.parseInt(splitCoords[1]);
        if (yNumCoord < 1 || yNumCoord > BOARD_SIZE) {
            return false;
        }
        yNumCoord -= 1;
        numCoords.setXCoord(xNumCoord);
        numCoords.setYCoord(yNumCoord);
        return true;
    }

    public boolean checkValidShipCoordinates(
            final int size, final CoordinatesPair startCoords, final CoordinatesPair endCoords) {
        return size <= BIGGEST_SHIP_SIZE && size > 1
                && startCoords.getXCoord() >= 0 && startCoords.getYCoord() >= 0
                && endCoords.getXCoord() >= 0 && endCoords.getYCoord() >= 0
                && startCoords.getXCoord() < BOARD_SIZE && startCoords.getYCoord() < BOARD_SIZE
                && endCoords.getXCoord() < BOARD_SIZE && endCoords.getYCoord() < BOARD_SIZE
                && ((startCoords.getXCoord() == endCoords.getXCoord()
                && (endCoords.getYCoord() - startCoords.getYCoord() == size
                || startCoords.getYCoord() - endCoords.getYCoord() == size))
                || (startCoords.getYCoord() == endCoords.getYCoord()
                && (endCoords.getXCoord() - startCoords.getXCoord() == size
                || startCoords.getXCoord() - endCoords.getXCoord() == size)));
    }

    public boolean checkCollision(
            final int size, final CoordinatesPair startCoords1, final CoordinatesPair endCoords1) {
        if (startCoords1.getXCoord() == endCoords1.getXCoord()) {
            int x = startCoords1.getXCoord();
            int y;
            if (startCoords1.getYCoord() > endCoords1.getYCoord()) {
                y = endCoords1.getYCoord();
            } else {
                y = startCoords1.getYCoord();
            }
            for (int i = 0; i < size; ++i) {
                if (gameBoard.get(x).get(y + i).getField().equals(FieldEnum.FIELD_WITH_SHIP)) {
                    return false;
                }
            }
        } else {
            int x = Math.min(endCoords1.getXCoord(), startCoords1.getXCoord());
            int y = startCoords1.getYCoord();
            for (int i = 0; i < size; ++i) {
                if (gameBoard.get(x).get(y + i).getField().equals(FieldEnum.FIELD_WITH_SHIP)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void placeShipOnGameBoard(final Ship ship) {
        if (ship.getStartCoordinates().getXCoord() == ship.getEndCoordinates().getXCoord()) {
            int x = ship.getStartCoordinates().getXCoord();
            int y = Math.min(ship.getStartCoordinates().getYCoord(), ship.getEndCoordinates().getYCoord());
            for (int i = 0; i < ship.getSize(); ++i) {
                gameBoard.get(x).get(y + i).setField(FieldEnum.FIELD_WITH_SHIP);
            }
        } else {
            int x = Math.min(ship.getEndCoordinates().getXCoord(), ship.getStartCoordinates().getXCoord());
            int y = ship.getStartCoordinates().getYCoord();
            for (int i = 0; i < ship.getSize(); ++i) {
                gameBoard.get(x + i).get(y).setField(FieldEnum.FIELD_WITH_SHIP);
            }
        }
    }

    public boolean setShip(
            final int shipSize, final String startCoords, final String endCoords) {
        CoordinatesPair start = new CoordinatesPair();
        CoordinatesPair end = new CoordinatesPair();
        if (!convertStringCoordinatesToCoordinatesPair(startCoords, start)
                || !convertStringCoordinatesToCoordinatesPair(endCoords, end)) {
            return false;
        }
        if (!checkValidShipCoordinates(shipSize, start, end)) {
            return false;
        }
        if (!checkCollision(shipSize, start, end)) {
            return false;
        }
        ships[shipIndex] = new Ship(shipSize, start, end);
        placeShipOnGameBoard(ships[shipIndex++]);
        return true;
    }
}