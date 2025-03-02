package com.romansarkis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ControllerWindow extends JFrame {
    private JComboBox<String> playerSelector;
    private JButton increaseHealthButton, decreaseHealthButton, increaseResourceButton, decreaseResourceButton, saveButton;
    private Main mainInstance;

    public ControllerWindow(Main mainInstance) {
        this.mainInstance = mainInstance; // Reference to main window
        setTitle("Controller");
        setSize(300, 200);
        setLayout(new GridLayout(6, 1));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Player dropdown
        playerSelector = new JComboBox<>();
        for (PlayerPanel player : mainInstance.getPlayers()) {
            playerSelector.addItem(player.getPlayerName());
        }

        increaseHealthButton = new JButton("Increase Health");
        decreaseHealthButton = new JButton("Decrease Health");
        increaseResourceButton = new JButton("Increase Resource");
        decreaseResourceButton = new JButton("Decrease Resource");
        saveButton = new JButton("Save & Update");

        increaseHealthButton.addActionListener(e -> modifyHealthAndResources(1, 0));
        decreaseHealthButton.addActionListener(e -> modifyHealthAndResources(-1,0));
        increaseResourceButton.addActionListener(e -> modifyHealthAndResources(0, 5));
        decreaseResourceButton.addActionListener(e -> modifyHealthAndResources(0, -5));
        saveButton.addActionListener(e -> updateMainWindow());

        add(playerSelector);
        add(increaseHealthButton);
        add(decreaseHealthButton);
        add(increaseResourceButton);
        add(decreaseResourceButton);
        add(saveButton);

        setVisible(true);
    }

    private void modifyHealthAndResources(int healthAmount, int resourceAmount) {
        String selectedPlayer = (String) playerSelector.getSelectedItem();
        for (PlayerPanel player : mainInstance.getPlayers()) {
            if (player.getPlayerName().equals(selectedPlayer)) {
                player.updateValues(player.getCurrentHealth() + healthAmount, player.getCurrentResource() + resourceAmount);
                saveToJson(player.getPlayerName(), player.getCurrentHealth());
                break;
            }
        }
    }

    private void saveToJson(String playerName, int newHealth) {
        JSONParser parser = new JSONParser();
        try {
            // Parse the JSON file
            Object obj = parser.parse(new java.io.FileReader("config/players.json"));
            JSONObject jsonObject = (JSONObject) obj; // Use JSONObject for the root
            JSONArray playersArray = (JSONArray) jsonObject.get("players"); // Extract players array
    
            // Modify the correct player's health
            for (Object o : playersArray) {
                JSONObject playerObj = (JSONObject) o; // Cast each player to JSONObject
                if (playerObj.get("name").equals(playerName)) {
                    playerObj.put("currentHealth", newHealth); // Modify the health
                    break;
                }
            }
    
            // Save changes back to the JSON file
            try (FileWriter file = new FileWriter("config/players.json")) {
                file.write(jsonObject.toJSONString()); // Save entire JSON object
                file.flush();
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    

    private void updateMainWindow() {
        mainInstance.reloadPlayers(); // Reload players from updated JSON
    }
}
