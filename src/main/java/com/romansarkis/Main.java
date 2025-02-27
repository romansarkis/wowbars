package com.romansarkis;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.*;

public class Main {
    public static String VERSION = "v0.1";
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("WoW Bars " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new GridLayout(0, 1)); // Stack player panels
        
        List<PlayerPanel> players = PlayerLoader.loadPlayers();
        for (PlayerPanel player : players) {
            frame.add(player);
        }
        // Show the window
        frame.setVisible(true);
    }
}
