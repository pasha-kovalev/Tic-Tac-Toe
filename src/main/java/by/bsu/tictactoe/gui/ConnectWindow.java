package by.bsu.tictactoe.gui;

import by.bsu.tictactoe.game.ClientGame;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectWindow extends JFrame {
    private static final long serialVersionUID = 6119049208274746346L;
    ClientGame clientGame;
    JLabel ip_label, port_label;
    JButton connectButton;
    JTextField ip_input, port_input;
    eHandler handler = new eHandler();
    private String ip;
    private int port;

    public ConnectWindow(ClientGame clientGame) {
        this.clientGame = clientGame;
        setTitle("Подключение");
        setSize(300, 170);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        ip_label = new JLabel("Введите IP:");
        port_label = new JLabel("Введите порт:");
        ip_input = new JTextField(20);
        port_input = new JTextField(20);
        connectButton = new JButton("Подключиться");
        add(ip_label);
        add(ip_input);
        add(port_label);
        add(port_input);
        add(connectButton);
        connectButton.addActionListener(handler);
        setVisible(true);
    }

    public class eHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == connectButton) {
                    ip = ip_input.getText();
                    port = Integer.parseInt(port_input.getText());
                }
                clientGame.connectToServer(ip, port);
                if (!clientGame.connected) {
                    JOptionPane.showMessageDialog(null, "Server has not found. Try again");
                } else {
                    dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Порт должен быть числом");
            }
        }
    }
}
