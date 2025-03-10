package com.romansarkis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.List;

public class PlayerPanel extends JPanel {
    private BufferedImage background;
    private BufferedImage portrait;
    private BufferedImage tempBuff;
    private String playerName;
    private int level;
    private int maxHealth, currentHealth;
    private int maxResource, currentResource;
    private Color resourceColor;
    int buffsPerRow = 5;  // Max number of buffs per row

    private List<String> buffs;

    public PlayerPanel(String playerName, int level, int maxHealth, int currentHealth, int maxResource, int currentResource, String resourceType, String portraitPath, List<String> buffs) {
        this.playerName = playerName;
        this.level = level;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.maxResource = maxResource;
        this.currentResource = currentResource;
        this.buffs = buffs;

        // Determine resource bar color
        switch (resourceType.toLowerCase()) {
            case "mana": resourceColor = new Color(1,3,168); break;
            case "rage": resourceColor = new Color(168,2,6); break;
            case "stamina": resourceColor = new Color(202,201,0); break;
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

    // Ensure background fits within panel
    if (bgHeight > panelHeight) {
        bgHeight = panelHeight;
        bgWidth = (int) (background.getWidth() * ((double) panelHeight / background.getHeight()));
    }

    int bgX = (panelWidth - bgWidth) / 2;
    int bgY = (panelHeight - bgHeight) / 2;

    // Scale and position portrait relative to background
    if (portrait != null) {
        double portraitScale = 0.575; // Adjust this to fit within the background frame
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
        int portraitX = bgX + (int) (bgWidth * 0.061);  // Adjust X to fit within the left circle
        int portraitY = bgY + (int) (bgHeight * 0.11); // Adjust Y to center inside the circle

        g2.drawImage(portrait, portraitX, portraitY, portraitWidth, portraitHeight, this);
    }

    // Health & Resource Bars (scaled to panel)
    int barWidth = (int) (bgWidth * 0.6);
    int healthBarHeight = (int) (bgHeight * 0.09);
    int resourceBarHeight = healthBarHeight;

    int healthBarFilled = (int) ((currentHealth / (double) maxHealth) * barWidth);
    int resourceBarFilled = (int) ((currentResource / (double) maxResource) * barWidth);

    int barX = bgX + (int) ((bgWidth - barWidth) / 1.1);
    int healthBarY = bgY + (int) (bgHeight * 0.375);
    int resourceBarY = healthBarY + healthBarHeight + (int) (bgHeight * 0.0275);

    // Draw health bar
    g2.setColor(new Color(8,165,2));
    g2.fillRect(barX, healthBarY, healthBarFilled, healthBarHeight);

    // Draw resource bar
    g2.setColor(resourceColor);
    g2.fillRect(barX, resourceBarY, resourceBarFilled, resourceBarHeight);

    g2.setColor(new Color(0, 0, 0, 150));
    g2.fillRect((int)(barX), (int)(bgY + bgHeight / 6), (int) (bgWidth / 1.69), (int) (bgHeight / 5));

    // Draw the background
    g2.drawImage(background, bgX, bgY, bgWidth, bgHeight, this);

    // Draw all buffs
    double buffScale = 0.17; // Scale relative to background
    int buffWidth = (int) (bgWidth * buffScale);
    int buffHeight = (int) (bgHeight * buffScale);

    // Keep aspect ratio
    double aspectRatio = (double) portrait.getWidth() / portrait.getHeight();
    if (buffWidth > buffHeight * aspectRatio) {
        buffWidth = (int) (buffHeight * aspectRatio);
    } else {
        buffHeight = (int) (buffWidth / aspectRatio);
    }

    for (int i = 0; i < buffs.size(); i++) {
        try {
            // Load buff image
            tempBuff = ImageIO.read(new File("assets/buffs/" + buffs.get(i)));
    
            // Calculate row and column position
            int col = i % buffsPerRow;  // Buff position in the row (0-4)
            int row = i / buffsPerRow;  // Buff row index (0,1,2...)
    
            // Calculate X and Y positions
            int buffX = bgX + (int) (bgWidth * 0.42) + (int) (col * (buffWidth * 1.2)); // Adjust X to space out buffs
            int buffY = bgY + (int) (bgHeight * 0.63) + (int) (row * (buffHeight * 1.2)); // Adjust Y for multiple rows
    
            // Draw buff icon
            g2.drawImage(tempBuff, buffX, buffY, buffWidth, buffHeight, this);
        } catch (Exception e) {
            System.err.println("Error loading buff: " + e.getMessage());
        }
    }

    // Draw player name
    Font wowFont = FontLoader.loadWoWFont(bgWidth / 15);
    g2.setFont(wowFont);

    FontMetrics fm = g2.getFontMetrics();
    int textWidth = fm.stringWidth(playerName);

    g2.setColor(Color.BLACK);
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.49) - textWidth / 2, bgY +(int)(bgHeight * .31));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.51) - textWidth / 2, bgY + (int)(bgHeight * .31));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.5) - textWidth / 2, bgY + (int) (bgHeight * .305));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.5) - textWidth / 2, bgY + (int) (bgHeight * .315));

    g2.drawString(playerName, bgX + (int)(bgWidth / 1.49) - textWidth / 2, bgY +(int)(bgHeight * .305));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.49) - textWidth / 2, bgY +(int)(bgHeight * .305));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.51) - textWidth / 2, bgY + (int)(bgHeight * .315));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.51) - textWidth / 2, bgY + (int)(bgHeight * .315));

    g2.setColor(new Color(214,178,0));
    g2.drawString(playerName, bgX + (int)(bgWidth / 1.5) - textWidth / 2, bgY + (int)(bgHeight * 0.31));

    // Draw level
    textWidth = fm.stringWidth(String.valueOf(level));
    g2.setColor(Color.BLACK);

    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.09)  - textWidth / 2, bgY + (int) (bgHeight * 0.64));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.1)  - textWidth / 2, bgY + (int) (bgHeight * 0.64));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.095)  - textWidth / 2, bgY + (int) (bgHeight * 0.635));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.095)  - textWidth / 2, bgY + (int) (bgHeight * 0.645));

    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.09)  - textWidth / 2, bgY + (int) (bgHeight * 0.645));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.09)  - textWidth / 2, bgY + (int) (bgHeight * 0.635));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.1)  - textWidth / 2, bgY + (int) (bgHeight * 0.645));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.1)  - textWidth / 2, bgY + (int) (bgHeight * 0.635));

    g2.setColor(new Color(214,178,0,255));
    g2.drawString(String.valueOf(level), bgX + (int) (bgWidth * 0.095)  - textWidth / 2, bgY + (int) (bgHeight * 0.64));

    // Draw Health and resource statistics
    wowFont = FontLoader.loadStatisticsFont(bgWidth/16);
    g2.setFont(wowFont);

    fm = g2.getFontMetrics();
    int healthStatWidth = fm.stringWidth(currentHealth + "/" + maxHealth);
    int resourceStatWidth = fm.stringWidth(currentResource + "/" + maxResource);

    g2.setColor(Color.BLACK);
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.505) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.46));
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.495) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.46));
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.5) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.465));
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.5) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.455));

    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.495) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.455));
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.495) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.465));
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.505) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.455));
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.505) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.465));

    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.505) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.573));
    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.495) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.573));
    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.5) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.578));
    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.5) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.568));

    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.495) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.568));
    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.495) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.578));
    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.505) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.568));
    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.505) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.578));

    g2.setColor(Color.WHITE);
    g2.drawString(currentHealth + "/" + maxHealth, bgX + (int)(bgWidth / 1.5) - healthStatWidth / 2, bgY + (int)(bgHeight * 0.46));

    g2.drawString(currentResource + "/" + maxResource, bgX + (int)(bgWidth / 1.5) - resourceStatWidth / 2, bgY + (int)(bgHeight * 0.573));
}


    public void updateValues(int newHealth, int newResource) {
        this.currentHealth = newHealth;
        this.currentResource = newResource;
        repaint();
    }

    public void addBuff(String buff) {
        this.buffs.add(buff);
        repaint();
    }

    public void removeBuff(String buff) {
        this.buffs.remove(buff);
        repaint();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getCurrentResource() {
        return this.currentResource;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 150);
    }
}
