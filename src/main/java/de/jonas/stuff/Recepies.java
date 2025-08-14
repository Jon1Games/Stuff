package de.jonas.stuff;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class Recepies implements Listener{

    Stuff stuff = Stuff.INSTANCE;
    FileConfiguration config = stuff.getConfig();

    List<NamespacedKey> recipes = new ArrayList<>();

    public Recepies() {
        loadConfig();
    }

    public void loadConfig() {
        ConfigurationSection section = config.getConfigurationSection("CraftingRecipes");
        for (String key : section.getKeys(false)) {
            if (key.equalsIgnoreCase("Enabled")) continue;

            ConfigurationSection cmd = section.getConfigurationSection(key);
            if (cmd == null) continue;

            NamespacedKey namespacedKey = new NamespacedKey(stuff, "craftingrecipe_" + key);

            if (cmd.getBoolean("shaped")) {
                ShapedRecipe r = new ShapedRecipe(namespacedKey, new ItemStack(Material.getMaterial(key), cmd.getInt("count")));
                r.shape(cmd.getStringList("shape").toArray(new String[0]));

                for (String ingredient : cmd.getConfigurationSection("ingredients").getKeys(false)) {
                    r.setIngredient(ingredient.charAt(0), Material.getMaterial(cmd.getString("ingredients." + ingredient)));
                }

                addRecipe(r, namespacedKey, cmd.getBoolean("publish", true));
            } else {
                ShapelessRecipe r = new ShapelessRecipe(namespacedKey, new ItemStack(Material.getMaterial(key), cmd.getInt("count")));
                for (String ingredient : cmd.getStringList("ingredients")) {
                    r.addIngredient(1, Material.getMaterial(ingredient));
                }

                addRecipe(r, namespacedKey, cmd.getBoolean("publish", true));
            }
        }
    }

    /**
     * Adds a recipe to the server.
     *
     * @param recipe  The recipe to add.
     * @param key     The key associated with the recipe.
     * @param publish Whether to publish the recipe to all players.
     */
    public void addRecipe(Recipe recipe, NamespacedKey key, boolean publish) {
        stuff.getServer().addRecipe(recipe);
        stuff.getServer().updateRecipes();
        if (publish) {
            recipes.add(key);
        }
    }

    @EventHandler
    public void onJoinAddRecipe(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (NamespacedKey key : recipes) {
            player.discoverRecipe(key);
        }
    }

}
