package by.bsu.tictactoe.game;

import by.bsu.tictactoe.network.Connection;
import by.bsu.tictactoe.network.ConnectionListener;
import by.bsu.tictactoe.network.packets.GameEndPacket;
import by.bsu.tictactoe.network.packets.OpponentMovePacket;
import by.bsu.tictactoe.network.packets.UpdatePacket;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerGame extends Game implements ConnectionListener {

    private ServerSocket serverSocket;

    public ServerGame() {
        super(Game.X);
        window.setVisible(true);
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            Socket socket = serverSocket.accept();
            connection = new Connection(socket, this);
            onConnectionReady();
        } catch (IOException e) {
            onException(e);
        }
    }

    public int getPort() {
        return port;
    }

    @Override
    public void processReceivedInput(int x, int y) {
        x /= Game.SUBBOARD_WIDTH;
        y /= Game.SUBBOARD_HEIGHT;
        if (isConnectionLost) {
            onDisconnect();
        }
        if (isMyTurn() && connection != null) {
            int xBoard = getBoardIndex(x);
            int yBoard = getBoardIndex(y);
            if (requiredBoard[0] != Game.ANY && (requiredBoard[0] != getBoardIndex(x) || requiredBoard[1] != getBoardIndex(y))) {
                return;
            }
            if (subboardWinners[xBoard][yBoard] >= Game.TIE) {
                requiredBoard[0] = Game.ANY;
            } else {
                requiredBoard = getSubboardIndex(x, y);
            }
            updateField(x, y);
        }
    }

    @Override
    public void processReceivedPacket(Object object) {
        if (object instanceof OpponentMovePacket) {
            OpponentMovePacket packet = (OpponentMovePacket) object;
            requiredBoard = getSubboardIndex(packet.getX(), packet.getY());
            updateField(packet.getX(), packet.getY());
        }
    }

    private void updateField(int x, int y) {
        if (fields[x][y] == Game.BLANK) {
            fields[x][y] = currentPlayer;
            if (currentPlayer == Game.X) {
                currentPlayer = Game.O;
            } else {
                currentPlayer = Game.X;
            }
            connection.sendPacket(new UpdatePacket(fields, currentPlayer, requiredBoard, subboardWinners));
            gamePanel.repaint();
            int winner = checkWin(x, y);
            if (winner != Game.BLANK) {
                endGame(winner);
            }
        }
    }

    private int checkWin(int x, int y) {
        int winner = Game.BLANK;
        boolean isNewSubboardWin = false;
        int x_board = getBoardIndex(x);
        int y_board = getBoardIndex(y);
        if (subboardWinners[x_board][y_board] == Game.BLANK) {
            subboardWinners[x_board][y_board] = checkBoardWin(x, y, fields, true);
            if (subboardWinners[x_board][y_board] != Game.BLANK) {
                isNewSubboardWin = true;
            }
        } else if (subboardWinners[x_board][y_board] < Game.TIE) {
            if (isSubboardFilled(x, y, fields)) {
                subboardWinners[x_board][y_board] *= COEFFICIENT;
            }
        }
        if (isNewSubboardWin) {
            winner = checkBoardWin(x, y, subboardWinners, false);
        }
        return winner;
    }

    private int checkBoardWin(int x_move, int y_move, int[][] board, boolean isSubboard) {
        int x_start = 0;
        int y_start = 0;
        int x_end = N; // N - size of game subboard. In our case, this number is 3
        int y_end = N;
        int x_board_index = getBoardIndex(x_move);
        int y_board_index = getBoardIndex(y_move);

        if (isSubboard) {
            x_start = getBoardIndex(x_move) * N;
            y_start = getBoardIndex(y_move) * N;
            x_end = x_start + N;
            y_end = y_start + N;
            x_board_index = getSubboardIndex(x_move);
            y_board_index = getSubboardIndex(y_move);
        }
        //check columns
        for (int x = x_start; x < x_end; x++) {
            int mark = board[x][y_start]; //mark at the beginning of column
            //if the column coordinate is the same as the x coordinate
            //of the last move, then check
            if (mark != Game.BLANK && getSubboardIndex(x) == x_board_index) {
                for (int y = y_start; y < y_end; y++) {
                    if (board[x][y] != mark)
                        break;
                    if (y == y_end - 1) {
                        return mark;
                    }

                }
            }
        }
        //check rows
        for (int y = y_start; y < y_end; y++) {
            int mark = board[x_start][y];
            if (mark != Game.BLANK && getSubboardIndex(y) == y_board_index) {
                for (int x = x_start; x < x_end; x++) {
                    if (board[x][y] != mark)
                        break;
                    if (x == x_end - 1) {
                        return mark;
                    }
                }
            }
        }
        //check diagonal
        if (x_board_index == y_board_index) {
            for (int y = y_start, x = x_start, mark = board[x_start][y_start]; y < y_end && x < x_end; x++, y++) {
                if (mark != Game.BLANK) {
                    if (board[x][y] != mark)
                        break;
                    if (x == x_end - 1) {
                        return mark;
                    }
                }
            }
        }
        //check antidiagonal
        if (x_board_index + y_board_index == N - 1) {
            for (int y = y_end - 1, x = x_start, mark = board[x_start][y_end - 1]; y >= y_start && x < x_end; x++, y--) {
                if (mark != Game.BLANK) {
                    if (board[x][y] != mark)
                        break;
                    if (x == x_end - 1) {
                        return mark;
                    }
                }
            }
        }
        //tie check
        checkTieLoop:
        for (int y = y_start; y < y_end; y++) {
            for (int x = x_start; x < x_end; x++) {
                if (board[x][y] == Game.BLANK)
                    break checkTieLoop;
                if (x == x_end - 1 && y == y_end - 1) {
                    return Game.TIE;
                }
            }
        }
        return Game.BLANK;
    }

    private boolean isSubboardFilled(int x_move, int y_move, int[][] subboard) {
        int x_start = getBoardIndex(x_move) * N;
        int y_start = getBoardIndex(y_move) * N;
        int x_end = x_start + N;
        int y_end = y_start + N;

        for (int y = y_start; y < y_end; y++) {
            for (int x = x_start; x < x_end; x++) {
                if (subboard[x][y] == Game.BLANK)
                    return false;
            }
        }
        return true;
    }

    private void endGame(int winner) {
        if (!isConnectionLost) {
            if (winner > Game.TIE) winner /= COEFFICIENT;
            showWinner(winner);
            connection.sendPacket(new GameEndPacket(winner));
        } else onDisconnect();
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                serverSocket.close();
            } catch (IOException e) {
                onException(e);
            }
        }
    }


    @Override
    public void onConnectionReady() {
        gamePanel.repaint();
    }

    @Override
    public void onDisconnect() {
        JOptionPane.showMessageDialog(window, "Disconnect from opponent");
        gamePanel.repaint();
        showWinner(thisPlayer);
        close();
    }

    @Override
    public void onException(Exception e) {
        System.out.println(e);
    }
}
