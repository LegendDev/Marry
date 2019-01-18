package me.legenddev.legendmarry;

import org.bukkit.plugin.java.*;

import me.legenddev.legendmarry.marry.*;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.configuration.file.*;
import org.bukkit.*;
import org.bukkit.plugin.*;

public class Main extends JavaPlugin implements Listener
{
    public static Plugin plugin;
    
    public void onEnable() {
        this.getServer().getConsoleSender().sendMessage("");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Trying to enable Marry...");
        this.getServer().getConsoleSender().sendMessage("");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Reading startup website");
        this.getServer().getConsoleSender().sendMessage("");
        this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Success! Startup website read true");
        this.getServer().getConsoleSender().sendMessage("");
        this.getCommand("marry").setExecutor((CommandExecutor)new Marry());
        this.registerEvents();
        final FileConfiguration cfg = this.getConfig();
        cfg.options().copyDefaults(true);
        this.saveDefaultConfig();
        this.loadConfigValues();
        getPlugin().saveDefaultConfig();
    }
    
    public static Plugin getPlugin() {
        return Bukkit.getPluginManager().getPlugin("Marry");
    }
    
    public void loadConfigValues() {
    }
    
    public void registerEvents() {
        final PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)this, (Plugin)this);
        pm.registerEvents((Listener)new Marry(), (Plugin)this);
        pm.registerEvents((Listener)new ChatListener(this), (Plugin)this);
    }
}
