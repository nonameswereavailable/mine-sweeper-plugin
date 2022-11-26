package com.royalswans.minesweeper;

import com.royalswans.minesweeper.commands.SetterToolCommand;
import com.royalswans.minesweeper.settertool.Init;
import com.royalswans.minesweeper.settertool.SetFieldEvents;
import com.royalswans.minesweeper.settertool.SetterConfig;
import com.royalswans.minesweeper.settertool.SetterTool;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class MineSweeper extends JavaPlugin {

    @Override
    public void onEnable() {
        SetterTool.createTool();

        getCommand("getsetter").setExecutor(new SetterToolCommand());

        Bukkit.getPluginManager().registerEvents(new SetFieldEvents(this), this);

        SetterConfig.createCustomConfig(this);

        Init.init(this);
    }
}
