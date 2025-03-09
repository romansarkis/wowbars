package com.romansarkis;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerLoader {
    public static List<PlayerPanel> loadPlayers() {
        List<PlayerPanel> players = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File("config/players.json"));

            for (JsonNode node : root.get("players")) {
                String name = node.get("name").asText();
                int level = node.get("level").asInt();
                int maxHealth = node.get("maxHealth").asInt();
                int currentHealth = node.get("currentHealth").asInt();
                int maxResource = node.get("maxResource").asInt();
                int currentResource = node.get("currentResource").asInt();
                String resourceType = node.get("type").asText();
                String portraitPath = "assets/" + node.get("portrait").asText();

                // Parse player buff list
                List<String> buffs = new ArrayList();
                JsonNode buffsNode = node.get("buffs");
                if (buffsNode != null && buffsNode.isArray()) {
                    for (JsonNode buff : buffsNode) {
                        buffs.add(buff.asText());
                    }
                }

                players.add(new PlayerPanel(name, level, maxHealth, currentHealth, maxResource, currentResource, resourceType, portraitPath, buffs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }
}
