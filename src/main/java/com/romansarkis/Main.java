package com.romansarkis;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class Main {
    public static String VERSION = "v0.4";
    private static boolean isFullscreen = false;
    private static JFrame frame;

    public static void main(String[] args) {
        // Create the main frame
        frame = new JFrame("WoW Bars " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("assets/icon.png");
        frame.setIconImage(icon.getImage());

        frame.setMinimumSize(new Dimension(1928, 1080)); 
        frame.setLayout(new GridLayout(0, 2)); 
        frame.getContentPane().setBackground(Color.gray);

        List<PlayerPanel> players = PlayerLoader.loadPlayers();
        for (PlayerPanel player : players) {
            player.setOpaque(false);
            frame.add(player);
        }

        // Add Key Listener for Fullscreen Toggle
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) {
                    toggleFullscreen();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0); // Exit app with ESC
                }
            }
        });

        frame.setFocusable(true);
        frame.requestFocusInWindow();

        frame.setVisible(true);
    }

    private static void toggleFullscreen() {
        isFullscreen = !isFullscreen;

        frame.dispose(); // Dispose before changing settings

        if (isFullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true); // Remove borders & title bar
        } else {
            frame.setExtendedState(JFrame.NORMAL);
            frame.setUndecorated(false); // Restore title bar
            frame.setSize(800, 600); // Set a default size when exiting fullscreen
            frame.setLocationRelativeTo(null); // Center the window
        }

        frame.setVisible(true); // Re-show the frame
    }
}
