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
                config.set("CraftingRecipes.LIGHT.Enabled", true);
                config.set("CraftingRecipes.LIGHT.shaped", true);
                config.set("CraftingRecipes.LIGHT.count", 1);
                config.set("CraftingRecipes.LIGHT.publish", true);
                config.set("CraftingRecipes.LIGHT.shape", new String[]{"'' A '", "'ABA'", "' C '"});
                config.set("CraftingRecipes.LIGHT.ingredients.A", "GLASS_PANE");
                config.set("CraftingRecipes.LIGHT.ingredients.B", "COAL");
                config.set("CraftingRecipes.LIGHT.ingredients.C", "COPPER_INGOT");

                // Add stone recipe
                config.set("CraftingRecipes.STONE.Enabled", false);
                config.set("CraftingRecipes.STONE.shaped", false);
                config.set("CraftingRecipes.STONE.publish", false);
                config.set("CraftingRecipes.STONE.count", 1);
                config.set("CraftingRecipes.STONE.ingredients", new String[]{"STONE"});

                // update version
                config.set("config", "2");
                stuff.saveConfig();
                break;
        
            default:
                break;
        }
    }

}
