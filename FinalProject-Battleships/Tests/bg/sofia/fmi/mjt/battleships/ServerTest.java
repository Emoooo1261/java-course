package bg.sofia.fmi.mjt.battleships;

import bg.sofia.fmi.mjt.battleships.server.ClientHandler;
import bg.sofia.fmi.mjt.battleships.server.GameInfo;
import bg.sofia.fmi.mjt.battleships.server.Server;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the server.
 */
public class ServerTest {
    private ClientHandler clientHandler;
    private Map<String, GameInfo> gamesMap;
    private Set<String> namesSet;

    @Before
    public void initialize() {
        gamesMap = new HashMap<>();
        namesSet = new HashSet<>();
        clientHandler = new ClientHandler(null, gamesMap, namesSet);
    }
    @Test
    public void createGameCmdTest() throws IOException {
        PrintWriter writer = new PrintWriter(System.out);
        clientHandler.createGameCmd("game", writer);
        clientHandler.createGameCmd("TooLongGameName", writer);
        clientHandler.createGameCmd("second", writer);
        assertEquals(gamesMap.size(), 1);
        assertEquals(gamesMap.get("game").getNumberOfPlayers(), 1);
    }

    @Test
    public void joinGameCmdTest() throws IOException {
        PrintWriter writer = new PrintWriter(System.out);
        clientHandler.createGameCmd("game", writer);
        clientHandler.joinGameCmd(writer);
        assertEquals(gamesMap.get("game").getNumberOfPlayers(), 1);
        PrintWriter writer1 = new PrintWriter(new Socket().getOutputStream());
        clientHandler.joinGameCmd(writer1, "game");
        clientHandler.joinGameCmd(writer1);
        assertEquals(gamesMap.get("game").getNumberOfPlayers(), 2);
    }

    @Test
    public void setPlayerNameTest() throws IOException {
        PrintWriter writer = new PrintWriter(System.out);
        String name = "name";
        Reader inputString = new StringReader(name);
        BufferedReader reader = new BufferedReader(inputString);
        clientHandler.setPlayerName(writer, reader);
        assertEquals(namesSet.size(), 1);
        PrintWriter writer1 = new PrintWriter(System.out);
        name = "name1";
        Reader inputString1 = new StringReader(name);
        BufferedReader reader1 = new BufferedReader(inputString1);
        clientHandler.setPlayerName(writer1,reader1);
        assertEquals(namesSet.size(), 2);

    }

    @Test
    public void exitGameCmdTest() throws IOException {
        PrintWriter writer = new PrintWriter(System.out);
        clientHandler.createGameCmd("game", writer);
        assertEquals(gamesMap.size(), 1);
        clientHandler.exitGameCmd(writer);
        assertEquals(gamesMap.size(), 0);
    }
}
