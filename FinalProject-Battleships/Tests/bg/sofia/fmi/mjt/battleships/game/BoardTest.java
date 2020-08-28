package bg.sofia.fmi.mjt.battleships.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {
    Board board = new Board();

    @Test
    public void convertStringCoordinatesToIntTest() {
        CoordinatesPair numCoords = new CoordinatesPair();
        String coords = "A10";
        assertTrue(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        assertEquals("NUM COORDS 0, 9", numCoords, new CoordinatesPair(0,9));
        coords = "A5";
        assertTrue(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        assertEquals("NUM COORDS 0, 4", numCoords, new CoordinatesPair(0,4));
        coords = "J1";
        assertTrue(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        assertEquals("NUM COORDS 9, 0", numCoords, new CoordinatesPair(9,0));
        coords = "C10";
        assertTrue(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        assertEquals("NUM COORDS 2, 9", numCoords, new CoordinatesPair(2,9));
        coords = "J10";
        assertTrue(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        assertEquals("NUM COORDS 9, 9", numCoords, new CoordinatesPair(9,9));
        coords = "a1";
        assertTrue(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        assertEquals("NUM COORDS 0, 0", numCoords, new CoordinatesPair(0,0));
        coords = "A11";
        assertFalse(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        coords = "K11";
        assertFalse(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        coords = "k1";
        assertFalse(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
        coords = "11";
        assertFalse(coords ,board.convertStringCoordinatesToCoordinatesPair(coords, numCoords));
    }

    @Test
    public void checkValidShipCoordinatesTest() {
        int size = board.SHIPS_NUMBER + 1;
        CoordinatesPair start = new CoordinatesPair(board.BOARD_SIZE - 1 - size,board.BOARD_SIZE - 1);
        CoordinatesPair end = new CoordinatesPair(board.BOARD_SIZE - 1, board.BOARD_SIZE - 1);
        assertTrue(board.checkValidShipCoordinates(size, start, end));
        size = 3;
        start = new CoordinatesPair(board.BOARD_SIZE - 1,board.BOARD_SIZE - 1);
        end = new CoordinatesPair(board.BOARD_SIZE - 1 - size,board.BOARD_SIZE - 1);
        assertTrue(board.checkValidShipCoordinates(size, start, end));
        size = 4;
        start = new CoordinatesPair(1, 1);
        end = new CoordinatesPair(5,1);
        assertTrue(board.checkValidShipCoordinates(size, start, end));
        size = 2;
        start = new CoordinatesPair(board.BOARD_SIZE + 5,board.BOARD_SIZE + 5);
        end = new CoordinatesPair(-1,-2);
        assertFalse(board.checkValidShipCoordinates(size, start, end));
        size = board.BOARD_SIZE;
        start = new CoordinatesPair(0,board.BOARD_SIZE);
        end = new CoordinatesPair(0,0);
        assertFalse(board.checkValidShipCoordinates(size, start, end));
        size = 2;
        start = new CoordinatesPair(board.BOARD_SIZE - 1,board.BOARD_SIZE - 1);
        end = new CoordinatesPair(-1,board.BOARD_SIZE - 1);
        assertFalse(board.checkValidShipCoordinates(size, start, end));
        size = 1;
        start = new CoordinatesPair(0,0);
        end = new CoordinatesPair(0,1);
        assertFalse(board.checkValidShipCoordinates(size, start, end));
    }

    @Test
    public void setShipTest() throws Exception {
        int size = board.SHIPS_NUMBER + 1;
        String start = "A1";
        String end = "A" + (size + 1);
        assertTrue(board.setShip(size, start, end));
        start = "A" + (size + 1);
        end = "A" + (size + 1) + 2;
        assertFalse(board.setShip(2, start, end));
        start = "A" + (size + 2);
        end = "A" + ((size + 2) + 2);
        assertTrue(board.setShip(2, start, end));
        start = "B1";
        end = "B" + (board.SHIPS_NUMBER + 1);
        assertTrue(board.setShip((board.SHIPS_NUMBER), start, end));
    }
}
