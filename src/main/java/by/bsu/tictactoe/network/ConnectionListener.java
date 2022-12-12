package by.bsu.tictactoe.network;

public interface ConnectionListener {
    void onConnectionReady();

    void onDisconnect();

    void onException(Exception e);
}
