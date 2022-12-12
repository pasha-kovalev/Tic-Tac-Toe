package by.bsu.tictactoe.network.packets;

import java.io.Serializable;

public class UpdatePacket implements Serializable {
    private static final long serialVersionUID = -1074007205963214341L;
    private final int[][] fields;
    private final int currentPlayer;
    private final int[] requiredBoard;
    private final int[][] subboardWinner;

    public UpdatePacket(int[][] fields, int currentPlayer, int[] requiredBoard, int[][] subboardWinner) {
        this.fields = fields;
        this.currentPlayer = currentPlayer;
        this.requiredBoard = requiredBoard;
        this.subboardWinner = subboardWinner;
    }

    public int[][] getFields() {
        return fields;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[] getRequiredBoard() {
        return requiredBoard;
    }

    public int[][] getSubboardWinner() {
        return subboardWinner;
    }
}
