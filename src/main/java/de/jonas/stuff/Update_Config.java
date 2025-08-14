package de.jonas.stuff;

import org.bukkit.configuration.file.FileConfiguration;

public class Update_Config {

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration config = stuff.getConfig();

    public Update_Config() {
        switch (config.getString("config")) {
            case "1":
                // Add CraftingRecipes section
                config.set("CraftingRecipes.Enabled", true);
                
                // Add light recipe
                config.set("CraftingRecipes.light.Enabled", true);
                config.set("CraftingRecipes.light.shaped", true);
                config.set("CraftingRecipes.light.count", 2);
                config.set("CraftingRecipes.light.publish", true);
                config.set("CraftingRecipes.light.shape", new String[]{"XAX", "XBX", "XXX"});
                config.set("CraftingRecipes.light.ingredients.A", "glass");
                config.set("CraftingRecipes.light.ingredients.B", "torch");
                
                // Add stone recipe
                config.set("CraftingRecipes.stone.Enabled", false);
                config.set("CraftingRecipes.stone.shaped", false);
                config.set("CraftingRecipes.stone.publish", false);
                config.set("CraftingRecipes.stone.count", 1);
                config.set("CraftingRecipes.stone.ingredients", new String[]{"stone"});

                // update version
                config.set("config", "2");
                stuff.saveConfig();
                break;
        
            default:
                break;
        }
    }

}
