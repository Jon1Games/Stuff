package de.jonas.stuff.utility;

import de.jonas.stuff.Stuff;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import java.util.List;
import java.util.logging.Level;

public class PermToOp {

    Stuff stuff = Stuff.INSTANCE;

    public void onEnable() {

        PluginManager pm = Bukkit.getPluginManager();
        List<String> listPOp = stuff.getConfig().getStringList("GiveOpPermission.Permissions");

        stuff.getLogger().log(Level.INFO, "Start Permission assignment");

        for (String a : listPOp) {
            setPermissionDefault(pm, a, PermissionDefault.OP);
            stuff.getLogger().log(Level.INFO, "Permission: \"" + a + "\" assigned to OP");
        }

    }

    public static void setPermissionDefault(PluginManager pm, String permission, PermissionDefault level) {
        Permission perm = pm.getPermission(permission);
        if(perm != null) {
            perm.setDefault(level);
            perm.recalculatePermissibles();
            pm.recalculatePermissionDefaults(perm);
        } else {
            perm = new Permission(permission, level);
            pm.addPermission(perm);
        }
    }

}
