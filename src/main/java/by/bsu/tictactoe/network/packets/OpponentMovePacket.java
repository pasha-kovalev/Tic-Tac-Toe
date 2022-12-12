package by.bsu.tictactoe.network.packets;

import java.io.Serializable;

public class OpponentMovePacket implements Serializable {
    private static final long serialVersionUID = -3924525876235627028L;
    private final int x;
    private final int y;

    public OpponentMovePacket(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
