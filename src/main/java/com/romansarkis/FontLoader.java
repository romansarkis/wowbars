package com.romansarkis;

import java.awt.*;
import java.io.File;

public class FontLoader {
    public static Font loadWoWFont(float size) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/friz_quadrata.ttf"));
            return customFont.deriveFont(size);
        } catch (Exception e) {
            System.err.println("Error loading WoW font: " + e.getMessage());
            return new Font("Serif", Font.BOLD, (int) size); // Fallback font
        }
    }
}
