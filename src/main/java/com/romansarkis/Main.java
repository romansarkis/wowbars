package com.romansarkis;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class Main {
    public static String VERSION = "v0.5";
    private boolean isFullscreen = false;
    private JFrame frame;
    private List<PlayerPanel> players;

    public static void main(String[] args) {
        Main mainWindow = new Main();
        mainWindow.start();
    }

    public void start() {
        frame = new JFrame("WoW Bars " + VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("assets/icon.png");
        frame.setIconImage(icon.getImage());

        frame.setMinimumSize(new Dimension(1920, 1080)); 
        frame.setLayout(new GridLayout(0, 2)); 
        frame.getContentPane().setBackground(Color.gray);

        players = PlayerLoader.loadPlayers();
        for (PlayerPanel player : players) {
            player.setOpaque(false);
            frame.add(player);
        }

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

         // Automatically open the controller window
        SwingUtilities.invokeLater(() -> new ControllerWindow(this));

        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setVisible(true);
    }

    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;

        frame.dispose();

        if (isFullscreen) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setUndecorated(true); // Remove borders & title bar
        } else {
            frame.setExtendedState(JFrame.NORMAL);
            frame.setUndecorated(false); // Restore title bar
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
        }

        frame.setVisible(true);
    }

    public List<PlayerPanel> getPlayers() {
        return players;
    }

    public void reloadPlayers() {
        players = PlayerLoader.loadPlayers();
        for (PlayerPanel player : players) {
            player.repaint();
        }
    }
}
