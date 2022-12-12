package by.bsu.tictactoe.network.packets;

import java.io.Serializable;

public class GameEndPacket implements Serializable {
    private static final long serialVersionUID = 3285262880484696199L;
    private final int winner;

    public GameEndPacket(int winner) {
        this.winner = winner;
    }

    public int getWinner() {
        return winner;
    }
}
