package com.royalswans.minesweeper.settertool;

import com.royalswans.minesweeper.sweeperfield.FieldEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class Init {
    public static void init(JavaPlugin plugin) {
        if (SetterConfig.getAllFields() == null) {
            return;
        }

        Set<String> fields = SetterConfig.getAllFields();

        int startX;
        int startZ;
        int endX;
        int endZ;
        int y;

        for (String field : fields) {
            startX = SetterConfig.getData(field, "startX");
            startZ = SetterConfig.getData(field, "startZ");
            endX = SetterConfig.getData(field, "endX");
            endZ = SetterConfig.getData(field, "endZ");
            y = SetterConfig.getData(field, "y");

            Bukkit.getPluginManager().registerEvents(new FieldEvents(plugin, startX, startZ, endX, endZ, y), plugin);
        }
    }
}
