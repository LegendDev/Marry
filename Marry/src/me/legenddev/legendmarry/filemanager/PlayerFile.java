package me.legenddev.legendmarry.filemanager;

import java.io.*;
import java.util.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.*;

public class PlayerFile
{
    private File file;
    private YamlConfiguration yaml;
    
    public PlayerFile(final File file) {
        this.file = null;
        this.yaml = new YamlConfiguration();
        this.file = file;
        this.load();
    }
    
    public PlayerFile(final String path) {
        this.file = null;
        this.yaml = new YamlConfiguration();
        this.file = new File(path);
        this.load();
    }
    
    public void createFile() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException ex) {}
        }
    }
    
    private void load() {
        try {
            this.yaml.load(this.file);
        }
        catch (Exception ex) {}
    }
    
    public void save() {
        try {
            this.yaml.save(this.file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void delete() {
        try {
            this.file.delete();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public final File getFile(final String PlayerName) {
        return this.file;
    }
    
    public int getInt(final String s) {
        return this.yaml.getInt(s);
    }
    
    public void reload() {
        this.save();
        this.load();
    }
    
    public String getString(final String s) {
        return this.yaml.getString(s);
    }
    
    public Object get(final String s) {
        return this.yaml.get(s);
    }
    
    public boolean getBoolean(final String s) {
        return this.yaml.getBoolean(s);
    }
    
    public void add(final String s, final Object o) {
        if (!this.contains(s)) {
            this.set(s, o);
            this.save();
        }
    }
    
    public void addToStringList(final String s, final String o) {
        this.yaml.getStringList(s).add(o);
        this.save();
    }
    
    public void removeFromStringList(final String s, final String o) {
        this.yaml.getStringList(s).remove(o);
        this.save();
    }
    
    public List<String> getStringList(final String s) {
        return (List<String>)this.yaml.getStringList(s);
    }
    
    public void addToIntegerList(final String s, final int o) {
        this.yaml.getIntegerList(s).add(o);
        this.save();
    }
    
    public void removeFromIntegerList(final String s, final int o) {
        this.yaml.getIntegerList(s).remove(o);
        this.save();
    }
    
    public List<Integer> getIntegerList(final String s) {
        return (List<Integer>)this.yaml.getIntegerList(s);
    }
    
    public void createNewStringList(final String s, final List<String> list) {
        this.yaml.set(s, (Object)list);
        this.save();
    }
    
    public void createNewIntegerList(final String s, final List<Integer> list) {
        this.yaml.set(s, (Object)list);
        this.save();
    }
    
    public void remove(final String s) {
        this.set(s, null);
        this.save();
    }
    
    public boolean contains(final String s) {
        return this.yaml.contains(s);
    }
    
    public double getDouble(final String s) {
        return this.yaml.getDouble(s);
    }
    
    public void set(final String s, final Object o) {
        this.yaml.set(s, o);
        this.save();
    }
    
    public ConfigurationSection getConfigurationSection(final String s) {
        return this.yaml.getConfigurationSection(s);
    }
    
    public void createConfigurationSection(final String s) {
        this.yaml.createSection(s);
        this.save();
    }
    
    public void increment(final String s) {
        this.yaml.set(s, (Object)(this.getInt(s) + 1));
        this.save();
    }
    
    public void decrement(final String s) {
        this.yaml.set(s, (Object)(this.getInt(s) - 1));
        this.save();
    }
    
    public void increment(final String s, final int i) {
        this.yaml.set(s, (Object)(this.getInt(s) + i));
        this.save();
    }
    
    public void decrement(final String s, final int i) {
        this.yaml.set(s, (Object)(this.getInt(s) - i));
        this.save();
    }
    
    public YamlConfigurationOptions options() {
        return this.yaml.options();
    }
    
    public boolean doesFileExist() {
        return this.file.exists();
    }
    
    public ItemStack getItemStack(final String path) {
        return this.yaml.getItemStack(path);
    }
}
