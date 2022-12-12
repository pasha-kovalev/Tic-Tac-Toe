package by.bsu.tictactoe.network;

import by.bsu.tictactoe.game.Game;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements Runnable, ConnectionListener {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final Game game;
    private boolean running;

    public Connection(Socket socket, Game game) {
        this.game = game;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Thread thread = new Thread(this);
            thread.start();
        } catch (IOException e) {
            onException(e);
        }
    }

    @Override
    public void run() {
        onConnectionReady();
        while (running) {
            try {
                Object object = inputStream.readObject();
                game.processReceivedPacket(object);
            } catch (EOFException | SocketException e) {
                onDisconnect();
            } catch (ClassNotFoundException | IOException e) {
                onException(e);
            }
        }
    }

    public void sendPacket(Object object) {
        try {
            outputStream.reset();
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            onDisconnect();
        }
    }

    public void close() {
        try {
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            onException(e);
        }
    }

    @Override
    public void onConnectionReady() {
        running = true;
    }

    @Override
    public void onDisconnect() {
        running = false;
        game.setConnectionLost(true);
        game.onDisconnect();
    }

    @Override
    public void onException(Exception e) {
        System.out.println(e);
    }
}
