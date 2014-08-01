package net.poweredbyhate.creativenogoditems;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public class CNGI extends JavaPlugin implements Listener {

    int maxEnchant;
    int maxNameChars;
    int maxLoreChars;
    String message2Cheater;

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        doConfigStuff();
    }

    @EventHandler
    public void onInvClick(InventoryCreativeEvent ev) {
        if (!(ev.getWhoClicked() instanceof Player)) return;
        Player p = (Player) ev.getWhoClicked();
        ItemStack item = ev.getCursor();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (!p.hasPermission("goditems.bypass")) {
            checkEnchants(p, meta, item);
            checkName(p, meta, item);
            checkLore(p, meta, item);
        }
    }

    public void checkEnchants(Player p, ItemMeta meta, ItemStack item) {
        if (meta.getEnchants() == null) return;
        Map<Enchantment, Integer> enchants = meta.getEnchants();
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            if (entry.getValue().intValue() > maxEnchant) {
                System.out.println("Enchantment got called!");
                item.setTypeId(0);
                p.updateInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', message2Cheater));
            }
        }
    }

    public void checkName(Player p, ItemMeta meta, ItemStack item) {
        if (meta.getDisplayName() == null) return;
        int nameLength = meta.getDisplayName().length();
        if (nameLength > maxNameChars) {
            item.setTypeId(0);
            p.updateInventory();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message2Cheater));
        }
    }

    public void checkLore(Player p, ItemMeta meta, ItemStack item) {
        if (meta.getLore() == null) return;
        List<String> Lores = meta.getLore();
        for (String s : Lores) {
            if (s.length() > maxLoreChars) {
                item.setTypeId(0);
                p.updateInventory();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', message2Cheater));
            }
        }
    }

    public void doConfigStuff() {
        saveDefaultConfig();
        saveConfig();
        message2Cheater = getConfig().getString("MessageToCheater");
        maxEnchant = getConfig().getInt("MaxEnchantLevel");
        maxNameChars = getConfig().getInt("MaxNameChars");
        maxLoreChars = getConfig().getInt("MaxLoreChars");
    }

}
