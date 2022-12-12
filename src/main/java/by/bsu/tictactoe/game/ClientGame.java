package by.bsu.tictactoe.game;

import by.bsu.tictactoe.network.Connection;
import by.bsu.tictactoe.network.ConnectionListener;
import by.bsu.tictactoe.network.packets.GameEndPacket;
import by.bsu.tictactoe.network.packets.OpponentMovePacket;
import by.bsu.tictactoe.network.packets.UpdatePacket;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.net.Socket;

public class ClientGame extends Game implements ConnectionListener {
    public boolean connected;
    private Socket socket;

    public ClientGame() {
        super(Game.O);
    }

    public void connectToServer(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            connection = new Connection(socket, this);
            this.port = port;
            onConnectionReady();
        } catch (IOException e) {
            onException(e);
        }
    }

    @Override
    public void processReceivedInput(int x, int y) {
        if (isMyTurn()) {
            if (requiredBoard[0] != Game.ANY && (requiredBoard[0] != getBoardIndex(x) || requiredBoard[1] != getBoardIndex(y))) {
                return;
            }
            if (subboardWinners[getBoardIndex(x)][getBoardIndex(y)] >= Game.TIE) {
                requiredBoard[0] = Game.ANY;
            } else {
                requiredBoard = getSubboardIndex(x, y);
            }
            connection.sendPacket(new OpponentMovePacket(x, y));
        }
    }

    @Override
    public void processReceivedPacket(Object object) {
        if (object instanceof UpdatePacket) {
            UpdatePacket packet = (UpdatePacket) object;
            fields = packet.getFields();
            currentPlayer = packet.getCurrentPlayer();
            requiredBoard = packet.getRequiredBoard();
            subboardWinners = packet.getSubboardWinner();
            gamePanel.repaint();
        } else if (object instanceof GameEndPacket) {
            GameEndPacket packet = (GameEndPacket) object;
            showWinner(packet.getWinner());
        }
        gamePanel.repaint();
    }


    @Override
    public void close() {
        if (connection != null) {
            connection.close();
        }
        try {
            socket.close();
        } catch (IOException e) {
            onException(e);
        }
    }

    @Override
    public void onConnectionReady() {
        connected = true;
        window.setVisible(true);
        gamePanel.repaint();
    }

    @Override
    public void onDisconnect() {
        JOptionPane.showMessageDialog(window, "Disconnect from opponent");
        showWinner(thisPlayer);
        close();
    }

    @Override
    public void onException(Exception e) {
        connected = false;
        e.printStackTrace();
    }
}
