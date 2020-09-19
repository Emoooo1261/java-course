package bg.sofia.fmi.mjt.battleships.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Represents the logic of the Server.
 * Reads inputs from the Client and answers back to them.
 * Executes command based on the inputs from the client.
 */
public class ClientHandler implements Runnable {
    public static final String AVAILABLE_COMMANDS = "Available commands:\n"
            + "create-game <game-name>\n"
            + "join-game [<game-name>]\n"
            + "saved-games\n"
            + "save-game\n"
            + "load-game <game-name>\n"
            + "delete-game\n"
            + "list-games\n"
            + "list-commands\n"
            + "disconnect\n";
    public static final String READY = "READY";
    public static final String THIS_IS_THE_LOSER = "THIS IS THE LOSER!";
    public static final String THIS_IS_THE_WINNER = "THIS IS THE WINNER!";
    public static final String YOU_SANK_ALL_OF_YOUR_OPPONENT_S_SHIPS_YOU_WIN = "You sank all of your opponent's ships! You win!";
    public static final String LIST_COMMANDS_CMD = "list-commands";
    public static final String LIST_GAMES_CMD = "list-games";
    public static final String JOIN_GAME = "join-game";
    public static final String JOIN_GAME_CMD = "join-game";
    public static final String START_GAME_CMD = "start-game";
    public static final String EXIT_GAME_CMD = "exit-game";
    public static final String DISCONNECT_CMD = "disconnect";
    public static final String CREATE_GAME_CMD = "create-game";

    private Socket socket;
    private Player player;
    private Set<String> playerNamesSet;
    private Map<String, GameInfo> gamesMap;
    private GameInfo currentGame;
    private boolean isConnected;

    public ClientHandler(
            final Socket socket, final Map<String, GameInfo> gamesMap,
            final Set<String> playersNamesSet) {
        this.socket = socket;
        this.gamesMap = gamesMap;
        this.playerNamesSet = playersNamesSet;
        this.player = null;
        this.currentGame = null;
        this.isConnected = true;
    }

    public void setPlayerName(final PrintWriter out, final BufferedReader in) throws IOException {
        out.println("Enter your username: ");
        String playerName = in.readLine();
        boolean isTakenName = false;
        while (playerName == null
                || playerName.length() <= 1 || playerName.length() > Player.MAX_PLAYER_NAME_LENGTH
                || (isTakenName = playerNamesSet.contains(playerName))) {
            if (isTakenName) {
                out.println("This name has already been taken. Please choose another one.");
            } else {
                out.println("Invalid username!\nYour name must contain more than 1 and less than "
                        + (Player.MAX_PLAYER_NAME_LENGTH + 1) + " symbols!");
            }
            playerName = in.readLine();
        }
        player = new Player(playerName, socket);
        playerNamesSet.add(playerName);
        out.println("You successfully set your username to [" + playerName + "]");
    }

    public void createGameCmd(final String gameName, final PrintWriter out) {
        if (currentGame != null) {
            out.println("You already have created/joined game [" + currentGame.getGameName() + "]");
            return;
        }
        if (gameName == null || gameName.length() <= 1
                || gameName.length() > GameInfo.MAX_GAME_NAME_LENGTH) {
            out.println("Invalid game name!\nThe name must contain more than 1 and less than "
                    + (GameInfo.MAX_GAME_NAME_LENGTH + 1) + " symbols!");
            return;
        } else if (gamesMap.containsKey(gameName)) {
            out.println("Game with this name already exists!");
            return;
        }
        currentGame = new GameInfo(gameName, player, GameStatusEnum.PENDING_GAME);
        gamesMap.put(gameName, currentGame);
        out.println("Game created! Waiting for second player...");
    }

    public void listGamesCmd(final PrintWriter out) {
        if (gamesMap.isEmpty()) {
            out.println("Currently there are no games created!");
            return;
        }
        out.println("|  GAME NAME  |  CREATOR  |      STATUS      | PLAYERS |");
        out.println("|-------------+-----------+------------------+---------|");
        for (GameInfo gameInfo : gamesMap.values()) {
            gameInfo.printGame(out);
        }
    }

