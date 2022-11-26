package com.royalswans.minesweeper.settertool;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class SetterConfig {
    private static File customConfigFile = null;
    private static FileConfiguration customConfig = null;


    public static void createCustomConfig(JavaPlugin plugin) {
        customConfigFile = new File(plugin.getDataFolder(), "fields.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            plugin.saveResource("fields.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void setField(int startX, int startZ, int endX, int endZ, int y, String world) {
        String time = Long.toString(System.currentTimeMillis());

        if (customConfigFile != null) {

            customConfig.set("fields." + time + ".startX", startX);
            customConfig.set("fields." + time + ".startZ", startZ);
            customConfig.set("fields." + time + ".endX", endX);
            customConfig.set("fields." + time + ".endZ", endZ);
            customConfig.set("fields." + time + ".y", y);
            customConfig.set("fields." + time + ".world", world);


            try {
                customConfig.save(customConfigFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Set<String> getAllFields() {
        if (customConfig == null) {
            return null;
        }

        ConfigurationSection section = customConfig.getConfigurationSection("fields");

        if (section != null) {
            return section.getKeys(false);
        }

        return null;
    }

    public static int getData(String field, String data) {
        if (customConfig == null) {
            return 0;
        }

        return customConfig.getInt("fields." + field + '.' + data);
    }

    public static World getWorld(String field) {
        if (customConfig == null) {
            return null;
        }

        String worldName = customConfig.getString("fields." + field + '.' + "world");

        return Bukkit.getWorld(worldName);
    }
}

