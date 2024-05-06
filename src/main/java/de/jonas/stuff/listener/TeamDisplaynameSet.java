package de.jonas.stuff.listener;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import de.jonas.stuff.Stuff;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import dev.jorel.commandapi.CommandAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPerms;
import org.jetbrains.annotations.NotNull;

public class TeamDisplaynameSet implements Listener {

    Stuff stuff = Stuff.INSTANCE;

    public Scoreboard scoreboard;

    private NumberFormat teamWeightFormat;

    public Map<OfflinePlayer, PlayerTeam> teams;

    public void onLoad() {
        teams = new HashMap<>();
        if (stuff.getConfig().getBoolean("Format.PlayerNames.Enabled")) {
            CommandAPI.unregister("tm", true);
            CommandAPI.unregister("teammsg", true);
            CommandAPI.unregister("team", true);
        }
        teamWeightFormat = NumberFormat.getIntegerInstance();
        teamWeightFormat.setMinimumIntegerDigits(String.valueOf(stuff.getConfig()
                .getInt("Format.PlayerNames.TabSorting.MaxWeight")).length());
    }

    public void onEnable() {
        scoreboard = stuff.getServer().getScoreboardManager().getMainScoreboard();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(@NotNull PlayerJoinEvent e) {
        Player player = e.getPlayer();
        onJoinPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent e) {
            Player p = e.getPlayer();
            teams.remove(p);

            removeTeam(scoreboard, p);
            Bukkit.getOnlinePlayers().forEach(pl -> removeTeam(pl.getScoreboard(), p));
    }

    private void addTeam(Scoreboard sb, PlayerTeam pti, OfflinePlayer p) {
        Team pt = sb.getPlayerTeam(p);
        if(pt != null) {
            pt.removePlayer(p);
            if(pt.getSize() == 0) pt.unregister();
        }
        try {
            pt = sb.registerNewTeam(pti.id);
        } catch(IllegalArgumentException e) {
            pt = sb.getTeam(pti.id);
        }
        if (stuff.getConfig().getBoolean("Format.PlayerNames.Prefix.Enabled")) {
            String prefix = stuff.getConfig().getString("Format.PlayerNames.Prefix.Prefix");
            if (prefix != null) pt.prefix(pti.prefix);
        }
        if (stuff.getConfig().getBoolean("Format.PlayerNames.Suffix.Enabled")) {
            String suffix = stuff.getConfig().getString("Format.PlayerNames.Suffix.Suffix");
            if (suffix != null) pt.suffix(pti.suffix);
        }
        pt.addPlayer(p);
    }

    public void removeTeam(Scoreboard sb, Player p) {
        Team pt = sb.getPlayerTeam(p);
        if(pt != null) {
            pt.removePlayer(p);
            if(pt.getSize() == 0) pt.unregister();
        }
    }

    private static class PlayerTeam {
        protected String id;
        protected Component prefix;
        protected Component suffix;
    }

    private int getWeight(Player p) {
        LuckPerms lp = LuckPermsProvider.get();
        String weight = lp.getUserManager().getUser(p.getUniqueId()).getCachedData().getMetaData().getMetaValue("weight");
        try {
            return Integer.parseInt(weight);
        } catch(Exception e) {
            return 0;
        }
    }

    public void onJoinPlayer(Player player) {
        var mm = MiniMessage.miniMessage();
        String playerTeamID;
        if (stuff.getConfig().getBoolean("Format.PlayerNames.TabSorting.Enabled")) {
            int weight = stuff.getConfig().getInt("Format.PlayerNames.TabSorting.MaxWeight") - getWeight(player);
            String playerWeight = teamWeightFormat.format(weight);
            String teamId = "<weight>_<player_name>";
            playerTeamID = PlainTextComponentSerializer.plainText().serialize(
                    mm.deserialize(teamId, Placeholder.unparsed("weight", playerWeight),
                            Placeholder.unparsed("player_name", player.getName())));
        } else {
            String teamId = "<player_name>";
            playerTeamID = PlainTextComponentSerializer.plainText().serialize(
                    mm.deserialize(teamId, Placeholder.unparsed("player_name", player.getName())));
        }

        PlayerTeam pti = new PlayerTeam();
        pti.id = playerTeamID;
        String prefix = null;
        Component prefixcom = null;
        if (stuff.getConfig().getBoolean("Format.PlayerNames.Prefix.Enabled")) {
            prefix = stuff.getConfig().getString("Format.PlayerNames.Prefix.Prefix");
            if (prefix != null) {
                prefix = PlaceholderAPI.setPlaceholders(player, prefix);
                prefixcom = LegacyComponentSerializer.legacy('&').deserialize(prefix);
                pti.prefix = prefixcom;
            } else {
                stuff.logger.log(Level.WARNING, "Prefix is not defined!");
            }
        }
        String suffix = null;
        Component suffixcom = null;
        if (stuff.getConfig().getBoolean("Format.PlayerNames.Suffix.Enabled")) {
            suffix = stuff.getConfig().getString("Format.PlayerNames.Suffix.Suffix");
            if (suffix != null) {
                suffix = PlaceholderAPI.setPlaceholders(player, suffix);
                suffixcom = LegacyComponentSerializer.legacy('&').deserialize(suffix);
                pti.suffix = suffixcom;
            } else {
                stuff.logger.log(Level.WARNING, "Suffix is not defined!");
            }
        }
        teams.put(player, pti);

        addTeam(scoreboard, pti, player);
        Bukkit.getScheduler().runTaskLater(stuff, () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> addTeam(pl.getScoreboard(), pti, player));

            Scoreboard lscore = player.getScoreboard();
            teams.forEach((pl, opti) -> {
                if (pl != player) addTeam(lscore, opti, pl);
            });
        }, 1);
        Component tabNameCom = Component.join(JoinConfiguration.separator(Component.text("")), prefixcom,
                player.displayName(), suffixcom);
        Bukkit.getScheduler().runTaskLater(stuff,
                () -> player.playerListName(),
                1);
    }
}