    public void joinGameCmd(final PrintWriter out, final String gameName) throws IOException {
        if (currentGame != null) {
            out.println("You already have created/joined game [" + currentGame.getGameName() + "]");
            return;
        }
        if (!gamesMap.containsKey(gameName)) {
            out.println("There is no game with this name!");
            return;
        }
        currentGame = gamesMap.get(gameName);
        if (currentGame.getGameStatus() == GameStatusEnum.PENDING_GAME) {
            currentGame.setGuest(player);
            out.println("You joined game [" + currentGame.getGameName()
                    + "] with host [" + currentGame.getHost().getPlayerName() + "]"
            + "\nEnter [start-game] whenever you are READY!");
            Socket hostSocket = currentGame.getHost().getSocket();
            PrintWriter hostWriter = new PrintWriter(hostSocket.getOutputStream(), true);
            hostWriter.println("Player [" + player.getPlayerName() + "] joined your game!");
        } else if (currentGame.getGameStatus() == GameStatusEnum.IN_PROGRESS_GAME) {
            out.println("This game is already in progress!");
        } else {
            out.println("This game has already finished!");
        }
    }

    public void joinGameCmd(final PrintWriter out) throws IOException {
        if (currentGame != null) {
            out.println("You already have created/joined game [" + currentGame.getGameName() + "]");
            return;
        }
        if (gamesMap.isEmpty()) {
            out.println("There are no games available to join!");
            return;
        }
        ArrayList<GameInfo> pendingGamesList = new ArrayList<>();
        for (GameInfo gameInfo : gamesMap.values()) {
            if (gameInfo.getGameStatus() == GameStatusEnum.PENDING_GAME) {
                pendingGamesList.add(gameInfo);
            }
        }
        Random rand = new Random();
        int randomGameIndex = rand.nextInt(pendingGamesList.size());
        currentGame = pendingGamesList.get(randomGameIndex);
        currentGame.setGuest(player);
        out.println("You joined game [" + currentGame.getGameName()
                + "] with host [" + currentGame.getHost().getPlayerName() + "]."
                + "\nEnter [start-game] whenever you are ready!");
        Socket hostSocket = currentGame.getHost().getSocket();
        PrintWriter hostWriter = new PrintWriter(hostSocket.getOutputStream(), true);
        hostWriter.println("Player [" + player.getPlayerName()
                + "] joined your game! Enter [start-game] whenever you are READY!");
    }

    private void startGameCmd(final PrintWriter out) throws IOException {
        if (currentGame.getGuest() == null) {
            out.println("You have to wait for an opponent to join the game before starting!");
            return;
        }
        if (!currentGame.getHost().getPlayerName().equals(player.getPlayerName())
                && !currentGame.getGuest().getPlayerName().equals(player.getPlayerName())) {
            out.println("You haven't joined a game yet!");
            return;
        }
        Socket opponent;
        boolean isOpponentReady;
        if (currentGame.getHost().getPlayerName().equals(player.getPlayerName())) {
            opponent = currentGame.getGuest().getSocket();
            currentGame.getHost().setPlayerReady(true);
            isOpponentReady = currentGame.getGuest().getIsPlayerReady();
        } else {
            opponent = currentGame.getHost().getSocket();
            currentGame.getGuest().setPlayerReady(true);
            isOpponentReady = currentGame.getHost().getIsPlayerReady();
        }
        PrintWriter opponentWriter = new PrintWriter(opponent.getOutputStream(), true);
        opponentWriter.println("Your opponent is READY to start!");
        if (isOpponentReady) {
            out.println("Game started!");
            opponentWriter.println("Game started!");
            currentGame.setGameStatus(GameStatusEnum.IN_PROGRESS_GAME);
        } else {
            out.println("Waiting for your opponent...");
        }
    }
    //TODO When you disconnect while not in a game it
    // sends you "you are not in a game" and then "disconnected"
    public void exitGameCmd(final PrintWriter out) throws IOException {
        if (currentGame == null) {
            out.println("You are not in a game!");
            return;
        }
        if (currentGame.getGuest() != null
                && player.getPlayerName().equals(currentGame.getHost().getPlayerName())) {
            Socket guestSocket = currentGame.getGuest().getSocket();
            currentGame.setHost(currentGame.getGuest());
            currentGame.setGuest(null);
            currentGame.getHost().setPlayerReady(false);
            PrintWriter guestWriter = new PrintWriter(guestSocket.getOutputStream(), true);
            guestWriter.println("The host just left the game. You are now the host of this game!");
        } else if (currentGame.getGuest() != null) {
            gamesMap.get(currentGame.getGameName()).setGuest(null);
            currentGame.getHost().setPlayerReady(false);
            Socket hostSocket = currentGame.getHost().getSocket();
            PrintWriter hostWriter = new PrintWriter(hostSocket.getOutputStream(), true);
            hostWriter.println("Player [" + player.getPlayerName() + "] just left the game!");
        } else {
            gamesMap.remove(currentGame.getGameName());
        }
        currentGame = null;
        out.println("You left the game!");
    }

