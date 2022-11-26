package com.royalswans.minesweeper.commands;

import com.royalswans.minesweeper.settertool.SetterTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetterToolCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.isOp()) {
                player.getInventory().addItem(SetterTool.getItem());
                return true;
            }
        }
        return false;
    }
}
