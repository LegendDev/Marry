package me.legenddev.legendmarry.filemanager;

import org.bukkit.entity.*;
import org.bukkit.*;

public class GetPlayerFile
{
    public static PlayerFile getPlayerFile(final Player p) {
        final String uuid = p.getUniqueId().toString();
        return new PlayerFile("plugins/Marry/UserData/" + uuid + ".yml");
    }
    
    public static PlayerFile getPlayerFile(final OfflinePlayer p) {
        final String uuid = p.getUniqueId().toString();
        return new PlayerFile("plugins/Marry/UserData/" + uuid + ".yml");
    }
}
