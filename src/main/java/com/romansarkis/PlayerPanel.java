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
            portrait = ImageIO.read(new File(portraitPath));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage());
        }

        setPreferredSize(new Dimension(600, 150)); // Default panel size
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    int panelWidth = getWidth();
    int panelHeight = getHeight();

    int bgWidth = panelWidth;
    int bgHeight = (int) (background.getHeight() * ((double) panelWidth / background.getWidth()));

    Font wowFont = FontLoader.loadWoWFont(panelWidth / 15);
    g2.setFont(wowFont);

    // Ensure background fits within panel
    if (bgHeight > panelHeight) {
        bgHeight = panelHeight;
        bgWidth = (int) (background.getWidth() * ((double) panelHeight / background.getHeight()));
    }

    int bgX = (panelWidth - bgWidth) / 2;
    int bgY = (panelHeight - bgHeight) / 2;

    // Scale and position portrait relative to background
    if (portrait != null) {
        double portraitScale = 0.55; // Adjust this to fit within the background frame
        int portraitWidth = (int) (bgWidth * portraitScale);
        int portraitHeight = (int) (bgHeight * portraitScale);

        // Keep aspect ratio
        double aspectRatio = (double) portrait.getWidth() / portrait.getHeight();
        if (portraitWidth > portraitHeight * aspectRatio) {
            portraitWidth = (int) (portraitHeight * aspectRatio);
        } else {
            portraitHeight = (int) (portraitWidth / aspectRatio);
        }

        // Position portrait inside the background
        int portraitX = bgX + (int) (bgWidth * 0.065);  // Adjust X to fit within the left circle
        int portraitY = bgY + (int) (bgHeight * 0.1); // Adjust Y to center inside the circle

        g2.drawImage(portrait, portraitX, portraitY, portraitWidth, portraitHeight, this);
    }

    // Health & Resource Bars (scaled to panel)
    int barWidth = (int) (bgWidth * 0.55);
    int healthBarHeight = (int) (bgHeight * 0.09);
    int resourceBarHeight = healthBarHeight;

    int healthBarFilled = (int) ((currentHealth / (double) maxHealth) * barWidth);
    int resourceBarFilled = (int) ((currentResource / (double) maxResource) * barWidth);

    int barX = bgX + (int) ((bgWidth - barWidth) / 1.25);
    int healthBarY = bgY + (int) (bgHeight * 0.35);
    int resourceBarY = healthBarY + healthBarHeight + (int) (bgHeight * 0.03);

    // Draw health bar
    g2.setColor(Color.GREEN);
    g2.fillRect(barX, healthBarY, healthBarFilled, healthBarHeight);

    // Draw resource bar
    g2.setColor(resourceColor);
    g2.fillRect(barX, resourceBarY, resourceBarFilled, resourceBarHeight);

    // Draw the background
    g2.drawImage(background, bgX, bgY, bgWidth, bgHeight, this);

    // Draw player name
    FontMetrics fm = g2.getFontMetrics();
    int textWidth = fm.stringWidth(playerName);

    g2.setColor(Color.WHITE);
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.575) - textWidth / 2, bgY + bgHeight / 4);

    // Draw level
    g2.setColor(Color.YELLOW);
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.07), bgY + (int) (bgHeight * 0.15));
}


    public void updateValues(int newHealth, int newResource) {
        this.currentHealth = newHealth;
        this.currentResource = newResource;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 150);
    }
}
