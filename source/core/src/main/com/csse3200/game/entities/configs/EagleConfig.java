package com.csse3200.game.entities.configs;

/**
 * Defines the Eagle's statistics stored in eagle config files to be loaded by the NPC factory.
 */
public class EagleConfig extends BaseEntityConfig {
    protected EagleConfig() {
        this.baseHint = new String[][]{
                { "Welcome to Animal Kingdom!", "I am Ethan the Eagle.", "/cWhich tip do you wanna hear about?/s01What do potions do???/s02How to beat the final boss/s03Nothing. Bye" },
                { "Potions heals you by (n) HP!", "I hope this helped." },
                { "Final boss?? That Kangaroo??", "idk" },
                { "Good luck!" }
        };
        this.spritePath = "images/eagle.atlas";
        this.animationSpeed = 0.1f;
        this.soundPath = new String[] {"sounds/eagle-scream.mp3"};
        this.health = 25;
        this.baseAttack = 0;
        this.animalName = "Eagle";
    }
}

//String[][] baseHint =
//        {
//            {"Text": "Welcome to Animal Kingdom!", "Options": null},
//            {"Text": "I am Ethan the Eagle.", "Options": null},
//            {"Text": "Which tip do you wanna hear about?", "Options": {
//                "What do potions do???": {
//                    {"Text": "Potions heals you by (n) HP!", "Options": null},
//                    {"Text": "I hope this helped.", "Options": null}
//                },
//                "How to beat the final boss": {
//                    {"Text": "Final boss?? That Kangaroo??", "Options": null},
//                    {"Text": "idk", "Options": null}
//                },
//                "Nothing. Bye": {
//                    {"Text": "Good luck!", "Options": null}
//                }
//            }}
//        }