# WoW Bars v0.4
World of Warcraft themed status GUI display for Dungeons &amp; Dragons campaign.

# Setup Instructions

1. Install Maven and add MAVEN_HOME to Environment Variables

    Example entry in User Variables:
        
        Variable name: MAVEN_HOME
        
        Variable value: C:\Program Files\Apache\Maven\apache-maven-3.x.x

2. Clone the repository:
    
        git clone https://github.com/romansarkis/wowbars.git
        cd <project_directory>

3. Configue the JSON data to match your DnD party, respective files should go into assets
        
        Here is an example entry in players.json, note that the type must be either rage, mana, or stamina:

        {
        "currentHealth": 6892,
        "level": 60,
        "clss": "Warrior",
        "name": "Alcorsx",
        "maxHealth": 6892,
        "currentResource": 100,
        "buffs": [
                "battleshout.png"
        ],
        "maxResource": 100,
        "type": "rage",
        "portrait": "alcorsx.png"
        }


4. Within the root directory, run `mvn compile` then `mvn exec:java`
