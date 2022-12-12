package by.bsu.tictactoe.gui;

import by.bsu.tictactoe.game.Game;
import by.bsu.tictactoe.resourses.Resources;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {
    public static final int SMALL_BORDER = 1;
    public static final int BIG_BORDER = 4;
    public static final int FONT_SIZE = 32;
    private static final long serialVersionUID = 900129083420602242L;
    private final Game game;

    public GamePanel(Game game) {
        this.game = game;
        addMouseListener(new Listener());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        setGUIStyle(g2D);
        if (game.getConnection() == null) {
            processConnectionNull(g2D);
            return;
        }
        drawLines(g2D);
        drawImages(g2D);
        if (game.isConnectionLost()) {
            int textMeasure = g2D.getFontMetrics().stringWidth(Game.LOST_CONNECTION);
            g2D.drawString(Game.LOST_CONNECTION, Game.WIDTH / 2 - textMeasure / 2, Game.HEIGHT / 2);
        }
    }

    private void setGUIStyle(Graphics2D g2D) {
        g2D.setFont(new Font("Verdana", Font.BOLD, FONT_SIZE));
        g2D.setColor(Color.black);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void drawImages(Graphics2D g2D) {
        for (int x = 0; x < Game.NN; x++) {
            for (int y = 0; y < Game.NN; y++) {
                int field = game.getFields()[x][y];
                if (field != Game.BLANK) {
                    g2D.drawImage(Resources.marks[field - 1], x * Game.SUBBOARD_WIDTH, y * Game.SUBBOARD_HEIGHT, Game.SUBBOARD_WIDTH, Game.SUBBOARD_HEIGHT, null);
                }
            }
        }
        for (int x = 0; x < Game.N; x++) {
            for (int y = 0; y < Game.N; y++) {
                int field = game.getSubboardWinners()[x][y];
                if (field >= Game.TIE) field /= Game.COEFFICIENT;
                if (field != Game.BLANK) {
                    g2D.drawImage(Resources.marks[field - 1], x * Game.BOARD_WIDTH, y * Game.BOARD_HEIGHT, Game.BOARD_WIDTH, Game.BOARD_HEIGHT, null);
                }
            }
        }
    }

    private void drawLines(Graphics2D g2D) {
        g2D.setStroke(new BasicStroke(SMALL_BORDER));
        for (int x = Game.SUBBOARD_WIDTH; x <= Game.SUBBOARD_WIDTH * Game.NN; x += Game.SUBBOARD_WIDTH) {
            g2D.drawLine(x, 0, x, Game.HEIGHT);
        }
        for (int y = Game.SUBBOARD_HEIGHT; y <= Game.SUBBOARD_HEIGHT * Game.NN; y += Game.SUBBOARD_HEIGHT) {
            g2D.drawLine(0, y, Game.WIDTH, y);
        }
        g2D.setStroke(new BasicStroke(BIG_BORDER));
        for (int x = Game.BOARD_WIDTH; x <= Game.BOARD_WIDTH * Game.N; x += Game.BOARD_WIDTH) {
            g2D.drawLine(x, 0, x, Game.HEIGHT);
        }
        for (int y = Game.BOARD_HEIGHT; y <= Game.BOARD_HEIGHT * Game.N; y += Game.BOARD_HEIGHT) {
            g2D.drawLine(0, y, Game.WIDTH, y);
        }
    }

    private void processConnectionNull(Graphics2D g2D) {
        String port = "";
        if (game.getPort() != 0) port = "Port: " + ((Integer) game.getPort()).toString();
        int waitingLength = g2D.getFontMetrics().stringWidth(Game.WAITING);
        int waitingHeight = g2D.getFontMetrics().getHeight();
        int portLength = g2D.getFontMetrics().stringWidth(port);
        g2D.drawString(Game.WAITING, Game.WIDTH / 2 - waitingLength / 2, Game.HEIGHT / 2);
        g2D.drawString(port, Game.WIDTH / 2 - portLength / 2, Game.HEIGHT / 2 + waitingHeight);
    }

    class Listener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            game.processReceivedInput(e.getX(), e.getY());
        }
    }
}
