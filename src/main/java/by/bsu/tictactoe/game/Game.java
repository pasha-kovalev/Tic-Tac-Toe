package by.bsu.tictactoe.game;

import by.bsu.tictactoe.gui.GamePanel;
import by.bsu.tictactoe.gui.Window;
import by.bsu.tictactoe.network.Connection;
import by.bsu.tictactoe.network.ConnectionListener;

import javax.swing.JOptionPane;

public abstract class Game implements ConnectionListener {
    public static final int WIDTH = 900, HEIGHT = 900;
    public static final int N = 3; //subboard dimension
    public static final int NN = N * N; //board dimension
    public static final int SUBBOARD_WIDTH = WIDTH / NN;
    public static final int SUBBOARD_HEIGHT = HEIGHT / NN;
    public static final int BOARD_WIDTH = WIDTH / N;
    public static final int BOARD_HEIGHT = HEIGHT / N;
    public static final int COEFFICIENT = 10;
    //fields to denote the state of the board and subboard
    public static final int BLANK = 0, X = 1, O = 2, TIE = 3, FILLED_X = X * COEFFICIENT, FILLED_O = O * COEFFICIENT;
    //field to denote that the player can go anywhere
    public static final int ANY = 30;
    public static final String LOST_CONNECTION = "Connection is lost. You win";
    public static final String WAITING = "Waiting for opponent";

    protected Window window;
    protected GamePanel gamePanel;
    protected Connection connection;
    protected int[][] fields;
    protected int[][] subboardWinners;
    protected int[] requiredBoard;
    protected int currentPlayer;
    protected int thisPlayer;
    protected int port;
    protected boolean isConnectionLost;

    public Game(int thisPlayer) {
        this.thisPlayer = thisPlayer;
        fields = new int[9][9];
        subboardWinners = new int[3][3];
        requiredBoard = new int[]{Game.ANY, Game.ANY};
        isConnectionLost = false;
        currentPlayer = X;
        window = new Window("Крестики-нолики", WIDTH, HEIGHT, this);
        gamePanel = new GamePanel(this);
        window.add(gamePanel);
    }

    public int[][] getFields() {
        return fields;
    }

    public int[][] getSubboardWinners() {
        return subboardWinners;
    }

    public int[] getSubboardIndex(int x, int y) {
        int[] result = new int[2];
        result[0] = x % N;
        result[1] = y % N;
        return result;
    }

    public int getSubboardIndex(int x) {
        return x % N;
    }

    public int getBoardIndex(int x) {
        return x / N;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnectionLost() {
        return isConnectionLost;
    }

    public void setConnectionLost(boolean connectionLost) {
        isConnectionLost = connectionLost;
    }

    public int getPort() {
        return port;
    }

    public abstract void processReceivedInput(int x, int y);

    public abstract void processReceivedPacket(Object object);

    public abstract void close();

    protected void showWinner(int winner) {
        if (winner != Game.TIE) {
            JOptionPane.showMessageDialog(window, "The player " + winner + " has won!");
        } else {
            JOptionPane.showMessageDialog(window, "TIE!");
        }
    }

    protected boolean isMyTurn() {
        if (thisPlayer == currentPlayer) {
            return true;
        }
        return false;
    }
}
