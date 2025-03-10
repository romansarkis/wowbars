package com.romansarkis;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ControllerWindow extends JFrame {
    private JComboBox<String> playerSelector;
    private JComboBox<String> buffSelector;
    private JButton increaseHealthButton, decreaseHealthButton, increaseResourceButton, decreaseResourceButton, singleBuffButton, multipleBuffButton, removeSingleBuffButton, removePartyBuffButton;
    private Main mainInstance;

    public ControllerWindow(Main mainInstance) {
        this.mainInstance = mainInstance; // Reference to main window
        setTitle("Controller");
        setSize(300, 200);
        setLayout(new GridLayout(5, 2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Player dropdown
        playerSelector = new JComboBox<>();
        for (PlayerPanel player : mainInstance.getPlayers()) {
            playerSelector.addItem(player.getPlayerName());
        }

        increaseHealthButton = new JButton("Health +1");
        decreaseHealthButton = new JButton("Health -1");
        increaseResourceButton = new JButton("Resource +5");
        decreaseResourceButton = new JButton("Resource -5");
        singleBuffButton = new JButton("Buff Self");
        multipleBuffButton = new JButton("Buff Party");
        removeSingleBuffButton = new JButton("Remove Single Buff");
        removePartyBuffButton = new JButton("Remove Party Buff");

        increaseHealthButton.addActionListener(e -> modifyHealthAndResources(1, 0));
        decreaseHealthButton.addActionListener(e -> modifyHealthAndResources(-1, 0));
        increaseResourceButton.addActionListener(e -> modifyHealthAndResources(0, 5));
        decreaseResourceButton.addActionListener(e -> modifyHealthAndResources(0, -5));

        // Buff dropdown
        buffSelector = new JComboBox<>();
        File buffsDir = new File("assets/buffs");

        if (buffsDir.exists() && buffsDir.isDirectory()) {
            File[] buffFiles = buffsDir.listFiles();
            if (buffFiles != null) {
                for (File buffFile : buffFiles) {
                    if (buffFile.isFile()) { // Ensure it's a file, not a directory
                        buffSelector.addItem(buffFile.getName()); // Add filename to dropdown
                    }
                }
            }
        }

        singleBuffButton.addActionListener(e -> addBuff());
        multipleBuffButton.addActionListener(e -> buffParty());
        removeSingleBuffButton.addActionListener(e -> removeSingleBuff());
        removePartyBuffButton.addActionListener(e -> removePartyBuff());

        add(playerSelector);
        add(buffSelector);

        add(increaseHealthButton);
        add(singleBuffButton);

        add(decreaseHealthButton);
        add(removeSingleBuffButton);

        add(increaseResourceButton);
        add(multipleBuffButton);

        add(decreaseResourceButton);
        add(removePartyBuffButton);

        setVisible(true);
    }

    private void modifyHealthAndResources(int healthAmount, int resourceAmount) {
        String selectedPlayer = (String) playerSelector.getSelectedItem();
        for (PlayerPanel player : mainInstance.getPlayers()) {
            if (player.getPlayerName().equals(selectedPlayer)) {
                player.updateValues(player.getCurrentHealth() + healthAmount, player.getCurrentResource() + resourceAmount);
                
                // Get the current buffs for the player to ensure they are preserved
                JSONArray currentBuffs = getPlayerBuffsFromJson(selectedPlayer);
                saveToJson(player.getPlayerName(), player.getCurrentHealth(), currentBuffs);
                break;
            }
        }
    }

    private void addBuff() {
        String selectedPlayer = (String) playerSelector.getSelectedItem();
        String selectedBuff = (String) buffSelector.getSelectedItem();
    
        if (selectedBuff == null) return;
    
        for (PlayerPanel player : mainInstance.getPlayers()) {
            if (player.getPlayerName().equals(selectedPlayer)) {
                // Get existing buffs from JSON
                JSONArray buffs = getPlayerBuffsFromJson(selectedPlayer);
    
                // Check if the buff already exists
                if (buffs.contains(selectedBuff)) {
                    System.out.println("Buff already applied to player: " + selectedBuff);
                    return; // Exit early if buff is already present
                }
    
                // Add buff and update player data
                player.addBuff(selectedBuff);
                buffs.add(selectedBuff);
                saveToJson(player.getPlayerName(), player.getCurrentHealth(), buffs);
                break;
            }
        }
    }

    private void buffParty() {
        String selectedBuff = (String) buffSelector.getSelectedItem();

        if (selectedBuff == null) return;

        for (PlayerPanel player : mainInstance.getPlayers()) {
                // Get existing buffs from JSON
                JSONArray buffs = getPlayerBuffsFromJson(player.getPlayerName());
    
                // Check if the buff already exists
                if (!buffs.contains(selectedBuff)) {
                    // Add buff and update player data
                    player.addBuff(selectedBuff);
                    buffs.add(selectedBuff);
                    saveToJson(player.getPlayerName(), player.getCurrentHealth(), buffs);
                }

        }
    }

    private void removeSingleBuff() {
        String selectedPlayer = (String) playerSelector.getSelectedItem();
        String selectedBuff = (String) buffSelector.getSelectedItem();
    
        if (selectedBuff == null) return;
    
        for (PlayerPanel player : mainInstance.getPlayers()) {
            if (player.getPlayerName().equals(selectedPlayer)) {
                // Get existing buffs from JSON
                JSONArray buffs = getPlayerBuffsFromJson(selectedPlayer);
    
                // Check if the buff already exists
                if (!buffs.contains(selectedBuff)) {
                    System.out.println("Buff not applied to player: " + selectedBuff);
                    return; // Exit early if buff is already present
                }
    
                // Remove buff and update player data
                player.removeBuff(selectedBuff);
                buffs.remove(selectedBuff);
                saveToJson(player.getPlayerName(), player.getCurrentHealth(), buffs);
                break;
            }
        }
    }

    private void removePartyBuff() {
        String selectedBuff = (String) buffSelector.getSelectedItem();

        if (selectedBuff == null) return;

        for (PlayerPanel player : mainInstance.getPlayers()) {
                // Get existing buffs from JSON
                JSONArray buffs = getPlayerBuffsFromJson(player.getPlayerName());
    
                // Check if the buff already exists
                if (buffs.contains(selectedBuff)) {
                     // Remove buff and update player data
                    player.removeBuff(selectedBuff);
                    buffs.remove(selectedBuff);
                    saveToJson(player.getPlayerName(), player.getCurrentHealth(), buffs);
                }
        }
    }

    private JSONArray getPlayerBuffsFromJson(String playerName) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config/players.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray playersArray = (JSONArray) jsonObject.get("players");

            for (Object o : playersArray) {
                JSONObject playerObj = (JSONObject) o;
                if (playerObj.get("name").equals(playerName)) {
                    JSONArray playerBuffs = (JSONArray) playerObj.get("buffs");
                    return playerBuffs != null ? playerBuffs : new JSONArray();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private void saveToJson(String playerName, int newHealth, JSONArray buffs) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("config/players.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray playersArray = (JSONArray) jsonObject.get("players");

            for (Object o : playersArray) {
                JSONObject playerObj = (JSONObject) o;
                if (playerObj.get("name").equals(playerName)) {
                    playerObj.put("currentHealth", newHealth);
                    playerObj.put("buffs", buffs);
                    break;
                }
            }

            // Pretty-print JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(jsonObject);

            try (FileWriter file = new FileWriter("config/players.json")) {
                file.write(prettyJson);
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
