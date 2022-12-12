package by.bsu.tictactoe;

import by.bsu.tictactoe.game.ClientGame;
import by.bsu.tictactoe.game.ServerGame;
import by.bsu.tictactoe.gui.ConnectWindow;

public class Main {
    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("client")) {
            ClientGame clientGame = new ClientGame();
            new ConnectWindow(clientGame);
        } else {
            new ServerGame();
        }
    }
}
