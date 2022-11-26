package com.royalswans.minesweeper.sweeperfield.runnables;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class LightTNT extends BukkitRunnable {
    private final Block block;

    public LightTNT(Block block) {
        Location loc = block.getLocation();
        loc.setY(loc.getY() - 1);

        this.block = loc.getBlock();
    }

    @Override
    public void run() {
        block.setType(Material.REDSTONE_BLOCK);
        block.setType(Material.OBSIDIAN);
    }
}

