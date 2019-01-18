package me.legenddev.legendmarry.filemanager;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class PlayerClass implements Listener
{
    public static void loadPlayer(final Player p) {
        try {
            GetPlayerFile.getPlayerFile(p);
        }
        catch (Exception ex) {}
    }
    
    public static void unloadPlayer(final Player p) {
    }
    
    public static boolean hasBought(final Player p) {
        return false;
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        loadPlayer(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        unloadPlayer(e.getPlayer());
    }
}
