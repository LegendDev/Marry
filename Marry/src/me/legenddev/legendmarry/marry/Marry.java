package me.legenddev.legendmarry.marry;

import org.bukkit.event.player.*;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import me.legenddev.legendmarry.*;
import me.legenddev.legendmarry.filemanager.*;
import me.legenddev.legendmarry.utils.*;

import org.bukkit.event.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.*;

public class Marry implements CommandExecutor, Listener
{
    public static HashMap<String, String> married;
    public static ArrayList<String> request;
    
    static {
        Marry.married = new HashMap<String, String>();
        Marry.request = new ArrayList<String>();
    }
    
    public boolean hasPartner(final Player p) {
        final PlayerFile file = GetPlayerFile.getPlayerFile(p);
        return file.getString("isMarried") != null;
    }
    
    public boolean hasRequest(final Player p) {
        return Marry.request.contains(p.getName());
    }
    
    public String getPartner(final Player p) {
        final PlayerFile file = GetPlayerFile.getPlayerFile(p);
        if (this.hasPartner(p)) {
            return file.getString("Married");
        }
        return "";
    }
    
    @EventHandler
    public void onEntitySex(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player) {
            final Player p = e.getPlayer();
            final Player t = (Player)e.getRightClicked();
            if (this.getPartner(p).equalsIgnoreCase(t.getName()) && this.getPartner(t).equalsIgnoreCase(p.getName()) && p.isSneaking()) {
                e.setCancelled(true);
                ParticleEffect.HEART.display(1.0f, 1.0f, 1.0f, 20.0f, 100, p.getLocation(), p);
                final int baby = new Random().nextInt(50);
                if (baby == 1) {
                    @SuppressWarnings({ "unchecked", "rawtypes" })
					final Cow Cow = (Cow)t.getWorld().spawn(t.getLocation(), (Class)Cow.class);
                    Cow.setBaby();
                    Cow.setCustomName(Main.getPlugin().getConfig().getString("BabyName").replace("&", "§").replace("%player1%", p.getName()).replace("%player2%", t.getName()));
                    Cow.setCustomNameVisible(true);
                    new BukkitRunnable() {
                        public void run() {
                            if (Cow != null) {
                                Cow.setAdult();
                                Cow.setCustomName(Main.getPlugin().getConfig().getString("KidName").replace("&", "§").replace("%player1%", p.getName()).replace("%player2%", t.getName()));
                                Cow.setCustomNameVisible(true);
                            }
                        }
                    }.runTaskLater(Main.getPlugin(), 600L);
                }
            }
        }
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("marry")) {
            final Player p = (Player)sender;
            if (args.length < 1 || args.length > 1) {
                p.sendMessage("§eMarry Commands - Developed By QuadFeed");
                p.sendMessage("§e/Marry (Player) §6- Marry a player.");
                p.sendMessage("§e/Marry Accept §6- Accept a marriage request.");
                p.sendMessage("§e/Marry Reject §6- Reject a marriage request.");
                p.sendMessage("§e/Marry Divorce §6- Divorce your partner.");
                p.sendMessage("§e/Marry Gift §6- Gift your partner the item in your hand.");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("accept")) {
                    if (this.hasPartner(p)) {
                        p.sendMessage("§6You're already married!");
                        return true;
                    }
                    if (!this.hasRequest(p)) {
                        p.sendMessage("§6You don't have a request to be married!");
                        return true;
                    }
                    final Player t = Bukkit.getPlayer((String)Marry.married.get(p.getName()));
                    if (t == null) {
                        final OfflinePlayer offline = Bukkit.getOfflinePlayer((String)Marry.married.get(p.getName()));
                        final PlayerFile p2 = GetPlayerFile.getPlayerFile(p);
                        final PlayerFile p3 = GetPlayerFile.getPlayerFile(offline);
                        p2.set("Married", offline.getName());
                        p2.set("isMarried", "Yes");
                        p2.save();
                        p3.set("Married", p.getName());
                        p3.set("isMarried", "Yes");
                        p3.save();
                        p.sendMessage("§6You may now kiss the bride!");
                        Marry.request.remove(p.getName());
                        Marry.married.remove(p.getName());
                        return true;
                    }
                    final PlayerFile p4 = GetPlayerFile.getPlayerFile(p);
                    final PlayerFile p5 = GetPlayerFile.getPlayerFile(t);
                    p4.set("Married", t.getName());
                    p4.set("isMarried", "Yes");
                    p4.save();
                    p5.set("Married", p.getName());
                    p5.set("isMarried", "Yes");
                    p5.save();
                    p.sendMessage("§6You may now kiss the bride!");
                    t.sendMessage("§6You may now kiss the bride!");
                    Marry.request.remove(p.getName());
                    Marry.married.remove(p.getName());
                    return true;
                }
                else if (args[0].equalsIgnoreCase("reject")) {
                    if (this.hasPartner(p)) {
                        p.sendMessage("§eYou're already married!");
                        return true;
                    }
                    if (!this.hasRequest(p)) {
                        p.sendMessage("§eYou don't have a request to be married!");
                        return true;
                    }
                    final Player t = Bukkit.getPlayer((String)Marry.married.get(p.getName()));
                    if (t == null) {
                        p.sendMessage("§eYou have rejected your request to be married!");
                        Marry.request.remove(p.getName());
                        Marry.married.remove(p.getName());
                        return true;
                    }
                    p.sendMessage("§eYou have rejected §6" + t.getName());
                    t.sendMessage("§6" + p.getName() + "§e has rejected your request!");
                    Marry.request.remove(p.getName());
                    Marry.married.remove(p.getName());
                    return true;
                }
                else if (args[0].equalsIgnoreCase("gift")) {
                    if (!this.hasPartner(p)) {
                        p.sendMessage("§eYou're aren't married!");
                        return true;
                    }
                    final PlayerFile file = GetPlayerFile.getPlayerFile(p);
                    final Player t2 = Bukkit.getPlayer(file.getString("Married"));
                    if (t2 == null) {
                        p.sendMessage("§eYou have spouse is not online!");
                        return true;
                    }
                    if (p.getItemInHand().getType() == Material.AIR) {
                        p.sendMessage("§eYou must have something in your hand!");
                        return true;
                    }
                    p.sendMessage("§6You have gifted§e " + t2.getName() + " §eyour item in hand!");
                    t2.sendMessage("§e" + p.getName() + " §ehas gifted you an item!");
                    if (t2.getInventory().firstEmpty() == -1) {
                        t2.getWorld().dropItemNaturally(t2.getEyeLocation(), p.getItemInHand());
                    }
                    else {
                        t2.getInventory().addItem(new ItemStack[] { p.getItemInHand() });
                    }
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR, 1));
                    return true;
                }
                else if (args[0].equalsIgnoreCase("check")) {
                    if (!this.hasPartner(p)) {
                        p.sendMessage("§eYou're aren't married!");
                        return true;
                    }
                    final PlayerFile file = GetPlayerFile.getPlayerFile(p);
                    final Player t2 = Bukkit.getPlayer(file.getString("Married"));
                    p.sendMessage("§eYou are married to " + t2);
                    return true;
                }
                else if (args[0].equalsIgnoreCase("divorce")) {
                    if (!this.hasPartner(p)) {
                        p.sendMessage("§eYou're aren't married!");
                        return true;
                    }
                    final PlayerFile file = GetPlayerFile.getPlayerFile(p);
                    final Player t2 = Bukkit.getPlayer(file.getString("Married"));
                    if (t2 == null) {
                        final OfflinePlayer offline2 = Bukkit.getOfflinePlayer(file.getString("Married"));
                        final PlayerFile p6 = GetPlayerFile.getPlayerFile(p);
                        final PlayerFile p7 = GetPlayerFile.getPlayerFile(offline2);
                        p6.set("Married", null);
                        p6.set("isMarried", null);
                        p6.save();
                        p7.set("Married", null);
                        p7.set("isMarried", null);
                        p7.save();
                        p.sendMessage("§6You have divorced your spouse!");
                        return true;
                    }
                    final PlayerFile p2 = GetPlayerFile.getPlayerFile(p);
                    final PlayerFile p3 = GetPlayerFile.getPlayerFile(t2);
                    p2.set("Married", null);
                    p2.set("isMarried", null);
                    p2.save();
                    p3.set("Married", null);
                    p3.set("isMarried", null);
                    p3.save();
                    p.sendMessage("§eYou have divorced your spouse!");
                    t2.sendMessage("§eYour spouse has divorced you!");
                    Marry.request.remove(p.getName());
                    Marry.married.remove(p.getName());
                    return true;
                }
                else {
                    final Player t = Bukkit.getPlayer(args[0]);
                    if (t == null) {
                        p.sendMessage("§eThe person you're trying to marry is offline");
                        return true;
                    }
                    if (t == p) {
                        p.sendMessage("§eYou cannot marry yourself");
                        return true;
                    }
                    if (this.hasPartner(p)) {
                        p.sendMessage("§eYou're already married!");
                        return true;
                    }
                    if (this.hasPartner(t)) {
                        p.sendMessage("§6" + t.getName() + " §eis already married!");
                        return true;
                    }
                    if (this.hasRequest(p)) {
                        p.sendMessage("§6" + t.getName() + " §ealready has a request to be married!");
                        return true;
                    }
                    if (this.hasRequest(t)) {
                        p.sendMessage("§6" + t.getName() + " §ealready has a request to be married!");
                        return true;
                    }
                    p.sendMessage("§eYou have requested §6" + t.getName() + "§e to marry you.");
                    t.sendMessage("§6" + p.getName() + " §ehas requested to marry you. (You have 30 seconds to accept or reject this)");
                    t.sendMessage("§6Type /marry accept to accept this request");
                    t.sendMessage("§6Type /marry reject to deny this request");
                    Marry.request.add(t.getName());
                    Marry.married.put(t.getName(), p.getName());
                    new BukkitRunnable() {
                        public void run() {
                            if (Marry.request.contains(t.getName())) {
                                Marry.request.remove(t.getName());
                                Marry.married.remove(t.getName());
                                t.sendMessage("§eYour time ran out to accept this marriage!");
                                p.sendMessage("§eYour request timed out to get married!");
                            }
                        }
                    }.runTaskLater(Main.getPlugin(), 600L);
                }
            }
        }
        return false;
    }
}