    private void endGame(final PrintWriter out) {
        currentGame.setGameStatus(GameStatusEnum.FINISHED_GAME);
        gamesMap.get(currentGame.getGameName()).setGameStatus(GameStatusEnum.FINISHED_GAME);
    }

//    //TODO
//    private void sendTextMsgCmd(PrintWriter out, String msg) {
//
//    }

    private void disconnectCmd(final PrintWriter out) throws IOException {
        exitGameCmd(out);
        playerNamesSet.remove(player.getPlayerName());
        isConnected = false;
        out.println("Disconnected from the server!"
        + "Press any key to continue!");
    }

    //TODO
    private void savedGamesCmd() {

    }

    //TODO
    private void loadGameCmd() {

    }

    //TODO
    private void deleteGameCmd() {

    }

    //TODO
    private void saveGame() {

    }

    private void executeCmd(final String cmd, final PrintWriter out) throws IOException {
        String[] splitCmd = cmd.split(" ", 2);
        if (splitCmd[0].length() < 1) {
            out.println("Invalid command!");
            return;
        }
        String commandOnly = splitCmd[0];
        if (splitCmd.length == 1) {
            switch (commandOnly) {
                case LIST_COMMANDS_CMD:
                    printCommands(out);
                    break;
                case LIST_GAMES_CMD:
                    listGamesCmd(out);
                    break;
                case JOIN_GAME_CMD:
                    joinGameCmd(out);
                    break;
                case START_GAME_CMD:
                    startGameCmd(out);
                    break;
                case EXIT_GAME_CMD:
                    exitGameCmd(out);
                    break;
                case DISCONNECT_CMD:
                    disconnectCmd(out);
                    break;
                default:
                    out.println("[" + commandOnly + "]" + " is not a valid command!");
                    break;
            }
        } else if (splitCmd.length == 2 && splitCmd[1].length() > 0) {
            switch (commandOnly) {
                case CREATE_GAME_CMD:
                    createGameCmd(splitCmd[1], out);
                    break;
                case JOIN_GAME_CMD:
                    joinGameCmd(out, splitCmd[1]);
                    break;
                default:
                    out.println("[" + commandOnly + "]" + " is not a valid command!");
                    break;
            }
        } else {
            System.out.println("Invalid command!");
        }
    }

    private void printCommands(final PrintWriter out) {
        out.println(AVAILABLE_COMMANDS);
    }

    @Override
    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println("Successfully connected to the server!");
            setPlayerName(out, in);
            printCommands(out);
            String input;
            while ((input = in.readLine()) != null) {
                if (currentGame == null
                        || currentGame.getGameStatus() != GameStatusEnum.IN_PROGRESS_GAME) {
                    executeCmd(input, out);
                } else {
                    if (input.equals(READY)) {
                        if (currentGame.getHost() == player) {
                            PrintWriter guestWriter
                                    = new PrintWriter(currentGame.getGuest()
                                    .getSocket().getOutputStream(), true);
                            guestWriter.println(READY);
                            while ((input = in.readLine()) != null) {
                                if (input.equals(THIS_IS_THE_LOSER)) {
                                    guestWriter.println(YOU_SANK_ALL_OF_YOUR_OPPONENT_S_SHIPS_YOU_WIN);
                                    break;
                                } else if (input.equals(THIS_IS_THE_WINNER)) {
                                    break;
                                }
                                guestWriter.println(input);
                            }
                        } else {
                            PrintWriter hostWriter
                                    = new PrintWriter(currentGame.getHost()
                                    .getSocket().getOutputStream(), true);
                            hostWriter.println(READY);
                            while ((input = in.readLine()) != null) {
                                if (input.equals(THIS_IS_THE_LOSER)) {
                                    hostWriter.println(YOU_SANK_ALL_OF_YOUR_OPPONENT_S_SHIPS_YOU_WIN);
                                    endGame(out);
                                    break;
                                } else if (input.equals(THIS_IS_THE_WINNER)) {
                                    break;
                                }
                                hostWriter.println(input);
                            }
                        }
                    }
                }
                if (!isConnected) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Connection problem with " + socket + " occurred!");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
