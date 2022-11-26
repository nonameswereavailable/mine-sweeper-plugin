package com.royalswans.minesweeper.sweeperfield;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.awt.*;

public class BuildField {
    public static void buildField(Rectangle rect, Field field, World world) {
        int MinX = (int) rect.getMinX();
        int MaxX = (int) rect.getMaxX();
        int MinZ = (int) rect.getMinY();
        int MaxZ = (int) rect.getMaxY();

        for (int x = MinX - 1; x <= MaxX; x++) {
            for (int z = MinZ - 1; z <= MaxZ; z++) {
                world.getBlockAt(x, field.getY() + 1, z).setType(Material.AIR);

                Block block = world.getBlockAt(x, field.getY(), z);
                if (
                        x == MinX - 1
                        || x == MaxX
                        || z == MinZ - 1
                        || z == MaxZ
                ) {
                    block.setType(Material.PURPUR_BLOCK);
                } else {
                    block.setType(Material.STONE);
                }

                world.playSound(block.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.3f, 1f);
                world.spawnParticle(Particle.EXPLOSION_HUGE, block.getLocation(), 10);
            }
        }
    }
}
