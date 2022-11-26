package com.royalswans.minesweeper.settertool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class SetterTool {
    private static ItemStack item;

    public static void createTool() {
        item = new ItemStack(Material.STICK);

        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.AQUA + "Setter Tool");
        item.setItemMeta(itemMeta);
    }

    public static ItemStack getItem() {
        return item;
    }
}
