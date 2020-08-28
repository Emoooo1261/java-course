package bg.sofia.fmi.mjt.battleships.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Game implements Runnable {
    private BufferedReader reader;
    private PrintWriter writer;

    public Game(final BufferedReader reader, final PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public boolean coordinatesInputValidator(final String inputCoordinates, String[] checkedCoordinates) {
        checkedCoordinates = inputCoordinates.split(" ", 2);
        for (int i = 0; i < checkedCoordinates.length; ++i) {
            if (checkedCoordinates[i].length() > Board.MAX_COORDINATES_INPUT_STRING_LENGTH
                    || checkedCoordinates[i].length() < Board.MIN_COORDINATES_INPUT_STRING_LENGTH) {
                return false;
            }
        }
        return true;
    }

    public void inputShips(Board board) {
        Scanner scanner = new Scanner(System.in);
        board.printBoard();
        String coordinatesString;
        do {
            coordinatesString = scanner.nextLine();
            String[] checkedCoordinates = new String[2];
            if (coordinatesInputValidator(coordinatesString, checkedCoordinates)) {
                if (board.setShip(Board.SHIPS_NUMBER - board.getShipIndex() + 1, checkedCoordinates[0], checkedCoordinates[1])) {
                    board.printBoard();
                }
            } else {
                System.out.println("Enter valid ship coordinates!");
            }

        } while (Board.SHIPS_NUMBER < board.getShipIndex());
    }

//    public boolean coordinatesInputValidator(final String coordinates) {
//        if(coordinates.length() > Board.MAX_COORDINATES_INPUT_STRING_LENGTH
//                || coordinates.length() < Board.MIN_COORDINATES_INPUT_STRING_LENGTH) {
//            return false;
//        }
//        String[] splitCoordinates = coordinates.split("", 2);
//        if (splitCoordinates[0].length() != 1) {
//            return false;
//        }
//        char xCoord = splitCoordinates[0].charAt(0);
//        if ((xCoord < 'a' || xCoord >= 'a' + Board.BOARD_SIZE)
//                && (xCoord < 'A' || xCoord >= 'A' + Board.BOARD_SIZE) {
//            return false;
//        }
//        int yCoord = Integer.parseInt(splitCoordinates[1]);
//        if (yCoord < 1 || yCoord > Board.BOARD_SIZE) {
//            return false;
//        }
//        return true;
//    }

    public void gameplay(Board player1) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String reply;
        Board player2 = new Board();

        while (true) {
            System.out.println("\tYOUR BOARD");
            player1.printBoard();
            System.out.println("\tENEMY BOARD");
            String command;
            CoordinatesPair numCoordinates = new CoordinatesPair();
            boolean isValidCmd = false;
            do {
                System.out.println("Enter coordinates you want to hit: ");
                command = scanner.nextLine();
                if(player1.convertStringCoordinatesToCoordinatesPair(command, numCoordinates)) {
                    writer.println(command);
                    isValidCmd = true;
                } else {
                    System.out.println("Invalid coordinates!");
                }
            } while (isValidCmd);
            reply = reader.readLine();
            System.out.println("Your opponent hit: " + reply);
            CoordinatesPair enemyNumCoordinatesHit = new CoordinatesPair();
            player1.convertStringCoordinatesToCoordinatesPair(reply, enemyNumCoordinatesHit);
            player1.updateBoard(enemyNumCoordinatesHit);
            player2.updateBoard(numCoordinates);
        }
    }


    @Override
    public void run() {
        Board player1 = new Board();
        inputShips(player1);
        System.out.println("Waiting for your opponent to set their ships.\n");
        writer.println("ready");
        try {
            reader.readLine();
            gameplay(player1);
        } catch (IOException e) {
            System.out.println("Problem with the connection occurred. Please restart the program.");
        }
    }
}
