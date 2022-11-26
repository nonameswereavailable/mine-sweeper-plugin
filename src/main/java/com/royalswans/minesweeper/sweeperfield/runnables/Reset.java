package com.royalswans.minesweeper.sweeperfield.runnables;

import com.royalswans.minesweeper.sweeperfield.FieldEvents;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class Reset extends BukkitRunnable {
    private final FieldEvents executor;
    private final World world;

    public Reset(FieldEvents executor, World world) {
        this.executor = executor;
        this.world = world;
    }

    @Override
    public void run() {
        executor.resetField(world);
    }
}
