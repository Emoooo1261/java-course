package bg.sofia.fmi.mjt.battleships.game;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * The main logic for the game action.
 * Firstly communicates with the Client for the input of their ships.
 * Then it sends message READY to the server and waits for an answer from the opponent.
 * After both players put their ships on their boards, it starts sending
 * coordinates of fields the players want to hit and gets answer if a the field has a
 * ship on it. When one of the players hits all of the ships of their opponent the game ends.
 */
public class Game implements Runnable {
    public static final String YOUR_BOARD = "\tYOUR BOARD";
    public static final String ENEMY_BOARD = "\tENEMY BOARD";
    public static final String INVALID_COORDINATES = "Invalid coordinates!";
    public static final String READY = "READY";
    public static final String THIS_IS_THE_WINNER = "THIS IS THE WINNER!";
    public static final String CONTINUE = "CONTINUE";
    public static final String THIS_IS_THE_LOSER = "THIS IS THE LOSER!";
    public static final String HIT = "HIT";
    public static final String MISS = "MISS";
    private BufferedReader reader;
    private PrintWriter writer;

    public Game(final BufferedReader reader, final PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void inputShips(Board board) {
        Scanner scanner = new Scanner(System.in);
        board.printBoard();
        String startCoordinatesString;
        String endCoordinatesString;
        do {
            System.out.println("Enter starting and ending coordinates for ship with size " + (Board.BIGGEST_SHIP_SIZE - board.getShipIndex()));
            startCoordinatesString = scanner.nextLine();
            endCoordinatesString = scanner.nextLine();
            if (board.setShip(Board.BIGGEST_SHIP_SIZE - board.getShipIndex(), startCoordinatesString, endCoordinatesString)) {
                board.printBoard();
            } else {
                System.out.println("Enter valid ship coordinates! For ship with size " + (Board.BIGGEST_SHIP_SIZE - board.getShipIndex()));
            }
        } while (Board.MAX_NUMBER_OF_SHIPS > board.getShipIndex());
    }

    //TODO fix hit already hit position, also maybe if you hit ship it should be your turn again
    public void gameplay(Board player1) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String reply;
        Board player2 = new Board();
        while (true) {
            System.out.println(YOUR_BOARD);
            player1.printBoard();
            System.out.println("- - - - - - - - - - - -");
            System.out.println(ENEMY_BOARD);
            player2.printBoard();
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
                    System.out.println(INVALID_COORDINATES);
                }
            } while (!isValidCmd);
            reply = reader.readLine();
            System.out.println("Your opponent hit: " + reply);
            CoordinatesPair enemyNumCoordinatesHit = new CoordinatesPair();
            player1.convertStringCoordinatesToCoordinatesPair(reply, enemyNumCoordinatesHit);
            if (player1.updateBoard(enemyNumCoordinatesHit)) {
                writer.println(HIT);
            } else {
                writer.println(MISS);
            }
            reply = reader.readLine();
            if (reply.equals(HIT)) {
                player2.setHitShipField(numCoordinates);
            } else { //MISS
                player2.setHitEmptyField(numCoordinates);
            }
            if (player1.areAllShipsSunken()) {
                System.out.println("You lose!");
                writer.println(THIS_IS_THE_LOSER);
                break;
            } else {
                writer.println(CONTINUE);
            }
            reply = reader.readLine();
            if (reply.equals("You sank all of your opponent's ships! You win!")) {
                writer.println(THIS_IS_THE_WINNER);
                System.out.println(reply);
                //player2.setHitShipField(numCoordinates);
                break;
            }
        }
    }
    @Override
    public void run() {
        Board player1 = new Board();
        inputShips(player1);
        System.out.println("Waiting for your opponent to set their ships...\n");
        writer.println(READY);
        try {
            reader.readLine();
            gameplay(player1);
        } catch (Exception e) {
            System.out.println("Problem with the connection occurred. Please restart the program.");
        }
    }
}
