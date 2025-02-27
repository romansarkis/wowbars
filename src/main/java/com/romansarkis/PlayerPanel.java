package com.romansarkis;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PlayerPanel extends JPanel {
    private BufferedImage background;
    private BufferedImage portrait;
    private String playerName;
    private int level;
    private int maxHealth, currentHealth;
    private int maxResource, currentResource;
    private Color resourceColor;

    public PlayerPanel(String playerName, int level, int maxHealth, int currentHealth, int maxResource, int currentResource, String resourceType, String portraitPath) {
        this.playerName = playerName;
        this.level = level;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.maxResource = maxResource;
        this.currentResource = currentResource;
        
        // Determine resource bar color
        switch (resourceType.toLowerCase()) {
            case "mana": resourceColor = Color.BLUE; break;
            case "rage": resourceColor = Color.RED; break;
            case "stamina": resourceColor = Color.YELLOW; break;
            default: resourceColor = Color.GRAY;
        }

        try {
            background = ImageIO.read(new File("assets/playerframe.png"));
            if (background == null) {
                System.err.println("Failed to load background image.");
            }
            portrait = ImageIO.read(new File(portraitPath));
            if (portrait == null) {
                System.err.println("Failed to load portrait image.");
            }
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        setPreferredSize(new Dimension(background.getWidth(), background.getHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw background image
        g2.drawImage(background, 0, 0, this);

        // Draw health bar
        int healthBarWidth = (int) ((currentHealth / (double) maxHealth) * 150); // Adjust width accordingly
        g2.setColor(Color.GREEN);
        g2.fillRect(50, 20, healthBarWidth, 10); // Position inside the background

        // Draw resource bar
        int resourceBarWidth = (int) ((currentResource / (double) maxResource) * 150);
        g2.setColor(resourceColor);
        g2.fillRect(50, 35, resourceBarWidth, 10);

        // Draw player name
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 14));  // Replace with WoW font if you have it
        g2.drawString(playerName, 55, 15);

        // Draw character level
        g2.setFont(new Font("Serif", Font.BOLD, 12));
        g2.setColor(Color.YELLOW);
        g2.drawString(String.valueOf(level), 20, 35);

        // Draw portrait
        if (portrait != null) {
            g2.drawImage(portrait, 5, 5, 40, 40, this);  // Position it inside the circle
        }
    }

    // Method to update values dynamically
    public void updateValues(int newHealth, int newResource) {
        this.currentHealth = newHealth;
        this.currentResource = newResource;
        repaint(); // Repaint the panel
    }
}
