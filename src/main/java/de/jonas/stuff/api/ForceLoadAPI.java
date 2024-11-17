package de.jonas.stuff.api;

import org.bukkit.World;

import de.jonas.stuff.Stuff;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ForceLoadAPI {
    
    MiniMessage mm = MiniMessage.miniMessage();
    Stuff stuff = Stuff.INSTANCE;

    public void forceloadRadius(World world, int chunkX, int chunkZ, int radius) {
        for(int dx = radius; dx >= -radius; dx--)
            for(int dz = radius; dz >= -radius; dz--)
                world.setChunkForceLoaded(chunkX + dx, chunkZ + dz, true);
    }

    public int forceloadRadiusReturnNumber(World world, int chunkX, int chunkZ, int radius) {
        int chunkloaded = 0;
        for(int dx = radius; dx >= -radius; dx--)
            for(int dz = radius; dz >= -radius; dz--) {
                int x = chunkX + dx;
                int z = chunkZ + dz;
                if (!world.isChunkForceLoaded(x, z)) {
                    world.setChunkForceLoaded(x, z, true);
                    chunkloaded++;
                }
            }
        return chunkloaded;
    }

    public void RemoveforceloadRadius(World world, int chunkX, int chunkZ, int radius) {
        for(int dx = radius; dx >= -radius; dx--)
            for(int dz = radius; dz >= -radius; dz--)
                world.setChunkForceLoaded(chunkX + dx, chunkZ + dz, false);
    }

    public int RemoveforceloadRadiusReturnNumber(World world, int chunkX, int chunkZ, int radius) {
        int unloaded = 0;
        for(int dx = radius; dx >= -radius; dx--)
            for(int dz = radius; dz >= -radius; dz--) {
                int x = chunkX + dx;
                int z = chunkZ + dz;
                if (world.isChunkForceLoaded(x, z)) {
                    world.setChunkForceLoaded(x, z, false);
                    unloaded++;
                }
            }
        return unloaded;
    }

}
