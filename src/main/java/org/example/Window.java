package org.example;

import org.example.client.Client;
import org.example.game.Game;

import javax.swing.*;
import java.awt.*;

import static org.example.Main.SCREEN_H;
import static org.example.Main.SCREEN_W;

public class Window {

    private final Game game;
    private final JFrame frame = new JFrame();

    public Window(Client client){
        game = new Game(client);
        frame.add(game);
        frame.setTitle("HaxBall by Kopecki Piotr & Krycki Jakub");
        frame.setSize(SCREEN_W, SCREEN_H);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
