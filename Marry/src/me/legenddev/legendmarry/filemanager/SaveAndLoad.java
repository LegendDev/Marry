package me.legenddev.legendmarry.filemanager;

import java.io.*;

public class SaveAndLoad
{
    public static <T> void save(final T obj, final String path) throws Exception {
        final ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }
    
    public static <T> T load(final String path) throws Exception {
        final ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        final T result = (T)ois.readObject();
        ois.close();
        return result;
    }
    
    public static void loadHashmaps() {
    }
    
    public static void saveHashmaps() {
    }
}
