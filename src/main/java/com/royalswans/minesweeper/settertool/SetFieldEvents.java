package com.royalswans.minesweeper.settertool;

import com.royalswans.minesweeper.sweeperfield.FieldEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class SetFieldEvents implements Listener {
    private final JavaPlugin plugin;
    private Block prevBlock = null;

    public SetFieldEvents(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();

        if (!Objects.requireNonNull(e.getHand()).equals(EquipmentSlot.HAND)) { return; }

        if (clickedBlock != null &&
                e.getPlayer().getInventory().getItemInMainHand().isSimilar(SetterTool.getItem())) {
            if (prevBlock != null && !clickedBlock.equals(prevBlock)) {
                e.getPlayer().sendMessage(
                        ChatColor.GREEN + "Set second block to:  "
                                + clickedBlock.getX()
                                + " / "
                                + clickedBlock.getY()
                                + " / "
                                + clickedBlock.getZ());

                int firstX = prevBlock.getX();
                int firstZ = prevBlock.getZ();
                int secondX = clickedBlock.getX();
                int secondZ = clickedBlock.getZ();
                int y = clickedBlock.getY();

                int startX = Math.min(firstX, secondX);
                int startZ = Math.min(firstZ, secondZ);
                int endX = Math.max(firstX, secondX);
                int endZ = Math.max(firstZ, secondZ);

                World world = e.getPlayer().getWorld();

                SetterConfig.setField(startX, startZ, endX, endZ, y, world.getName());

                Bukkit.getPluginManager().registerEvents(new FieldEvents(plugin, startX, startZ, endX, endZ, y, world), plugin);

                prevBlock = null;
            }
            else {
                e.getPlayer().sendMessage(
                        ChatColor.GREEN + "Set first block to: "
                                + clickedBlock.getX()
                                + " / "
                                + clickedBlock.getY()
                                + " / "
                                + clickedBlock.getZ());

                prevBlock = clickedBlock;
            }
        }
    }
}
