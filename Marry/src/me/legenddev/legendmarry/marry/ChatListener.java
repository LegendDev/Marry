package me.legenddev.legendmarry.marry;

import org.bukkit.entity.*;
import org.bukkit.event.player.*;

import me.legenddev.legendmarry.*;
import me.legenddev.legendmarry.filemanager.*;

import org.bukkit.event.*;

public class ChatListener implements Listener
{
    Main plugin;
    
    public ChatListener(final Main i) {
        this.plugin = i;
    }
    
    public String marryPrefix(final Player p) {
        final PlayerFile file = GetPlayerFile.getPlayerFile(p);
        if (Main.getPlugin().getConfig().getString("EnablePrefix").equalsIgnoreCase("true") && file.getString("isMarried") != null) {
            return new StringBuilder(String.valueOf(Main.getPlugin().getConfig().getString("MarriedPrefix").replace("&", "§"))).toString();
        }
        return "";
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        String format = e.getFormat();
        format = String.valueOf(String.valueOf(this.marryPrefix(p))) + format;
        e.setFormat(format);
    }
}
