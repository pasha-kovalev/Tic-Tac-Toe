package by.bsu.tictactoe.gui;

import by.bsu.tictactoe.game.Game;

import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Window extends JFrame {
    private static final long serialVersionUID = 6119049208274746346L;
    private final Game game;

    public Window(String title, int width, int height, Game game) {
        super(title);
        this.game = game;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width + 10, height + 40);
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowListener(new Listener());
    }

    class Listener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            game.close();
        }
    }
}
