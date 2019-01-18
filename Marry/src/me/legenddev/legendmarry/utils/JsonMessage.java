package me.legenddev.legendmarry.utils;

import org.bukkit.*;
import org.bukkit.entity.*;
import java.util.*;
import org.json.simple.*;

public class JsonMessage
{
    private String msg;
    
    public JsonMessage() {
        this.msg = "[{\"text\":\"\",\"extra\":[{\"text\": \"\"}";
    }
    
    private static Class<?> getNmsClass(final String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }
    
    private static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
    
    public void send() {
        final List<Object> players = new ArrayList<Object>();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            players.add(p);
        }
        this.send((Player[])players.toArray(new Player[players.size()]));
    }
    
    public void send(final Player... player) {
        final String version = getServerVersion();
        final String nmsClass = String.valueOf(String.valueOf(version.startsWith("v1_8_R") ? "IChatBaseComponent$" : "")) + "ChatSerializer";
        for (final Player p : player) {
            try {
                final Object comp = getNmsClass(nmsClass).getMethod("a", String.class).invoke(null, String.valueOf(String.valueOf(this.msg)) + "]}]");
                final Object packet = getNmsClass("PacketPlayOutChat").getConstructor(getNmsClass("IChatBaseComponent")).newInstance(comp);
                final Object handle = p.getClass().getMethod("getHandle", (Class<?>[])new Class[0]).invoke(p, new Object[0]);
                final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNmsClass("Packet")).invoke(playerConnection, packet);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public JsonStringBuilder append(final String text) {
        return new JsonStringBuilder(this, esc(text), null);
    }
    
    private static String esc(final String s) {
        return JSONObject.escape(s);
    }
    
    static void access$2(final JsonMessage jsonMessage, final String msg) {
        jsonMessage.msg = msg;
    }
    
    public static class JsonStringBuilder
    {
        private JsonMessage message;
        private String string;
        private String hover;
        private String click;
        
        private JsonStringBuilder(final JsonMessage msg, final String text, Object object) {
            this.hover = "";
            this.click = "";
            this.message = msg;
            this.string = ",{\"text\":\"" + text + "\"";
        }
        
        public JsonStringBuilder setHoverAsTooltip(final String... lore) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lore.length; ++i) {
                if (i + 1 == lore.length) {
                    builder.append(lore[i]);
                }
                else {
                    builder.append(String.valueOf(String.valueOf(lore[i])) + "\n");
                }
            }
            this.hover = ",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + esc(builder.toString()) + "\"}";
            return this;
        }
        
        public JsonStringBuilder setHoverAsAchievement(final String ach) {
            this.hover = ",\"hoverEvent\":{\"action\":\"show_achievement\",\"value\":\"achievement." + esc(ach) + "\"}";
            return this;
        }
        
        public JsonStringBuilder setClickAsURL(final String link) {
            this.click = ",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + esc(link) + "\"}";
            return this;
        }
        
        public JsonStringBuilder setClickAsSuggestCmd(final String cmd) {
            this.click = ",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + esc(cmd) + "\"}";
            return this;
        }
        
        public JsonStringBuilder setClickAsExecuteCmd(final String cmd) {
            this.click = ",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + esc(cmd) + "\"}";
            return this;
        }
        
        public JsonMessage save() {
            final JsonMessage message = this.message;
            JsonMessage.access$2(message, String.valueOf(String.valueOf(message.msg)) + this.string + this.hover + this.click + "}");
            return this.message;
        }
    }
}
